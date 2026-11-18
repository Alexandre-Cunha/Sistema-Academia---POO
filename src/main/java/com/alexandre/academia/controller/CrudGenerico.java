package com.alexandre.academia.controller;

import com.alexandre.academia.model.Identificavel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

/**
 * Controlador genérico que implementa as operações básicas de CRUD
 * (Create, Read, Update e Delete) para qualquer entidade que implemente
 * a interface {@link Identificavel}.
 *
 * <p>A classe mantém uma lista de elementos em memória, controla os
 * identificadores (IDs) já utilizados para evitar duplicidade e delega a
 * persistência dos dados a uma função fornecida no momento da criação,
 * o que a mantém independente da forma de armazenamento (arquivo, banco
 * de dados etc.).</p>
 *
 * <p>Exemplo de uso:</p>
 * <pre>{@code
 * CrudGenerico<Aluno> crudAlunos = new CrudGenerico<>(
 *         alunos,
 *         lista -> arquivoAlunos.salvar(lista)
 * );
 *
 * crudAlunos.cadastrar(new Aluno("Maria"));
 * Aluno aluno = crudAlunos.buscarPorId(1);
 * }</pre>
 *
 * @param <T> tipo da entidade gerenciada; deve implementar
 *            {@link Identificavel}, ou seja, possuir ID com
 *            {@code getId()} e {@code setId(int)}
 *
 * @author Alexandre
 * @see Identificavel
 */
public class CrudGenerico<T extends Identificavel> {

    /** Lista de elementos gerenciados por este controlador. */
    private ArrayList<T> elementos;

    /** Conjunto de IDs já utilizados, para garantir a unicidade. */
    private HashSet<Integer> idsUsados;

    /** Função responsável por persistir a lista de elementos. */
    private Consumer<ArrayList<T>> salvarDados;

    /**
     * Cria um controlador CRUD a partir de uma lista de elementos já
     * existente (por exemplo, carregada de um arquivo) e de uma função
     * de persistência.
     *
     * <p>Os IDs dos elementos recebidos são registrados como já
     * utilizados. A lista informada <b>não é copiada</b>: o controlador
     * passa a operar diretamente sobre ela.</p>
     *
     * @param elementos   lista inicial de elementos; não deve ser
     *                    {@code null}
     * @param salvarDados função chamada sempre que os dados precisarem
     *                    ser persistidos, recebendo a lista atualizada;
     *                    não deve ser {@code null}
     */
    public CrudGenerico(ArrayList<T> elementos,
                        Consumer<ArrayList<T>> salvarDados) {

        this.elementos = elementos;
        this.salvarDados = salvarDados;
        this.idsUsados = new HashSet<>();

        // Recupera os IDs já utilizados.
        for (T elemento : elementos) {
            idsUsados.add(elemento.getId());
        }
    }

    /**
     * Gera o menor ID positivo ainda não utilizado.
     *
     * <p>O método apenas calcula o valor; ele <b>não reserva</b> o ID.
     * O ID só passa a ser considerado usado quando um elemento com ele é
     * cadastrado por meio de {@link #cadastrar(Identificavel)}.</p>
     *
     * @return um ID inteiro maior ou igual a 1 que não está em uso
     */
    public int gerarId() {

        int id = 1;

        while (idsUsados.contains(id)) {
            id++;
        }

        return id;
    }

    /**
     * <b>CREATE</b> - Cadastra um novo elemento e persiste os dados.
     *
     * <p>Se o elemento possuir ID menor ou igual a zero, um ID é
     * atribuído automaticamente com {@link #gerarId()}. Caso o ID já
     * esteja em uso, o cadastro é rejeitado.</p>
     *
     * @param elemento elemento a ser cadastrado
     * @return {@code true} se o elemento foi cadastrado com sucesso;
     *         {@code false} se o elemento for {@code null} ou se o seu
     *         ID já estiver em uso
     */
    public boolean cadastrar(T elemento) {

        if (elemento == null) {
            return false;
        }

        // Atribui um ID automaticamente, se necessário.
        if (elemento.getId() <= 0) {
            elemento.setId(gerarId());
        }

        if (idsUsados.contains(elemento.getId())) {
            return false;
        }

        elementos.add(elemento);
        idsUsados.add(elemento.getId());

        salvar();
        return true;
    }

    /**
     * <b>READ</b> - Busca um elemento pelo seu ID.
     *
     * @param id identificador do elemento procurado
     * @return o elemento com o ID informado, ou {@code null} se nenhum
     *         elemento for encontrado
     */
    public T buscarPorId(int id) {

        for (T elemento : elementos) {
            if (elemento.getId() == id) {
                return elemento;
            }
        }

        return null;
    }

    /**
     * <b>READ</b> - Lista todos os elementos cadastrados.
     *
     * <p>Retorna uma <b>cópia</b> da lista, de modo que adicionar ou
     * remover itens da lista retornada não afeta o controlador. Os
     * objetos contidos, porém, são os mesmos (cópia rasa): alterá-los
     * modifica os elementos originais.</p>
     *
     * @return nova lista contendo todos os elementos; vazia se não
     *         houver nenhum
     */
    public ArrayList<T> listar() {
        return new ArrayList<>(elementos);
    }

    /**
     * <b>UPDATE</b> - Persiste as alterações feitas nos elementos.
     *
     * <p>Como {@link #buscarPorId(int)} e {@link #listar()} devolvem
     * referências aos objetos originais, uma alteração feita em um
     * elemento já se reflete na memória. Este método deve ser chamado em
     * seguida para gravar essas alterações na persistência.</p>
     */
    public void salvarAlteracoes() {
        salvar();
    }

    /**
     * <b>DELETE</b> - Remove o elemento com o ID informado e persiste os
     * dados.
     *
     * <p>O ID removido continua registrado como utilizado, portanto não
     * será reaproveitado por {@link #gerarId()} durante a execução do
     * programa.</p>
     *
     * @param id identificador do elemento a ser removido
     * @return {@code true} se algum elemento foi removido;
     *         {@code false} se nenhum elemento possuía o ID informado
     */
    public boolean remover(int id) {

        boolean removido = elementos.removeIf(
            elemento -> elemento.getId() == id
        );

        if (removido) {
            salvar();
        }

        return removido;
    }

    /**
     * Persiste os dados chamando a função de salvamento informada no
     * construtor, passando a lista atual de elementos.
     */
    private void salvar() {
        salvarDados.accept(elementos);
    }
}