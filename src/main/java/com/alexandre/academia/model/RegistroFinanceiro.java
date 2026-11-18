package com.alexandre.academia.model;

import java.time.LocalDate;

/**
 * @author Alexandre
 */
public class RegistroFinanceiro {

    private String descricao;
    private TipoRegistro tipo;
    private double valor;
    private LocalDate data;

    public RegistroFinanceiro(String descricao, TipoRegistro tipo,
                              double valor, LocalDate data) {
        this.descricao = descricao;
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
    }

    public RegistroFinanceiro() {
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoRegistro getTipo() {
        return tipo;
    }

    public void setTipo(TipoRegistro tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RegistroFinanceiro{descricao=" + descricao
                + ", tipo=" + tipo
                + ", valor=" + valor
                + ", data=" + data + '}';
    }
}