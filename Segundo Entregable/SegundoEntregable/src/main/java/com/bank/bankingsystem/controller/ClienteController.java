package com.bank.bankingsystem.controller;

import com.bank.bankingsystem.model.Cliente;
import com.bank.bankingsystem.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.bank.bankingsystem.model.CuentaBancaria;
import com.bank.bankingsystem.model.ReporteComisionesRequest;


@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public Flux<Cliente> getAllClientes() {
        return clienteService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Cliente> getClienteById(@PathVariable String id) {
        return clienteService.findById(id);
    }

    // Endpoint para crear un cliente
    @PostMapping
    public Mono<Cliente> createCliente(@RequestBody Cliente cliente) {
        return clienteService.save(cliente);
    }

    @PutMapping("/{id}")
    public Mono<Cliente> updateCliente(@PathVariable String id, @RequestBody Cliente cliente) {
        return clienteService.update(id, cliente);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCliente(@PathVariable String id) {
        return clienteService.delete(id);
    }

    // Endpoint para agregar una cuenta bancaria a un cliente
    @PostMapping("/{clienteId}/cuentas")
    public Mono<Cliente> addCuentaBancaria(@PathVariable String clienteId, @RequestBody CuentaBancaria cuentaBancaria) {
        return clienteService.addCuentaBancaria(clienteId, cuentaBancaria);
    }

    // Endpoint para obtener el resumen de saldos promedio diarios del mes en curso
    @GetMapping("/{clienteId}/resumen-saldos")
    public Mono<String> obtenerResumenSaldosPromedioDiarios(@PathVariable String clienteId) {
        return clienteService.obtenerResumenSaldosPromedioDiarios(clienteId);
    }

    // Endpoint para generar un reporte de comisiones cobradas por producto en un periodo de tiempo
    @PostMapping("/{clienteId}/reporte-comisiones")
    public Mono<String> generarReporteComisiones(@PathVariable String clienteId,
                                                 @RequestBody ReporteComisionesRequest request) {
        return clienteService.generarReporteComisiones(clienteId, request.getFechaInicio(), request.getFechaFin());
    }

}