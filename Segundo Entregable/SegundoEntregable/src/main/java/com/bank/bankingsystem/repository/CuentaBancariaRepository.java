package com.bank.bankingsystem.repository;

import com.bank.bankingsystem.model.CuentaBancaria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CuentaBancariaRepository extends ReactiveMongoRepository<CuentaBancaria, String> {
    // Metodo para encontrar cuentas bancarias por clienteId
    Flux<CuentaBancaria> findByClienteId(String clienteId);
}




