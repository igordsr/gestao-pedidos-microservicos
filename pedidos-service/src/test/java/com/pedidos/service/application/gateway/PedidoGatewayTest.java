package com.pedidos.service.application.gateway;

import com.pedidos.service.domain.contract.IClienteContract;
import com.pedidos.service.domain.contract.IManderDadosPedidoContract;
import com.pedidos.service.domain.contract.IProdutoContract;
import com.pedidos.service.domain.dto.PedidoDTO;
import com.pedidos.service.domain.dto.RelatorioDTO;
import com.pedidos.service.domain.model.Pedido;
import com.pedidos.service.domain.model.StatusPedido;
import com.pedidos.service.util.InstanceGeneratorHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class PedidoGatewayTest {
    @Mock
    IClienteContract manterCliente;
    @Mock
    IProdutoContract materProduto;
    @Mock
    IManderDadosPedidoContract manterPedido;
    PedidoGateway pedidoGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.pedidoGateway = new PedidoGateway(manterCliente, materProduto, manterPedido);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void consultarPeloIdentificador() {
        final UUID identificador = UUID.fromString("30f88a6e-a701-4eda-b812-5053ccb419ed");
        final Pedido pedido = InstanceGeneratorHelper.getPedido();
        final PedidoDTO pedidoDTO = InstanceGeneratorHelper.getPedidoDTO();
        when(this.manterPedido.consultarPeloIdentificador(any(UUID.class))).thenReturn(pedido);

        final PedidoDTO result = this.pedidoGateway.consultarPeloIdentificador(identificador);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> Assertions.assertEquals(Objects.requireNonNull(result).cliente(), pedidoDTO.cliente()),
                () -> Assertions.assertEquals(Objects.requireNonNull(result).status(), pedidoDTO.status()),
                () -> Assertions.assertEquals(Objects.requireNonNull(result).itemList(), pedidoDTO.itemList()),
                () -> verify(this.manterPedido, times(1)).consultarPeloIdentificador(any(UUID.class))
        );
    }

    @Test
    void cadastrar() {
        final Pedido pedido = InstanceGeneratorHelper.getPedido();
        final PedidoDTO pedidoDTO = InstanceGeneratorHelper.getPedidoDTO();
        doNothing().when(this.manterCliente).verificarExistencia(any(UUID.class));
        when(this.materProduto.consultarProdutos(anyList())).thenReturn(pedido.getItemList());
        when(this.manterPedido.cadastrar(any(Pedido.class))).thenReturn(pedido);

        PedidoDTO result = this.pedidoGateway.cadastrar(pedidoDTO);

        assertAll(
                () -> assertThat(result).isNotNull().isEqualTo(result),
                () -> verify(this.manterPedido, times(1)).cadastrar(any(Pedido.class))
        );
    }

    @Test
    void gerarRelatorioPedidosPagos() {
        final UUID identificador = UUID.fromString("30f88a6e-a701-4eda-b812-5053ccb419ed");
        final Pedido pedido = new Pedido(identificador, UUID.fromString("e3d4133c-c6aa-4a16-a104-241dffad037b"), List.of(InstanceGeneratorHelper.getItem()), StatusPedido.PAGO);
        final RelatorioDTO pedidoDTO = new RelatorioDTO(identificador, UUID.fromString("e3d4133c-c6aa-4a16-a104-241dffad037b"), List.of(InstanceGeneratorHelper.getItemDTO()));
        when(this.manterPedido.consultarPeloStatus(anyList())).thenReturn(List.of(pedido));
        when(this.manterPedido.atualizar(anyList())).thenReturn(List.of(pedido));

        List<RelatorioDTO> result = this.pedidoGateway.gerarRelatorioPedidosPagos();

        assertAll(
                () -> assertThat(result).isNotNull().isNotEmpty().containsExactly(pedidoDTO),
                () -> verify(this.manterPedido, times(1)).consultarPeloStatus(anyList()),
                () -> verify(this.manterPedido, times(1)).atualizar(anyList())
        );
    }

    @Test
    void liquidarPedido() {
        final UUID identificador = UUID.fromString("30f88a6e-a701-4eda-b812-5053ccb419ed");
        final Pedido pedido = InstanceGeneratorHelper.getPedido();
        final PedidoDTO pedidoDTO = InstanceGeneratorHelper.getPedidoDTO();
        when(this.manterPedido.consultarPeloIdentificador(any(UUID.class))).thenReturn(pedido);
        when(this.materProduto.diminuirQuantidadeProdutoEstoque(any(UUID.class), any(Integer.class))).thenReturn(InstanceGeneratorHelper.getItem());
        when(this.manterPedido.atualizar(any(Pedido.class))).thenReturn(pedido);

        final PedidoDTO result = this.pedidoGateway.liquidarPedido(identificador);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> Assertions.assertEquals(Objects.requireNonNull(result).cliente(), pedidoDTO.cliente()),
                () -> Assertions.assertEquals(StatusPedido.AGUARDANDO_PAGAMENTO.getDescricao(), pedidoDTO.status()),
                () -> Assertions.assertEquals(Objects.requireNonNull(result).itemList(), pedidoDTO.itemList()),
                () -> verify(this.manterPedido, times(1)).consultarPeloIdentificador(any(UUID.class)),
                () -> verify(this.materProduto, times(1)).diminuirQuantidadeProdutoEstoque(any(UUID.class), any(Integer.class)),
                () -> verify(this.manterPedido, times(1)).atualizar(any(Pedido.class))
        );
    }

    @Test
    void enviar() {
        final UUID identificador = UUID.fromString("30f88a6e-a701-4eda-b812-5053ccb419ed");
        final Pedido pedido = new Pedido(identificador, UUID.fromString("e3d4133c-c6aa-4a16-a104-241dffad037b"), List.of(InstanceGeneratorHelper.getItem()), StatusPedido.PREPARANDO_PARA_ENVIO);
        final PedidoDTO pedidoDTO = new PedidoDTO(identificador, UUID.fromString("e3d4133c-c6aa-4a16-a104-241dffad037b"), List.of(InstanceGeneratorHelper.getItemDTO()), StatusPedido.PREPARANDO_PARA_ENVIO.getDescricao());
        when(this.manterPedido.consultarPeloIdentificador(any(UUID.class))).thenReturn(pedido);
        when(this.manterPedido.atualizar(any(Pedido.class))).thenReturn(pedido);

        final PedidoDTO result = this.pedidoGateway.enviar(identificador);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> Assertions.assertEquals(Objects.requireNonNull(result).cliente(), pedidoDTO.cliente()),
                () -> Assertions.assertEquals(StatusPedido.AGUARDANDO_ENTREGA.getDescricao(), result.status()),
                () -> Assertions.assertEquals(Objects.requireNonNull(result).itemList(), pedidoDTO.itemList()),
                () -> verify(this.manterPedido, times(1)).consultarPeloIdentificador(any(UUID.class)),
                () -> verify(this.manterPedido, times(1)).atualizar(any(Pedido.class))
        );
    }

    @Test
    void entregar() {
        final UUID identificador = UUID.fromString("30f88a6e-a701-4eda-b812-5053ccb419ed");
        final Pedido pedido = new Pedido(identificador, UUID.fromString("e3d4133c-c6aa-4a16-a104-241dffad037b"), List.of(InstanceGeneratorHelper.getItem()), StatusPedido.AGUARDANDO_ENTREGA);
        final PedidoDTO pedidoDTO = new PedidoDTO(identificador, UUID.fromString("e3d4133c-c6aa-4a16-a104-241dffad037b"), List.of(InstanceGeneratorHelper.getItemDTO()), StatusPedido.AGUARDANDO_ENTREGA.getDescricao());
        when(this.manterPedido.consultarPeloIdentificador(any(UUID.class))).thenReturn(pedido);
        when(this.manterPedido.atualizar(any(Pedido.class))).thenReturn(pedido);

        final PedidoDTO result = this.pedidoGateway.entregar(identificador);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> Assertions.assertEquals(pedidoDTO.cliente(), Objects.requireNonNull(result).cliente()),
                () -> Assertions.assertEquals(StatusPedido.ENTREGUE.getDescricao(), result.status()),
                () -> Assertions.assertEquals(pedidoDTO.itemList(), Objects.requireNonNull(result).itemList()),
                () -> verify(this.manterPedido, times(1)).consultarPeloIdentificador(any(UUID.class)),
                () -> verify(this.manterPedido, times(1)).atualizar(any(Pedido.class))
        );
    }
}