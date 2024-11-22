package com.bank.bankingsystem.repository;

import com.bank.bankingsystem.model.Movimiento;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoRepository extends ReactiveMongoRepository<Movimiento, String> {
}
