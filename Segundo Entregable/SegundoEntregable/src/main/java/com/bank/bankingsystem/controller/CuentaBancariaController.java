package com.bank.bankingsystem.controller;

import com.bank.bankingsystem.model.CuentaBancaria;
import com.bank.bankingsystem.model.TransferenciaRequest;
import com.bank.bankingsystem.service.CuentaBancariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cuentas")
public class CuentaBancariaController {

    @Autowired
    private CuentaBancariaService cuentaBancariaService;

    @GetMapping
    public Flux<CuentaBancaria> getAllCuentas() {
        return cuentaBancariaService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<CuentaBancaria> getCuentaById(@PathVariable String id) {
        return cuentaBancariaService.findById(id);
    }

    // Endpoint para crear una cuenta bancaria
    @PostMapping
    public Mono<CuentaBancaria> createCuenta(@RequestBody CuentaBancaria cuentaBancaria) {
        return cuentaBancariaService.save(cuentaBancaria);
    }

    @PutMapping("/{id}")
    public Mono<CuentaBancaria> updateCuenta(@PathVariable String id, @RequestBody CuentaBancaria cuentaBancaria) {
        return cuentaBancariaService.update(id, cuentaBancaria);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCuenta(@PathVariable String id) {
        return cuentaBancariaService.delete(id);
    }

    // Endpoint para transferir dinero entre cuentas del mismo cliente
    @PostMapping("/transferir")
    public Mono<Void> transferirEntreCuentas(@RequestBody TransferenciaRequest request) {
        return cuentaBancariaService.transferirEntreCuentas(request.getCuentaOrigenId(), request.getCuentaDestinoId(), request.getMonto());
    }

    // Endpoint para transferir dinero a cuentas de terceros del mismo banco
    @PostMapping("/transferir-terceros")
    public Mono<Void> transferirATerceros(@RequestBody TransferenciaRequest request) {
        return cuentaBancariaService.transferirATerceros(request.getCuentaOrigenId(), request.getCuentaDestinoId(), request.getMonto());
    }
}