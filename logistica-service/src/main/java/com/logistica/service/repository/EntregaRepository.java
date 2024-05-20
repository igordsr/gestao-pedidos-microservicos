package src.main.java.com.logistica.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.main.java.com.logistica.service.model.Entrega;

import java.util.List;
import java.util.UUID;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, UUID> {

    List<Entrega> findByCep(String cep);

}