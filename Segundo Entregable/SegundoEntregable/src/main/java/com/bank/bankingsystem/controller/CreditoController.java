package com.bank.bankingsystem.controller;

import com.bank.bankingsystem.model.Credito;
import com.bank.bankingsystem.service.CreditoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
        import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/creditos")
public class CreditoController {

    @Autowired
    private CreditoService creditoService;

    @GetMapping
    public Flux<Credito> getAllCreditos() {
        return creditoService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Credito> getCreditoById(@PathVariable String id) {
        return creditoService.findById(id);
    }

    // Endpoint para crear un credito
    @PostMapping
    public Mono<Credito> createCredito(@RequestBody Credito credito) {
        return creditoService.save(credito);
    }

    @PutMapping("/{id}")
    public Mono<Credito> updateCredito(@PathVariable String id, @RequestBody Credito credito) {
        return creditoService.update(id, credito);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCredito(@PathVariable String id) {
        return creditoService.delete(id);
    }
}
