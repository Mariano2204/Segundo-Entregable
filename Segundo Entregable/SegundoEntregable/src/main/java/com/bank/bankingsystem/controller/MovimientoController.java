package com.bank.bankingsystem.controller;

import com.bank.bankingsystem.model.Movimiento;
import com.bank.bankingsystem.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
        import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @GetMapping
    public Flux<Movimiento> getAllMovimientos() {
        return movimientoService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Movimiento> getMovimientoById(@PathVariable String id) {
        return movimientoService.findById(id);
    }

    // Endpoint para crear un movimiento bancario
    @PostMapping
    public Mono<Movimiento> createMovimiento(@RequestBody Movimiento movimiento) {
        return movimientoService.save(movimiento);
    }

    @PutMapping("/{id}")
    public Mono<Movimiento> updateMovimiento(@PathVariable String id, @RequestBody Movimiento movimiento) {
        return movimientoService.update(id, movimiento);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteMovimiento(@PathVariable String id) {
        return movimientoService.delete(id);
    }
}
