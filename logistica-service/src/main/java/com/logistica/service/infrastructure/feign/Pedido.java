package src.main.java.com.logistica.service.infrastructure.feign;

import java.util.List;
import java.util.UUID;

public record Pedido(
        UUID identificador,
        UUID cliente,
        List<Item> itemList,
        String status
){

}