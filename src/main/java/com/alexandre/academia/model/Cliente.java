
package com.alexandre.academia.model;

/**
 * Representa um cliente da academia.
 * Herda os dados pessoais da classe Pessoa.
 *
 * @author Alexandre
 */
public class Cliente extends Pessoa {

    private String endereco;
    private String cartao;

    public Cliente(String endereco, String cartao, String nome, String cpf,
                   String telefone, String email, int id) {

        super(nome, cpf, telefone, email);
        setId(id);
        this.endereco = endereco;
        this.cartao = cartao;
    }

    public Cliente() {
        super();
    }

    public Cliente(int id) {
        super();
        setId(id);
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCartao() {
        return cartao;
    }

    public void setCartao(String cartao) {
        this.cartao = cartao;
    }

    @Override
    public String toString() {
        String final4 = (cartao != null && cartao.length() >= 4)
                ? cartao.substring(cartao.length() - 4) : "----";

        return "Cliente{id=" + getId()
                + ", endereco=" + endereco
                + ", cartao=**** " + final4
                + "} " + super.toString();
    }
}
