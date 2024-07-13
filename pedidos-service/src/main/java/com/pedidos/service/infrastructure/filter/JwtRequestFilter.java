package com.pedidos.service.infrastructure.filter;


import com.pedidos.service.domain.exception.ComunicacaoApiException;
import com.pedidos.service.infrastructure.config.JwtUserDetails;
import com.pedidos.service.infrastructure.feign.UsuarioServiceClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private UsuarioServiceClient authServiceClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String requestTokenHeader = request.getHeader("Authorization");

        if (isTokenValid(requestTokenHeader)) {
            try {
                var userDetailsVO = authServiceClient.validateToken(requestTokenHeader);
                JwtUserDetails jwtUserDetails = userDetailsVO.toJwtUserDetails();

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(jwtUserDetails.getUsername(), requestTokenHeader, jwtUserDetails.getAuthorities());


                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                chain.doFilter(request, response);
            } catch (ComunicacaoApiException comunicacaoApiException) {
                this.defineErroByHttpStatus(HttpStatus.valueOf(comunicacaoApiException.getCode()), response, comunicacaoApiException.getMessage());
            } catch (HttpClientErrorException httpClientErrorException) {
                handleHttpClientError(httpClientErrorException, response);
            } catch (Exception e) {
                log.error("Erro ao validar o token: {}", e.getMessage());
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isTokenValid(String token) {
        return token != null && token.startsWith("Bearer ");
    }

    private void handleHttpClientError(HttpClientErrorException e, HttpServletResponse response) throws IOException {
        String errorMessage = e.getMessage() != null ? e.getMessage() : "Erro desconhecido";
        HttpStatus statusCode = (HttpStatus) e.getStatusCode();
        this.defineErroByHttpStatus(statusCode, response, errorMessage);
    }

    private void defineErroByHttpStatus(final HttpStatus statusCode, HttpServletResponse response, String errorMessage) throws IOException {
        switch (statusCode) {
            case UNAUTHORIZED:
                log.error("Falha na autenticação: {}", errorMessage);
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Falha na autenticação: o token fornecido não possui privilégios ou é inválido ou expirado.");
                break;
            case FORBIDDEN:
                log.error("Acesso proibido: {}", errorMessage);
                response.sendError(HttpStatus.FORBIDDEN.value(), "Acesso proibido: você não tem permissão para acessar este recurso.");
                break;
            case BAD_REQUEST:
                log.error("Requisição inválida: {}", errorMessage);
                response.sendError(HttpStatus.BAD_REQUEST.value(), "Requisição inválida: verifique os dados enviados.");
                break;
            case NOT_FOUND:
                log.error("Recurso não encontrado: {}", errorMessage);
                response.sendError(HttpStatus.NOT_FOUND.value(), "Recurso não encontrado: verifique a URL.");
                break;
            default:
                log.error("Erro na requisição: {}", errorMessage);
                response.sendError(HttpStatus.BAD_REQUEST.value(), "Erro na requisição: verifique os dados enviados.");
                break;
        }
    }
}