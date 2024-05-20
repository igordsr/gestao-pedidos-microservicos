package src.main.java.com.logistica.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.main.java.com.logistica.service.model.Entrega;
import src.main.java.com.logistica.service.repository.EntregaRepository;

import java.util.List;

@Service
public class EntregaService {
    private final EntregaRepository entregaRepository;

    @Autowired
    public EntregaService(EntregaRepository entregaRepository) {
        this.entregaRepository = entregaRepository;
    }

    public List<Entrega> buscarEntregasPendentes() {
        return entregaRepository.findAll();
    }

}
