package src.main.java.com.logistica.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.main.java.com.logistica.service.dto.EntregaDTO;
import src.main.java.com.logistica.service.infrastructure.exception.RegistroNaoEncontradoException;
import src.main.java.com.logistica.service.infrastructure.feign.Cliente;
import src.main.java.com.logistica.service.infrastructure.feign.ClienteServiceClient;
import src.main.java.com.logistica.service.infrastructure.feign.Pedido;
import src.main.java.com.logistica.service.infrastructure.feign.PedidoServiceClient;
import src.main.java.com.logistica.service.model.Entrega;
import src.main.java.com.logistica.service.repository.EntregaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EntregaService {
    private final EntregaRepository entregaRepository;
    private final ClienteServiceClient clienteServiceClient;
    private final PedidoServiceClient pedidoServiceClient;


    @Autowired
    public EntregaService(EntregaRepository entregaRepository, ClienteServiceClient clienteServiceClient, PedidoServiceClient pedidoServiceClient) {
        this.entregaRepository = entregaRepository;
        this.clienteServiceClient = clienteServiceClient;
        this.pedidoServiceClient = pedidoServiceClient;
    }

    public void atualizarEntrega(UUID idEntrega){
        final Entrega entrega = entregaRepository.findById(idEntrega).orElseThrow(()-> new RegistroNaoEncontradoException(idEntrega.toString()));
        entrega.setDataEntrega(LocalDateTime.now());
        entregaRepository.save(entrega);
        pedidoServiceClient.confirmarEntrega(idEntrega);
    }

    public Map<String, List<EntregaDTO>> processarPedidosPagosEAgruparPorCep() {
        List<Pedido> listaPedidosPagos = pedidoServiceClient.getRelatorioPedidosPagos();
        Map<String, List<EntregaDTO>> entregasAgrupadasPorCep = new HashMap<>();

        for (Pedido pedido : listaPedidosPagos) {

            pedidoServiceClient.confirmarTransporte(pedido.identificador());
            Cliente cliente = clienteServiceClient.getClienteById(pedido.cliente());
            String cepPrefixo = cliente.cep().substring(0, 5);


            EntregaDTO entregaDTO = new EntregaDTO(
                    UUID.randomUUID(),
                    pedido.identificador(),
                    cliente.cep(),
                    isCepDeSaoPaulo(cliente.cep()) ? LocalDate.now().plusDays(3) : LocalDate.now().plusDays(10) ,
                    null, // Data de entrega
                    "EM_TRANSITO" // Status entrega
            );

            entregaRepository.save(entregaDTO.toEntrega());


            entregasAgrupadasPorCep
                    .computeIfAbsent(cepPrefixo, k -> new ArrayList<>())
                    .add(entregaDTO);
        }

        return entregasAgrupadasPorCep;
    }

    public static boolean isCepDeSaoPaulo(String cep) {
        if (cep == null || cep.length() != 9 || !cep.matches("\\d{5}-\\d{3}")) {
            throw new IllegalArgumentException("CEP deve estar no formato 00000-000");
        }

        String cepPrefixo = cep.substring(0, 5);
        int prefix = Integer.parseInt(cepPrefixo);


        return (prefix >= 1000 && prefix <= 1999) || (prefix >= 60000 && prefix <= 199999);
    }


}
