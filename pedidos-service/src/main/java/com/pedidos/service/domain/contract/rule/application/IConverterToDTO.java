package com.pedidos.service.domain.contract.rule.application;

public interface IConverterToDTO<T> {
    T toDTO();
}
