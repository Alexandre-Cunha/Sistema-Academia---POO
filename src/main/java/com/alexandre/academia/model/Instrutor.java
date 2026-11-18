
package com.alexandre.academia.model;

/**
 * Representa um instrutor da academia.
 *
 * @author Alexandre
 */
public class Instrutor extends Pessoa {

    private String especialidade;

    public Instrutor() {
        super();
    }

    public Instrutor(String especialidade, String nome, String cpf,
                     String telefone, String email) {

        super(nome, cpf, telefone, email);
        this.especialidade = especialidade;
    }

    public Instrutor(String especialidade, String nome, String cpf,
                     String telefone, String email, int id) {

        super(nome, cpf, telefone, email);
        setId(id);
        this.especialidade = especialidade;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public String toString() {
        return "Instrutor{especialidade=" + especialidade
                + ", id=" + getId() + "} " + super.toString();
    }
}
