package com.alexandre.academia.model;

import java.time.LocalDateTime;

/**
 * @author Alexandre
 */
public class RegistroCatraca {

    public static final String ENTRADA = "ENTRADA";
    public static final String SAIDA = "SAIDA";

    private Cliente cliente;
    private LocalDateTime horario;
    private String tipoPassagem;

    public RegistroCatraca(Cliente cliente, String tipoPassagem) {
        this.cliente = cliente;
        this.tipoPassagem = tipoPassagem;
        this.horario = LocalDateTime.now();
    }

    public RegistroCatraca(Cliente cliente) {
        this.cliente = cliente;
        this.horario = LocalDateTime.now();
    }

    public RegistroCatraca() {
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

    public String getTipoPassagem() {
        return tipoPassagem;
    }

    public void setTipoPassagem(String tipoPassagem) {
        this.tipoPassagem = tipoPassagem;
    }

    @Override
    public String toString() {
        return "RegistroCatraca{cliente=" + cliente
                + ", horario=" + horario
                + ", tipoPassagem=" + tipoPassagem + '}';
    }
}