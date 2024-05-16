package com.pedidos.service.domain.usecase;

import com.pedidos.service.domain.contract.IClienteContract;
import com.pedidos.service.domain.contract.IManderDadosPedidoContract;
import com.pedidos.service.domain.contract.IProdutoContract;
import com.pedidos.service.domain.exception.GestaoDePedidosApplicationException;
import com.pedidos.service.domain.exception.PedidoNotFoundException;
import com.pedidos.service.domain.model.Item;
import com.pedidos.service.domain.model.Pedido;
import com.pedidos.service.domain.model.StatusPedido;
import com.pedidos.service.util.InstanceGeneratorHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class PedidoUseCaseContractTest {
    @Mock
    private IClienteContract manterCliente;
    @Mock
    private IProdutoContract materProduto;
    @Mock
    private IManderDadosPedidoContract manterPedido;
    private PedidoUseCaseContract pedidoUseCase;
    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        this.pedidoUseCase = new PedidoUseCaseContract(this.manterCliente, this.materProduto, this.manterPedido);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void cadastrar() {
        final Pedido pedido = InstanceGeneratorHelper.getPedido();
        doNothing().when(this.manterCliente).verificarExistencia(any(UUID.class));
        when(this.materProduto.consultarProdutos(anyList())).thenReturn(pedido.getItemList());
        when(this.manterPedido.cadastrar(any(Pedido.class))).thenReturn(pedido);

        final Pedido result = this.pedidoUseCase.cadastrar(pedido);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getIdentificador()).isNotNull(),
                () -> assertEquals(result.getCliente(), pedido.getCliente()),
                () -> assertEquals(result.getStatusPedido().name(), StatusPedido.AGUARDANDO_PAGAMENTO.name()),
                () -> verify(this.manterCliente, times(1)).verificarExistencia(any(UUID.class)),
                () -> verify(this.materProduto, times(1)).consultarProdutos(anyList()),
                () -> verify(this.manterPedido, times(1)).cadastrar(any(Pedido.class))
        );
    }

    @Test
    void liquidarPedido() {
        final UUID identificador = UUID.fromString("30f88a6e-a701-4eda-b812-5053ccb419ed");
        final Pedido pedido = new Pedido(identificador, UUID.fromString("e3d4133c-c6aa-4a16-a104-241dffad037b"), List.of(InstanceGeneratorHelper.getItem()), StatusPedido.AGUARDANDO_PAGAMENTO);
        when(this.manterPedido.consultarPeloIdentificador(any(UUID.class))).thenReturn(pedido);
        when(this.manterPedido.atualizar(any(Pedido.class))).thenReturn(pedido);

        final Pedido result = this.pedidoUseCase.liquidarPedido(identificador);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(result, pedido),
                () -> assertEquals(result.getStatusPedido().name(), StatusPedido.PAGO.name()),
                () -> verify(this.manterPedido, times(1)).consultarPeloIdentificador(any(UUID.class)),
                () -> verify(this.manterPedido, times(1)).atualizar(any(Pedido.class))
        );
    }

    @Test
    void consultar() {
        final UUID identificador = UUID.fromString("30f88a6e-a701-4eda-b812-5053ccb419ed");
        final Pedido pedido = new Pedido(identificador, UUID.fromString("e3d4133c-c6aa-4a16-a104-241dffad037b"), List.of(InstanceGeneratorHelper.getItem()), StatusPedido.PAGO);
        when(this.manterPedido.consultarPeloIdentificador(any(UUID.class))).thenReturn(pedido);

        final Pedido result = this.pedidoUseCase.consultarPeloIdentificador(identificador);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(result, pedido),
                () -> verify(this.manterPedido, times(1)).consultarPeloIdentificador(any(UUID.class))
        );
    }

    @Test
    void gerarRelatorio() {
        final UUID identificador = UUID.fromString("30f88a6e-a701-4eda-b812-5053ccb419ed");
        final Pedido pedido = new Pedido(identificador, UUID.fromString("e3d4133c-c6aa-4a16-a104-241dffad037b"), List.of(InstanceGeneratorHelper.getItem()), StatusPedido.PAGO);
        List<Pedido> pedidos = List.of(pedido);
        when(this.manterPedido.consultarPeloStatus(StatusPedido.PAGO)).thenReturn(pedidos);
        when(this.manterPedido.atualizar(pedidos)).thenReturn(pedidos);

        List<Pedido> result = this.pedidoUseCase.gerarRelatorioPedidosPagos();

        assertAll(
                () -> assertThat(result).isNotNull().isNotEmpty().extracting(Pedido::getStatusPedido).containsOnly(StatusPedido.PREPARANDO_PARA_ENVIO),
                () -> verify(this.manterPedido, times(1)).atualizar(anyList())
        );
    }

    @Test
    void enviar() {
        final UUID identificador = UUID.fromString("30f88a6e-a701-4eda-b812-5053ccb419ed");
        final Pedido pedido = new Pedido(identificador, UUID.fromString("e3d4133c-c6aa-4a16-a104-241dffad037b"), List.of(InstanceGeneratorHelper.getItem()), StatusPedido.PAGO);
        when(this.manterPedido.consultarPeloIdentificador(any(UUID.class))).thenReturn(pedido);
        when(this.manterPedido.atualizar(any(Pedido.class))).thenReturn(pedido);

        final Pedido result = this.pedidoUseCase.enviar(identificador);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(result, pedido),
                () -> assertEquals(result.getStatusPedido().name(), StatusPedido.AGUARDANDO_ENTREGA.name()),
                () -> verify(this.manterPedido, times(1)).consultarPeloIdentificador(any(UUID.class)),
                () -> verify(this.manterPedido, times(1)).atualizar(any(Pedido.class))
        );
    }

    @Test
    void entregar() {
        final UUID identificador = UUID.fromString("30f88a6e-a701-4eda-b812-5053ccb419ed");
        final Pedido pedido = new Pedido(identificador, UUID.fromString("e3d4133c-c6aa-4a16-a104-241dffad037b"), List.of(InstanceGeneratorHelper.getItem()), StatusPedido.AGUARDANDO_ENTREGA);
        when(this.manterPedido.consultarPeloIdentificador(any(UUID.class))).thenReturn(pedido);
        when(this.manterPedido.atualizar(any(Pedido.class))).thenReturn(pedido);

        final Pedido result = this.pedidoUseCase.entregar(identificador);
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertEquals(result, pedido),
                () -> assertEquals(result.getStatusPedido().name(), StatusPedido.ENTREGUE.name()),
                () -> verify(this.manterPedido, times(1)).consultarPeloIdentificador(any(UUID.class)),
                () -> verify(this.manterPedido, times(1)).atualizar(any(Pedido.class))
        );
    }


    @Test
    void cadastrarProdutoInsuficienteException() {
        final Pedido pedido = InstanceGeneratorHelper.getPedido();
        final Item item = new Item(UUID.fromString("232d0ce3-1ffd-4b22-91d6-ee6e45834167"), 50);
        doNothing().when(this.manterCliente).verificarExistencia(any(UUID.class));
        when(this.materProduto.consultarProdutos(anyList())).thenReturn(List.of(item));

        assertAll(
                () -> assertThatThrownBy(() -> this.pedidoUseCase.cadastrar(pedido)).isInstanceOf(GestaoDePedidosApplicationException.class).hasMessage("NÃO HÁ UNIDADES SUFICIENTES DO PRODUTO 232D0CE3-1FFD-4B22-91D6-EE6E45834167 PARA A DEMANDA SOLICITADA"),
                () -> verify(this.manterCliente, times(1)).verificarExistencia(any(UUID.class)),
                () -> verify(this.materProduto, times(1)).consultarProdutos(anyList()),
                () -> verify(this.manterPedido, times(0)).cadastrar(any(Pedido.class))
        );
    }

    @Test
    void cadastrarProdutoNotFoundException() {
        final Pedido pedido = InstanceGeneratorHelper.getPedido();
        doNothing().when(this.manterCliente).verificarExistencia(any(UUID.class));
        when(this.materProduto.consultarProdutos(anyList())).thenReturn(List.of());

        assertAll(
                () -> assertThatThrownBy(() -> this.pedidoUseCase.cadastrar(pedido)).isInstanceOf(GestaoDePedidosApplicationException.class).hasMessage("PRODUTO 232D0CE3-1FFD-4B22-91D6-EE6E45834167 NÃO FOI ENCONTRADO"),
                () -> verify(this.manterCliente, times(1)).verificarExistencia(any(UUID.class)),
                () -> verify(this.materProduto, times(1)).consultarProdutos(anyList()),
                () -> verify(this.manterPedido, times(0)).cadastrar(any(Pedido.class))
        );
    }

    @Test
    void liquidarPedidoPedidoNotFoundException() {
        final UUID identificador = UUID.fromString("30f88a6e-a701-4eda-b812-5053ccb419ed");
        when(this.manterPedido.consultarPeloIdentificador(any(UUID.class))).thenThrow(new PedidoNotFoundException(identificador.toString()));

        assertAll(
                () -> assertThatThrownBy(() -> this.pedidoUseCase.liquidarPedido(identificador)).isInstanceOf(GestaoDePedidosApplicationException.class).hasMessage("PEDIDO 30F88A6E-A701-4EDA-B812-5053CCB419ED NÃO FOI ENCONTRADO"),
                () -> verify(this.manterPedido, times(1)).consultarPeloIdentificador(any(UUID.class)),
                () -> verify(this.manterPedido, times(0)).atualizar(any(Pedido.class))
        );
    }

    @Test
    void consultarPedidoPedidoNotFoundException() {
        final UUID identificador = UUID.fromString("30f88a6e-a701-4eda-b812-5053ccb419ed");
        when(this.manterPedido.consultarPeloIdentificador(any(UUID.class))).thenThrow(new PedidoNotFoundException(identificador.toString()));

        assertAll(
                () -> assertThatThrownBy(() -> this.pedidoUseCase.consultarPeloIdentificador(identificador)).isInstanceOf(GestaoDePedidosApplicationException.class).hasMessage("PEDIDO 30F88A6E-A701-4EDA-B812-5053CCB419ED NÃO FOI ENCONTRADO"),
                () -> verify(this.manterPedido, times(1)).consultarPeloIdentificador(any(UUID.class))
        );
    }

    @Test
    void enviarPedidoPedidoNotFoundException() {
        final UUID identificador = UUID.fromString("30f88a6e-a701-4eda-b812-5053ccb419ed");
        when(this.manterPedido.consultarPeloIdentificador(any(UUID.class))).thenThrow(new PedidoNotFoundException(identificador.toString()));
        assertAll(
                () -> assertThatThrownBy(() -> this.pedidoUseCase.enviar(identificador)).isInstanceOf(GestaoDePedidosApplicationException.class).hasMessage("PEDIDO 30F88A6E-A701-4EDA-B812-5053CCB419ED NÃO FOI ENCONTRADO"),
                () -> verify(this.manterPedido, times(0)).atualizar(any(Pedido.class))
        );
    }

    @Test
    void entregarPedidoPedidoNotFoundException() {
        final UUID identificador = UUID.fromString("30f88a6e-a701-4eda-b812-5053ccb419ed");
        when(this.manterPedido.consultarPeloIdentificador(any(UUID.class))).thenThrow(new PedidoNotFoundException(identificador.toString()));
        assertAll(
                () -> assertThatThrownBy(() -> this.pedidoUseCase.entregar(identificador)).isInstanceOf(GestaoDePedidosApplicationException.class).hasMessage("PEDIDO 30F88A6E-A701-4EDA-B812-5053CCB419ED NÃO FOI ENCONTRADO"),
                () -> verify(this.manterPedido, times(0)).atualizar(any(Pedido.class))
        );
    }

}