package com.bank.bankingsystem.service;

import com.bank.bankingsystem.model.CuentaBancaria;
import com.bank.bankingsystem.repository.CuentaBancariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class CuentaBancariaService {

    @Autowired
    private CuentaBancariaRepository cuentaBancariaRepository;

    public Flux<CuentaBancaria> findAll() {
        return cuentaBancariaRepository.findAll();
    }

    public Mono<CuentaBancaria> findById(String id) {
        return cuentaBancariaRepository.findById(id);
    }

    public Mono<CuentaBancaria> save(CuentaBancaria cuentaBancaria) {
        return cuentaBancariaRepository.save(cuentaBancaria);
    }

    public Mono<CuentaBancaria> update(String id, CuentaBancaria cuentaBancaria) {
        return cuentaBancariaRepository.findById(id)
                .flatMap(existingCuenta -> {
                    existingCuenta.setNumeroCuenta(cuentaBancaria.getNumeroCuenta());
                    existingCuenta.setTipoCuenta(cuentaBancaria.getTipoCuenta());
                    existingCuenta.setSaldo(cuentaBancaria.getSaldo());
                    existingCuenta.setMovimientos(cuentaBancaria.getMovimientos());
                    existingCuenta.setMaxTransaccionesSinComision(cuentaBancaria.getMaxTransaccionesSinComision());
                    existingCuenta.setComisionPorTransaccion(cuentaBancaria.getComisionPorTransaccion());
                    return cuentaBancariaRepository.save(existingCuenta);
                });
    }

    public Mono<Void> delete(String id) {
        return cuentaBancariaRepository.deleteById(id);
    }

    // Metodo para transferir dinero entre cuentas del mismo cliente
    public Mono<Void> transferirEntreCuentas(String cuentaOrigenId, String cuentaDestinoId, double monto) {
        return cuentaBancariaRepository.findById(cuentaOrigenId)
                .flatMap(cuentaOrigen -> {
                    if (cuentaOrigen.getSaldo().doubleValue() < monto) {
                        return Mono.error(new RuntimeException("Saldo insuficiente en la cuenta de origen."));
                    }
                    return cuentaBancariaRepository.findById(cuentaDestinoId)
                            .flatMap(cuentaDestino -> {
                                cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(BigDecimal.valueOf(monto)));
                                cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(BigDecimal.valueOf(monto)));
                                return cuentaBancariaRepository.save(cuentaOrigen)
                                        .then(cuentaBancariaRepository.save(cuentaDestino))
                                        .then();
                            });
                });
    }

    // Metodo para transferir dinero a cuentas de terceros del mismo banco
    public Mono<Void> transferirATerceros(String cuentaOrigenId, String cuentaDestinoId, double monto) {
        return transferirEntreCuentas(cuentaOrigenId, cuentaDestinoId, monto);
    }
}