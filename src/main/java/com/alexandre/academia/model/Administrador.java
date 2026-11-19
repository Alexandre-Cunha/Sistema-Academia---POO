package com.alexandre.academia.model;

/**
 * Representa um administrador da academia.
 *
 * Herda de Funcionario os atributos e métodos necessários
 * para identificação e autenticação (usuário e senha).
 *
 * @author Alexandre
 */
public class Administrador extends Funcionario {

    public Administrador() {
        super();
    }

    public Administrador(String user, String senha, String cargo,
                         String nome, String cpf, String telefone,
                         String email) {

        super(user, senha, cargo, nome, cpf, telefone, email);
    }

    public Administrador(String user, String senha, String cargo,
                         String nome, String cpf, String telefone,
                         String email, int id) {

        super(user, senha, cargo, nome, cpf, telefone, email, id);
    }

    @Override
    public String toString() {
        return "Administrador{" + super.toString() + "}";
    }
}
