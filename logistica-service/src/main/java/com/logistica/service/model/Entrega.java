package src.main.java.com.logistica.service.model;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Getter
@Setter
@Table(name = "entrega")
public class Entrega {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID pedidoId;

    private String cep;

    @Column(name = "data_previsao_entrega", nullable = false)
    private LocalDate dataPrevisaoEntrega;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;

    @Column(name = "status_entrega", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEntrega statusEntrega;

    public enum StatusEntrega {
        PENDENTE,
        PREPARANDO,
        EM_TRANSITO,
        ENTREGUE,
        CANCELADO
    }

}