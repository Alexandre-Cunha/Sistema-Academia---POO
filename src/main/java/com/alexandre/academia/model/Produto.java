
package com.alexandre.academia.model;

/**
 * Representa um produto comercializado pela academia.
 *
 * @author Alexandre
 */
public class Produto implements Identificavel {

    public static final String LOJA = "LOJA";
    public static final String LANCHONETE = "LANCHONETE";

    private int id;
    private String descricao;
    private String nomeProduto;
    private double preco;
    private String categoria;
    private String setor;
    private boolean ativo = true;

    public Produto(int id, String descricao, String nomeProduto,
                   double preco, String categoria, String setor) {
        this.id = id;
        this.descricao = descricao;
        this.nomeProduto = nomeProduto;
        this.preco = preco;
        this.categoria = categoria;
        setSetor(setor);
    }

    public Produto() {
        this.id = 0;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        if (LOJA.equals(setor) || LANCHONETE.equals(setor)) {
            this.setor = setor;
        }
    }
    
    public boolean isAtivo() {
    return ativo;
}

public void setAtivo(boolean ativo) {
    this.ativo = ativo;
}

    @Override
    public String toString() {
        return "Produto{id=" + id
                + ", descricao=" + descricao
                + ", nomeProduto=" + nomeProduto
                + ", preco=" + preco
                + ", categoria=" + categoria
                + ", setor=" + setor + '}';
    }
}
