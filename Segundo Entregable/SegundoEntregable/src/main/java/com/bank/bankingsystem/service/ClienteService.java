package com.bank.bankingsystem.service;

import com.bank.bankingsystem.model.enums.TipoCliente;
import com.bank.bankingsystem.model.enums.TipoCuenta;
import com.bank.bankingsystem.model.Cliente;
import com.bank.bankingsystem.model.CuentaBancaria;
import com.bank.bankingsystem.repository.ClienteRepository;
import com.bank.bankingsystem.repository.CuentaBancariaRepository;
import com.bank.bankingsystem.repository.CreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CuentaBancariaRepository cuentaBancariaRepository;

    @Autowired
    private CreditoRepository creditoRepository;

    public Flux<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Mono<Cliente> findById(String id) {
        return clienteRepository.findById(id);
    }

    public Mono<Cliente> save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Mono<Cliente> update(String id, Cliente cliente) {
        return clienteRepository.findById(id)
                .flatMap(existingCliente -> {
                    existingCliente.setNombre(cliente.getNombre());
                    existingCliente.setTipo(cliente.getTipo());
                    existingCliente.setProductos(cliente.getProductos());
                    return clienteRepository.save(existingCliente);
                });
    }

    public Mono<Void> delete(String id) {
        return clienteRepository.deleteById(id);
    }

    // Metodo para agregar una cuenta bancaria a un cliente
    public Mono<Cliente> addCuentaBancaria(String clienteId, CuentaBancaria cuentaBancaria) {
        return clienteRepository.findById(clienteId)
                .flatMap(cliente -> {
                    // Validar reglas de negocio
                    if (cliente.getTipo() == TipoCliente.PERSONAL) {
                        return cuentaBancariaRepository.findByClienteId(clienteId)
                                .filter(cuenta -> cuenta.getTipoCuenta() == cuentaBancaria.getTipoCuenta())
                                .hasElements()
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new RuntimeException("Un cliente personal solo puede tener una cuenta de cada tipo."));
                                    } else {
                                        cuentaBancaria.setClienteId(clienteId); // Asignar clienteId a la cuenta bancaria
                                        return cuentaBancariaRepository.save(cuentaBancaria)
                                                .flatMap(savedCuenta -> {
                                                    cliente.getProductos().add(savedCuenta.getId());
                                                    return clienteRepository.save(cliente);
                                                });
                                    }
                                });
                    } else if (cliente.getTipo() == TipoCliente.EMPRESARIAL) {
                        if (cuentaBancaria.getTipoCuenta() == TipoCuenta.AHORRO || cuentaBancaria.getTipoCuenta() == TipoCuenta.PLAZO_FIJO) {
                            return Mono.error(new RuntimeException("Un cliente empresarial no puede tener cuentas de ahorro o de plazo fijo."));
                        } else {
                            cuentaBancaria.setClienteId(clienteId); // Asignar clienteId a la cuenta bancaria
                            return cuentaBancariaRepository.save(cuentaBancaria)
                                    .flatMap(savedCuenta -> {
                                        cliente.getProductos().add(savedCuenta.getId());
                                        return clienteRepository.save(cliente);
                                    });
                        }
                    } else if (cliente.getTipo() == TipoCliente.VIP) {
                        return creditoRepository.findByClienteId(clienteId)
                                .hasElements()
                                .flatMap(hasCreditCard -> {
                                    if (!hasCreditCard) {
                                        return Mono.error(new RuntimeException("Un cliente VIP debe tener una tarjeta de crédito."));
                                    } else {
                                        cuentaBancaria.setClienteId(clienteId); // Asignar clienteId a la cuenta bancaria
                                        return cuentaBancariaRepository.save(cuentaBancaria)
                                                .flatMap(savedCuenta -> {
                                                    cliente.getProductos().add(savedCuenta.getId());
                                                    return clienteRepository.save(cliente);
                                                });
                                    }
                                });
                    } else if (cliente.getTipo() == TipoCliente.PYME) {
                        if (cuentaBancaria.getTipoCuenta() != TipoCuenta.CORRIENTE) {
                            return Mono.error(new RuntimeException("Un cliente PYME solo puede tener cuentas corrientes."));
                        }
                        return creditoRepository.findByClienteId(clienteId)
                                .hasElements()
                                .flatMap(hasCreditCard -> {
                                    if (!hasCreditCard) {
                                        return Mono.error(new RuntimeException("Un cliente PYME debe tener una tarjeta de crédito."));
                                    } else {
                                        cuentaBancaria.setClienteId(clienteId); // Asignar clienteId a la cuenta bancaria
                                        return cuentaBancariaRepository.save(cuentaBancaria)
                                                .flatMap(savedCuenta -> {
                                                    cliente.getProductos().add(savedCuenta.getId());
                                                    return clienteRepository.save(cliente);
                                                });
                                    }
                                });
                    } else {
                        return Mono.error(new RuntimeException("Tipo de cliente no soportado."));
                    }
                });
    }

    // Metodo para obtener el resumen de saldos promedio diarios del mes en curso
    public Mono<String> obtenerResumenSaldosPromedioDiarios(String clienteId) {
        return clienteRepository.findById(clienteId)
                .flatMap(cliente -> {
                    List<String> productos = cliente.getProductos();
                    return Flux.fromIterable(productos)
                            .flatMap(productoId -> cuentaBancariaRepository.findById(productoId)
                                    .map(cuenta -> "Cuenta: " + cuenta.getNumeroCuenta() + ", Saldo Promedio Diario: " + calcularSaldoPromedioDiario(cuenta)))
                            .collectList()
                            .map(resumen -> String.join("\n", resumen));
                });
    }

    // Metodo para calcular el saldo promedio diario de una cuenta bancaria
    private double calcularSaldoPromedioDiario(CuentaBancaria cuenta) {
        // Implementar la lógica para calcular el saldo promedio diario
        return cuenta.getSaldo().doubleValue(); // Ejemplo simplificado
    }

    // Metodo para generar un reporte de comisiones cobradas por producto en un periodo de tiempo
    public Mono<String> generarReporteComisiones(String clienteId, String fechaInicio, String fechaFin) {
        return clienteRepository.findById(clienteId)
                .flatMap(cliente -> {
                    List<String> productos = cliente.getProductos();
                    return Flux.fromIterable(productos)
                            .flatMap(productoId -> cuentaBancariaRepository.findById(productoId)
                                    .map(cuenta -> "Cuenta: " + cuenta.getNumeroCuenta() + ", Comisiones: " + calcularComisiones(cuenta, fechaInicio, fechaFin)))
                            .collectList()
                            .map(reporte -> String.join("\n", reporte));
                });
    }

    // Metodo para calcular las comisiones de una cuenta bancaria en un periodo de tiempo
    private double calcularComisiones(CuentaBancaria cuenta, String fechaInicio, String fechaFin) {
        return 0.0;
    }
}