package com.alexandre.academia.controller;

import com.alexandre.academia.model.Instrutor;
import com.alexandre.academia.dao.InstrutorDAO;
import java.util.ArrayList;

/**
 * Gerencia o cadastro de instrutores da academia.
 *
 * <p>Cada instrutor é identificado pelo seu CPF, que deve ser único no
 * sistema. As operações básicas de CRUD (cadastro, busca, listagem e
 * remoção) são delegadas a um {@link CrudGenerico}, enquanto a
 * persistência dos dados em JSON é feita por meio de
 * {@link InstrutorDAO}.</p>
 *
 * <p>Os métodos desta classe exibem mensagens de retorno ao usuário no
 * console ({@code System.out}) para informar o resultado de cada
 * operação.</p>
 *
 * <p>Exemplo de uso:</p>
 * <pre>{@code
 * GerenciarInstrutores gerenciador = new GerenciarInstrutores();
 *
 * gerenciador.cadastrarInstrutor(instrutor);
 * Instrutor encontrado = gerenciador.obterInstrutor("123.456.789-00");
 * gerenciador.exibirInstrutores();
 * }</pre>
 *
 * @author Alexandre
 * @see CrudGenerico
 * @see Instrutor
 * @see InstrutorDAO
 */
public class GerenciarInstrutores {

    /** CRUD genérico responsável pelas operações comuns. */
    private CrudGenerico<Instrutor> crud;

    /**
     * Constrói o gerenciador carregando os instrutores salvos.
     *
     * <p>Os instrutores são lidos do arquivo JSON por meio de
     * {@link InstrutorDAO#importarInstrutoresDoJSON()}. Toda alteração
     * feita posteriormente é gravada automaticamente com
     * {@link InstrutorDAO#exportarInstrutoresParaJSON(ArrayList)}.</p>
     */
    public GerenciarInstrutores() {

        ArrayList<Instrutor> instrutores = InstrutorDAO.importarInstrutoresDoJSON();

        crud = new CrudGenerico<>(instrutores,InstrutorDAO::exportarInstrutoresParaJSON);
    }

    /**
     * Cadastra um instrutor, desde que o CPF não esteja cadastrado.
     *
     * <p>O cadastro é recusado, com uma mensagem no console, quando o
     * instrutor é {@code null}, quando o CPF é nulo ou em branco, quando
     * já existe um instrutor com o mesmo CPF ou quando o
     * {@link CrudGenerico} não consegue cadastrá-lo. Em caso de sucesso,
     * o ID gerado é exibido.</p>
     *
     * @param instrutor instrutor a ser cadastrado
     */
    public void cadastrarInstrutor(Instrutor instrutor) {

        if (instrutor == null || instrutor.getCpf() == null || instrutor.getCpf().isBlank()) {
            System.out.println("Instrutor inválido: o CPF é obrigatório.");
            return;
        }

        if (obterInstrutor(instrutor.getCpf()) != null) {
            System.out.println("Já existe um instrutor cadastrado com esse CPF.");
            return;
        }

        if (crud.cadastrar(instrutor)) {
            System.out.println("Instrutor cadastrado com sucesso. ID: " + instrutor.getId());
        } else {
            System.out.println("Não foi possível cadastrar o instrutor.");
        }
    }

    /**
     * Remove um instrutor utilizando seu CPF.
     *
     * <p>Apenas o CPF do instrutor informado é considerado: o instrutor
     * cadastrado com esse CPF é localizado e removido. Se o parâmetro ou
     * o seu CPF forem {@code null}, ou se nenhum instrutor com esse CPF
     * existir, uma mensagem de erro é exibida no console.</p>
     *
     * @param instrutor instrutor a ser removido
     */
    public void removerInstrutor(Instrutor instrutor) {

        if (instrutor == null || instrutor.getCpf() == null) {
            System.out.println("Instrutor inválido.");
            return;
        }

        Instrutor encontrado = obterInstrutor(instrutor.getCpf());

        if (encontrado != null && crud.remover(encontrado.getId())) {
            System.out.println("Instrutor removido com sucesso.");
        } else {
            System.out.println("Instrutor não encontrado.");
        }
    }

    /**
     * Busca um instrutor pelo CPF.
     *
     * <p>A comparação é feita com {@code equals}, portanto o CPF deve
     * ser informado exatamente no mesmo formato em que foi cadastrado
     * (com ou sem pontuação, por exemplo).</p>
     *
     * @param cpf CPF do instrutor procurado
     * @return instrutor encontrado, ou {@code null} se o CPF for
     *         {@code null} ou se não houver instrutor com esse CPF
     */
    public Instrutor obterInstrutor(String cpf) {

        if (cpf == null) {
            return null;
        }

        for (Instrutor instrutor : crud.listar()) {
            if (cpf.equals(instrutor.getCpf())) {
                return instrutor;
            }
        }

        return null;
    }

    /**
     * Exibe todos os instrutores cadastrados no console.
     *
     * <p>Cada instrutor é impresso por meio de seu método
     * {@code toString()}. Se não houver nenhum instrutor, uma mensagem
     * informando isso é exibida.</p>
     */
    public void exibirInstrutores() {
        ArrayList<Instrutor> instrutores = crud.listar();
        if (instrutores.isEmpty()) {
            System.out.println("Nenhum instrutor cadastrado.");
            return;
        }

        for (Instrutor instrutor : instrutores) {
            System.out.println(instrutor);
        }
    }

    /**
     * Retorna a lista de instrutores.
     *
     * <p>A lista retornada é uma cópia: adicionar ou remover itens dela
     * não altera os dados do gerenciador. Os objetos {@link Instrutor}
     * contidos, no entanto, são os mesmos do cadastro.</p>
     *
     * @return lista de instrutores cadastrados; vazia se não houver
     *         nenhum
     */
    public ArrayList<Instrutor> getInstrutores() {
        return crud.listar();
    }

    /**
     * Substitui a lista de instrutores e atualiza o JSON.
     *
     * <p>Cria um novo {@link CrudGenerico} a partir da lista recebida
     * (que passa a ser usada diretamente, sem cópia) e grava os dados
     * imediatamente no arquivo JSON. Se a lista for {@code null}, nada
     * é alterado e uma mensagem de erro é exibida.</p>
     *
     * @param instrutores nova lista de instrutores
     */
    public void setInstrutores(ArrayList<Instrutor> instrutores) {

        if (instrutores == null) {
            System.out.println("Lista de instrutores inválida.");
            return;
        }

        crud = new CrudGenerico<>(instrutores, InstrutorDAO::exportarInstrutoresParaJSON);

        crud.salvarAlteracoes();
    }
}