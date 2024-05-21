package com.logistica.service.service;

import com.logistica.service.dto.EntregaDTO;
import com.logistica.service.infrastructure.feign.ClienteServiceClient;
import com.logistica.service.infrastructure.feign.PedidoServiceClient;
import com.logistica.service.infrastructure.feign.vo.Cliente;
import com.logistica.service.infrastructure.feign.vo.Item;
import com.logistica.service.infrastructure.feign.vo.Pedido;
import com.logistica.service.model.Entrega;
import com.logistica.service.repository.EntregaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EntregaServiceTest {

    @Mock
    private EntregaRepository entregaRepository;

    @Mock
    private ClienteServiceClient clienteServiceClient;

    @Mock
    private PedidoServiceClient pedidoServiceClient;

    @InjectMocks
    private EntregaService entregaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAtualizarEntrega() {
        UUID idEntrega = UUID.randomUUID();
        Entrega entrega = new Entrega();
        entrega.setPedidoId(UUID.randomUUID());
        Cliente cliente = new Cliente(UUID.randomUUID(), "Cliente 1", "10000-000", "Logradouro 1", "Complemento 1", "Bairro 1", "1", "1111111111", "cliente1@example.com", LocalDate.now(), "11111111111");
        when(entregaRepository.findById(idEntrega)).thenReturn(Optional.of(entrega));
        when(clienteServiceClient.getClienteById(any(UUID.class))).thenReturn(cliente);

        entregaService.atualizarEntrega(idEntrega);

        assertNotNull(entrega.getDataEntrega());
        verify(entregaRepository).save(entrega);
        verify(pedidoServiceClient).confirmarEntrega(entrega.getPedidoId());
    }

    @Test
    public void testProcessarPedidosPagosEAgruparPorCep() {
        Pedido pedido = this.getPedido();
        Cliente cliente1 = new Cliente(pedido.cliente(), "Cliente 1", "10000-000", "Logradouro 1", "Complemento 1", "Bairro 1", "1", "1111111111", "cliente1@example.com", LocalDate.now(), "11111111111");

        List<Pedido> listaPedidosPagos = Arrays.asList(pedido);

        when(pedidoServiceClient.getRelatorioPedidosPagos()).thenReturn(listaPedidosPagos);
        when(clienteServiceClient.getClienteById(any(UUID.class))).thenReturn(cliente1);
        when(entregaRepository.save(any(Entrega.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, List<EntregaDTO>> result = entregaService.processarPedidosPagosEAgruparPorCep();

        assertEquals(1, result.size());
        assertTrue(result.containsKey("10000"));

        verify(pedidoServiceClient).confirmarTransporte(pedido.identificador());
        verify(entregaRepository, times(1)).save(any(Entrega.class));
    }

    @Test
    public void testIsCepDeSaoPaulo() {
        assertTrue(EntregaService.isCepDeSaoPaulo("01000-000"));
        assertFalse(EntregaService.isCepDeSaoPaulo("06000-000"));
        assertFalse(EntregaService.isCepDeSaoPaulo("20000-000"));
        assertThrows(IllegalArgumentException.class, () -> EntregaService.isCepDeSaoPaulo("00000"));
        assertThrows(IllegalArgumentException.class, () -> EntregaService.isCepDeSaoPaulo("00000-00a"));
    }


    public Pedido getPedido() {
        return new Pedido(
                UUID.fromString("30f88a6e-a701-4eda-b812-5053ccb419ed"),
                UUID.fromString("e3d4133c-c6aa-4a16-a104-241dffad037b"),
                List.of(getItem()),
                "PAGO"
        );
    }
    public static Item getItem() {
        return new Item(UUID.fromString("232d0ce3-1ffd-4b22-91d6-ee6e45834167"), 100);
    }


}