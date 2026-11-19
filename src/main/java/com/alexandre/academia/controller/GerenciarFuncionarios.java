
package com.alexandre.academia.controller;

import com.alexandre.academia.dao.FuncionarioDAO;
import com.alexandre.academia.model.Administrador;
import com.alexandre.academia.model.Funcionario;

import java.util.ArrayList;

/**
 * Gerencia o cadastro e as operações dos funcionários da academia.
 *
 * As operações de CRUD são delegadas ao CrudGenerico, enquanto
 * a persistência dos dados é realizada pelo FuncionarioDAO.
 *
 * A interação com o usuário é responsabilidade das classes de menu.
 *
 * @author Alexandre
 */
public class GerenciarFuncionarios {

    /** CRUD genérico de funcionários. */
    private CrudGenerico<Funcionario> crud;

    /** Nomes de usuário já utilizados. */
    private ArrayList<String> usuarios;

    /**
     * Constrói o gerenciador carregando os funcionários salvos.
     */
    public GerenciarFuncionarios() {

        ArrayList<Funcionario> funcionarios =
                FuncionarioDAO.importarFuncionariosDoJSON();

        crud = new CrudGenerico<>(
                funcionarios,
                FuncionarioDAO::exportarFuncionariosParaJSON
        );

        usuarios = new ArrayList<>();

        for (Funcionario f : crud.listar()) {
            usuarios.add(f.getUser());
        }
    }

    /**
     * Salva as alterações no JSON.
     */
    private void salvarDados() {
        crud.salvarAlteracoes();
    }

    /**
     * Busca um funcionário pelo nome de usuário.
     *
     * @param user nome de usuário procurado
     * @return funcionário encontrado ou null
     */
    public Funcionario obterFuncionario(String user) {

        if (user == null) {
            return null;
        }

        for (Funcionario f : crud.listar()) {
            if (user.equals(f.getUser())) {
                return f;
            }
        }

        return null;
    }

    /**
     * Autentica um administrador utilizando seu usuário e senha.
     *
     * Além de conferir as credenciais, verifica se o funcionário
     * encontrado é realmente uma instância de Administrador.
     *
     * @param user nome de usuário
     * @param senha senha informada
     * @return administrador autenticado ou null se os dados forem inválidos
     */
    public Administrador autenticarAdministrador(String user, String senha) {

        Funcionario funcionario = obterFuncionario(user);

        if (funcionario instanceof Administrador
                && senha != null
                && senha.equals(funcionario.getSenha())) {

            return (Administrador) funcionario;
        }

        return null;
    }

    /**
     * Verifica se um nome de usuário já está cadastrado.
     *
     * @param user nome de usuário
     * @return true se já estiver cadastrado
     */
    public boolean usuarioCadastrado(String user) {
        return user != null && usuarios.contains(user);
    }

    /**
     * Cadastra um funcionário.
     *
     * @param funcionario funcionário a ser cadastrado
     * @return true se o cadastro for realizado
     */
    public boolean cadastrarFuncionario(Funcionario funcionario) {

        if (funcionario == null
                || funcionario.getUser() == null
                || funcionario.getUser().isBlank()
                || funcionario.getSenha() == null
                || funcionario.getSenha().isBlank()) {

            return false;
        }

        if (usuarioCadastrado(funcionario.getUser())) {
            return false;
        }

        if (crud.cadastrar(funcionario)) {
            usuarios.add(funcionario.getUser());
            return true;
        }

        return false;
    }

    /**
     * Remove um funcionário pelo seu nome de usuário.
     *
     * @param funcionario funcionário a ser removido
     * @return true se a remoção for realizada
     */
    public boolean removerFuncionario(Funcionario funcionario) {

        if (funcionario == null || funcionario.getUser() == null) {
            return false;
        }

        Funcionario encontrado = obterFuncionario(funcionario.getUser());

        if (encontrado != null && crud.remover(encontrado.getId())) {
            usuarios.remove(encontrado.getUser());
            return true;
        }

        return false;
    }

    /**
     * Valida um novo valor para alteração.
     *
     * @param funcionario funcionário que será alterado
     * @param novoValor novo valor informado
     * @return valor sem espaços nas extremidades ou null
     */
    private String validarAlteracao(Funcionario funcionario, String novoValor) {

        if (funcionario == null || novoValor == null || novoValor.isBlank()) {
            return null;
        }

        return novoValor.trim();
    }

    /**
     * Altera o nome de usuário de um funcionário.
     *
     * @param funcionario funcionário que será alterado
     * @param novoUser novo nome de usuário
     * @return true se a alteração for realizada
     */
    public boolean alterarUser(Funcionario funcionario, String novoUser) {

        String novo = validarAlteracao(funcionario, novoUser);

        if (novo == null) {
            return false;
        }

        if (novo.equals(funcionario.getUser()) || usuarioCadastrado(novo)) {
            return false;
        }

        usuarios.remove(funcionario.getUser());

        funcionario.setUser(novo);

        usuarios.add(novo);

        salvarDados();

        return true;
    }

    /**
     * Altera a senha de um funcionário ou administrador.
     *
     * @param funcionario funcionário que terá a senha alterada
     * @param novaSenha nova senha
     * @return true se a alteração for realizada
     */
    public boolean alterarSenha(Funcionario funcionario, String novaSenha) {

        String novo = validarAlteracao(funcionario, novaSenha);

        if (novo == null) {
            return false;
        }

        funcionario.setSenha(novo);

        salvarDados();

        return true;
    }

    /**
     * Altera o nome de um funcionário.
     *
     * @param funcionario funcionário que será alterado
     * @param novoNome novo nome
     * @return true se a alteração for realizada
     */
    public boolean alterarNome(Funcionario funcionario, String novoNome) {

        String novo = validarAlteracao(funcionario, novoNome);

        if (novo == null) {
            return false;
        }

        funcionario.setNome(novo);

        salvarDados();

        return true;
    }

    /**
     * Altera o cargo de um funcionário.
     *
     * @param funcionario funcionário que será alterado
     * @param novoCargo novo cargo
     * @return true se a alteração for realizada
     */
    public boolean alterarCargo(Funcionario funcionario, String novoCargo) {

        String novo = validarAlteracao(funcionario, novoCargo);

        if (novo == null) {
            return false;
        }

        funcionario.setCargo(novo);

        salvarDados();

        return true;
    }

    /**
     * Altera o CPF de um funcionário.
     *
     * @param funcionario funcionário que será alterado
     * @param novoCpf novo CPF
     * @return true se a alteração for realizada
     */
    public boolean alterarCpf(Funcionario funcionario, String novoCpf) {

        String novo = validarAlteracao(funcionario, novoCpf);

        if (novo == null) {
            return false;
        }

        funcionario.setCpf(novo);

        salvarDados();

        return true;
    }

    /**
     * Altera o e-mail de um funcionário.
     *
     * @param funcionario funcionário que será alterado
     * @param novoEmail novo e-mail
     * @return true se a alteração for realizada
     */
    public boolean alterarEmail(Funcionario funcionario, String novoEmail) {

        String novo = validarAlteracao(funcionario, novoEmail);

        if (novo == null) {
            return false;
        }

        funcionario.setEmail(novo);

        salvarDados();

        return true;
    }

    /**
     * Altera o telefone de um funcionário.
     *
     * @param funcionario funcionário que será alterado
     * @param novoTelefone novo telefone
     * @return true se a alteração for realizada
     */
    public boolean alterarTelefone(Funcionario funcionario, String novoTelefone) {

        String novo = validarAlteracao(funcionario, novoTelefone);

        if (novo == null) {
            return false;
        }

        funcionario.setTelefone(novo);

        salvarDados();

        return true;
    }

    /**
     * Retorna a lista de funcionários cadastrados.
     *
     * @return lista de funcionários
     */
    public ArrayList<Funcionario> getFuncionarios() {
        return crud.listar();
    }

    /**
     * Substitui a lista de funcionários e atualiza o JSON.
     *
     * @param funcionarios nova lista de funcionários
     * @return true se a substituição for realizada
     */
    public boolean setFuncionarios(ArrayList<Funcionario> funcionarios) {

        if (funcionarios == null) {
            return false;
        }

        crud = new CrudGenerico<>(
                funcionarios,
                FuncionarioDAO::exportarFuncionariosParaJSON
        );

        usuarios = new ArrayList<>();

        for (Funcionario f : crud.listar()) {
            usuarios.add(f.getUser());
        }

        salvarDados();

        return true;
    }
}