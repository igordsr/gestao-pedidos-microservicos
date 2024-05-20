package src.main.java.com.logistica.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import src.main.java.com.logistica.service.dto.EntregaDTO;
import src.main.java.com.logistica.service.infrastructure.exception.ComunicacaoApiException;
import src.main.java.com.logistica.service.service.EntregaService;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping(value = "/entrega", produces = {"application/json"})
@Tag(name = "Entrega")
public class EntregaController {
    private final EntregaService entregaService;

    public EntregaController(EntregaService entregaService) {
        this.entregaService = entregaService;
    }

    @GetMapping
    @Operation(summary = "Consultar Relatório de Entrega", description = "Consultar os dados de entregas", method = "GET")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Relatório de Entrega realizado com sucesso"),})
    public ResponseEntity<Map<String, List<EntregaDTO>>> getRelatorioDeEntregas() throws ComunicacaoApiException {
        return new ResponseEntity<>(entregaService.processarPedidosPagosEAgruparPorCep(), HttpStatus.OK);
    }
}
