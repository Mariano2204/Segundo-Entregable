package com.bank.bankingsystem.model;


import com.bank.bankingsystem.model.enums.TipoCliente;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Cliente {
    @Id
    private String id;
    private String nombre;
    private TipoCliente tipo;
    private List<String> productos;

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(TipoCliente tipo) {
        this.tipo = tipo;
    }

    public void setProductos(List<String> productos) {
        this.productos = productos;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

    public List<String> getProductos() {
        return productos;
    }
}