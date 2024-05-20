package src.main.java.com.logistica.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import src.main.java.com.logistica.service.model.Entrega;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record EntregaDTO(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        UUID id,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        UUID pedidoId,
        @Schema(example = "08793-020")
        @NotNull(message = "O cep não pode ser nulo")
        @NotBlank(message = "O cep não pode ser em branco")
        String cep,
        @Schema(example = "2024-05-17")
        @NotNull(message = "Data previsão de entrega não pode ser null")
        @NotBlank(message = "Data previsão de entrega não pode ser vazia")
        LocalDate dataPrevisaoEntrega,

        @Schema(example = "2024-05-17T10:15:30")
        LocalDateTime dataEntrega,

        @Schema(example = "EM_TRANSITO")
        String statusEntrega
) {
        public Entrega toEntrega(){
                Entrega entrega = new Entrega();
                entrega.setId(this.id);
                entrega.setPedidoId(this.pedidoId);
                entrega.setCep(this.cep);
                entrega.setDataPrevisaoEntrega(this.dataPrevisaoEntrega);
                entrega.setDataEntrega(this.dataEntrega);
                entrega.setStatusEntrega(Entrega.StatusEntrega.valueOf(this.statusEntrega));
                return entrega;
        }

        public static EntregaDTO getInstance(final Entrega entrega) {
                Assert.notNull(entrega, "entrega cannot be null");
                return new EntregaDTO(
                        entrega.getId(),
                        entrega.getPedidoId(),
                        entrega.getCep(),
                        entrega.getDataPrevisaoEntrega(),
                        entrega.getDataEntrega(),
                        entrega.getStatusEntrega().name()
                );
        }
}
