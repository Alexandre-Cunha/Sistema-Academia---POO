
package com.alexandre.academia.model;

/**
 * Representa um administrador da academia.
 *
 * @author Alexandre
 */
public class Administrador extends Funcionario {

    private static String senhaAdm = "0000";

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

    public static String getSenhaAdm() {
        return senhaAdm;
    }

    public static void setSenhaAdm(String senhaAdm) {
        Administrador.senhaAdm = senhaAdm;
    }

    @Override
    public String toString() {
        return "Administrador{" + super.toString() + "}";
    }
}
