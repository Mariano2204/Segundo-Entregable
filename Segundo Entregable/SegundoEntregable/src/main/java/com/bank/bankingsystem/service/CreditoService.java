package com.bank.bankingsystem.service;

import com.bank.bankingsystem.model.Credito;
import com.bank.bankingsystem.repository.CreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditoService {

    @Autowired
    private CreditoRepository creditoRepository;

    public Flux<Credito> findAll() {
        return creditoRepository.findAll();
    }

    public Mono<Credito> findById(String id) {
        return creditoRepository.findById(id);
    }

    public Mono<Credito> save(Credito credito) {
        return creditoRepository.save(credito);
    }

    public Mono<Credito> update(String id, Credito credito) {
        return creditoRepository.findById(id)
                .flatMap(existingCredito -> {
                    existingCredito.setTipoCredito(credito.getTipoCredito());
                    existingCredito.setLimiteCredito(credito.getLimiteCredito());
                    existingCredito.setSaldo(credito.getSaldo());
                    return creditoRepository.save(existingCredito);
                });
    }

    public Mono<Void> delete(String id) {
        return creditoRepository.deleteById(id);
    }
}
