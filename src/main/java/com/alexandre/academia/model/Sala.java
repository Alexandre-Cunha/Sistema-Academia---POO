package com.alexandre.academia.model;

/**
 * @author Alexandre
 */
public class Sala {
    private String tipoSala;
    private int capacidade;

    public Sala() {
    }

    public Sala(String tipoSala, int capacidade) {
        this.tipoSala = tipoSala;
        this.capacidade = capacidade;
    }

    public String getTipoSala() {
        return tipoSala;
    }

    public void setTipoSala(String tipoSala) {
        this.tipoSala = tipoSala;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    @Override
    public String toString() {
        return "Sala{tipoSala=" + tipoSala + ", capacidade=" + capacidade + '}';
    }
}