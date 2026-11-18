package com.alexandre.academia.model;

import java.time.LocalDate;

/**
 * @author Alexandre
 */
public class Mensalidade {

    private Cliente cliente;
    private double valor;
    private LocalDate dataVencimento;
    private boolean paga;

    public Mensalidade(Cliente cliente, double valor, LocalDate dataVencimento) {
        this.cliente = cliente;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
    }

    public Mensalidade() {
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public boolean isPaga() {
        return paga;
    }

    public void setPaga(boolean paga) {
        this.paga = paga;
    }

    @Override
    public String toString() {
        return "Mensalidade{cliente=" + cliente
                + ", valor=" + valor
                + ", dataVencimento=" + dataVencimento
                + ", paga=" + paga + '}';
    }
}