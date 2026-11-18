package com.alexandre.academia.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author Alexandre
 */
public class Agendamento {

    private LocalDate dataAgendamento;
    private LocalTime horarioAgendamento;
    private Cliente alunoAgendamento;
    private Sala sala;
    private Instrutor instrutor;
    private String tipoAula;
    private StatusAgendamento status = StatusAgendamento.PRELIMINAR;
    private double valor;

    public Agendamento(LocalDate dataAgendamento, LocalTime horarioAgendamento,
                       Cliente alunoAgendamento, Sala sala, Instrutor instrutor,
                       String tipoAula, double valor) {
        this.dataAgendamento = dataAgendamento;
        this.horarioAgendamento = horarioAgendamento;
        this.alunoAgendamento = alunoAgendamento;
        this.sala = sala;
        this.instrutor = instrutor;
        this.tipoAula = tipoAula;
        this.valor = valor;
    }

    public Agendamento(LocalDate dataAgendamento, LocalTime horarioAgendamento) {
        this.dataAgendamento = dataAgendamento;
        this.horarioAgendamento = horarioAgendamento;
    }

    public Agendamento(Cliente alunoAgendamento) {
        this.alunoAgendamento = alunoAgendamento;
    }

    public Agendamento() {
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public LocalTime getHorarioAgendamento() {
        return horarioAgendamento;
    }

    public void setHorarioAgendamento(LocalTime horarioAgendamento) {
        this.horarioAgendamento = horarioAgendamento;
    }

    public Cliente getAlunoAgendamento() {
        return alunoAgendamento;
    }

    public void setAlunoAgendamento(Cliente alunoAgendamento) {
        this.alunoAgendamento = alunoAgendamento;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public Instrutor getInstrutor() {
        return instrutor;
    }

    public void setInstrutor(Instrutor instrutor) {
        this.instrutor = instrutor;
    }

    public String getTipoAula() {
        return tipoAula;
    }

    public void setTipoAula(String tipoAula) {
        this.tipoAula = tipoAula;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Agendamento{dataAgendamento=" + dataAgendamento
                + ", horarioAgendamento=" + horarioAgendamento
                + ", alunoAgendamento=" + alunoAgendamento
                + ", sala=" + sala
                + ", instrutor=" + instrutor
                + ", tipoAula=" + tipoAula
                + ", status=" + status
                + ", valor=" + valor + '}';
    }
}