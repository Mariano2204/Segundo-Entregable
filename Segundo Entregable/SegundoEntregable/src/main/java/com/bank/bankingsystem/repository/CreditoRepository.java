package com.bank.bankingsystem.repository;

import com.bank.bankingsystem.model.Credito;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CreditoRepository extends ReactiveMongoRepository<Credito, String> {
    // Metodo para encontrar creditos por clienteId
    Flux<Credito> findByClienteId(String clienteId);
}




