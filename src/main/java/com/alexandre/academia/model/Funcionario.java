
package com.alexandre.academia.model;

/**
 * Representa um funcionário da academia.
 * Herda os dados pessoais da classe Pessoa.
 *
 * @author Alexandre
 */
public class Funcionario extends Pessoa {

    private String user;
    private String senha;
    private String cargo;

    public Funcionario() {
        super();
    }

    public Funcionario(String user) {
        this.user = user;
    }

    public Funcionario(String user, String senha, String cargo,
                       String nome, String cpf, String telefone, String email) {

        super(nome, cpf, telefone, email);
        this.user = user;
        this.senha = senha;
        this.cargo = cargo;
    }

    public Funcionario(String user, String senha, String cargo,
                       String nome, String cpf, String telefone,
                       String email, int id) {

        super(nome, cpf, telefone, email);
        setId(id);
        this.user = user;
        this.senha = senha;
        this.cargo = cargo;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public String toString() {
        return "Funcionario{user=" + user
                + ", cargo=" + cargo
                + ", id=" + getId()
                + "} " + super.toString();
    }
}
