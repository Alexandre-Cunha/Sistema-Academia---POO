package com.alexandre.academia.model;

import java.time.LocalDate;

/**
 *
 * @author Alexandre
 */
public class Consumo {

    private Cliente cliente;
    private Item item;
    private LocalDate data;

    public Consumo(Cliente cliente, Item item, LocalDate data) {
        this.cliente = cliente;
        this.item = item;
        this.data = data;
    }

    public Consumo() {
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    /**
     * Calcula o valor total do consumo 
     *
     * @return valor total, ou 0 se o item ou o produto não estiverem definidos
     */
    public double getValorTotal() {
        if (item == null || item.getProduto() == null) {
            return 0;
        }
        return item.getProduto().getPreco() * item.getQuantidade();
    }

    @Override
    public String toString() {
        return "Consumo{cliente=" + cliente
                + ", item=" + item
                + ", data=" + data
                + ", valorTotal=" + getValorTotal() + '}';
    }
}