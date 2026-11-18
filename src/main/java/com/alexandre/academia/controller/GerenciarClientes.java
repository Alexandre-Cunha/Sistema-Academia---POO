
package com.alexandre.academia.controller;

import com.alexandre.academia.dao.ClienteDAO;
import com.alexandre.academia.model.Cliente;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Gerencia o cadastro de clientes da academia.
 *
 * <p>Cada cliente é identificado por um ID único, gerado
 * automaticamente no cadastro. O CPF também não pode se repetir entre
 * os clientes. As operações básicas de CRUD são delegadas a um
 * {@link CrudGenerico}, enquanto a persistência dos dados em JSON é
 * feita por meio de {@link ClienteDAO}.</p>
 *
 * <p>Além do cadastro, da busca e da remoção, a classe oferece métodos
 * interativos para alterar os dados de um cliente (nome, CPF,
 * telefone, e-mail, endereço e cartão), que leem o novo valor pelo
 * terminal.</p>
 *
 * <p>Os métodos desta classe exibem mensagens de retorno ao usuário no
 * console ({@code System.out}) para informar o resultado de cada
 * operação.</p>
 *
 * <p>Exemplo de uso:</p>
 * <pre>{@code
 * GerenciarClientes gerenciador = new GerenciarClientes();
 *
 * gerenciador.cadastrarCliente("Rua A, 10", "1234", "Maria",
 *         "123.456.789-00", "(38) 99999-0000", "maria@email.com");
 * Cliente c = gerenciador.obterCliente(1);
 * gerenciador.alterarTelefone(c);
 * }</pre>
 *
 * @author Alexandre
 * @see CrudGenerico
 * @see Cliente
 * @see ClienteDAO
 */
public class GerenciarClientes {

    /** CRUD genérico de clientes. */
    private CrudGenerico<Cliente> crud;

    /** Leitura de dados pelo terminal. */
    private Scanner scanner;

    /** Contador estático de clientes. */
    private static int numClientes1;

    /** Contador estático protegido para subclasses. */
    protected static int numClientes2;

    /**
     * Constrói o gerenciador carregando os clientes do JSON.
     *
     * <p>Os clientes são lidos por meio de
     * {@link ClienteDAO#importarClientesDoJSON()} e os contadores
     * estáticos de clientes são inicializados com a quantidade de
     * clientes carregados. Toda alteração feita posteriormente é
     * gravada com {@link ClienteDAO#exportarClientesParaJSON(ArrayList)}.</p>
     */
    public GerenciarClientes() {
        ArrayList<Cliente> clientes = ClienteDAO.importarClientesDoJSON();

        crud = new CrudGenerico<>(clientes, ClienteDAO::exportarClientesParaJSON);

        scanner = new Scanner(System.in);

        numClientes1 = crud.listar().size();
        numClientes2 = crud.listar().size();
    }

    /**
     * Salva as alterações no JSON.
     *
     * <p>Chama {@link CrudGenerico#salvarAlteracoes()}, que grava a
     * lista atual de clientes por meio do {@link ClienteDAO}.</p>
     */
    private void salvarDados() {
        crud.salvarAlteracoes();
    }

    /**
     * Cadastra um cliente utilizando os dados informados.
     *
     * <p>Cria um {@link Cliente} com ID igual a {@code 0}, o que faz o
     * {@link CrudGenerico} gerar um ID automaticamente, e delega o
     * cadastro a {@link #cadastrarCliente(Cliente)}.</p>
     *
     * @param endereco endereço do cliente
     * @param cartao   cartão de crédito do cliente
     * @param nome     nome do cliente
     * @param cpf      CPF do cliente; é obrigatório e não pode se
     *                 repetir
     * @param telefone telefone do cliente
     * @param email    e-mail do cliente
     */
    public void cadastrarCliente(String endereco, String cartao, String nome, String cpf, String telefone, String email) {
        Cliente cliente = new Cliente(endereco, cartao, nome, cpf, telefone, email, 0);
        cadastrarCliente(cliente);
    }

    /**
     * Cadastra um cliente.
     *
     * <p>O cadastro é recusado, com uma mensagem no console, quando o
     * cliente é {@code null}, quando o CPF é nulo ou em branco, quando
     * já existe outro cliente com o mesmo CPF ou quando o
     * {@link CrudGenerico} não consegue cadastrá-lo. Em caso de sucesso,
     * os contadores de clientes são incrementados e o ID gerado é
     * exibido.</p>
     *
     * @param cliente cliente a ser cadastrado
     */
    public void cadastrarCliente(Cliente cliente) {
        if (cliente == null || cliente.getCpf() == null || cliente.getCpf().isBlank()) {
            System.out.println("Cliente inválido: o CPF é obrigatório.");
            return;
        }

        if (cpfCadastrado(cliente.getCpf(), null)) {
            System.out.println("Já existe um cliente cadastrado com esse CPF.");
            return;
        }

        if (crud.cadastrar(cliente)) {
            numClientes1++;
            numClientes2++;
            System.out.println("Cliente cadastrado com sucesso. ID: " + cliente.getId());
        } else {
            System.out.println("Não foi possível cadastrar o cliente.");
        }
    }

    /**
     * Remove um cliente pelo ID.
     *
     * <p>Apenas o ID do cliente informado é considerado: o cliente
     * cadastrado com esse ID é localizado e removido, e os contadores de
     * clientes são decrementados. Se o parâmetro for {@code null} ou se
     * nenhum cliente com esse ID existir, uma mensagem de erro é
     * exibida no console.</p>
     *
     * @param cliente cliente a ser removido
     */
    public void removerCliente(Cliente cliente) {

        if (cliente == null) {
            System.out.println("Cliente inválido.");
            return;
        }

        Cliente encontrado = obterCliente(cliente.getId());

        if (encontrado != null && crud.remover(encontrado.getId())) {
            numClientes1--;
            numClientes2--;
            System.out.println("Cliente removido com sucesso.");
        } else {
            System.out.println("Cliente não encontrado.");
        }
    }

    /**
     * Busca um cliente pelo ID.
     *
     * @param id identificador do cliente procurado
     * @return cliente encontrado, ou {@code null} se nenhum cliente
     *         possuir esse ID
     */
    public Cliente obterCliente(int id) {
        return crud.buscarPorId(id);
    }

    /**
     * Busca um cliente pelo ID.
     *
     * <p>Equivale a {@link #obterCliente(int)}.</p>
     *
     * @param id identificador do cliente procurado
     * @return cliente encontrado, ou {@code null} se nenhum cliente
     *         possuir esse ID
     * @see #obterCliente(int)
     */
    public Cliente findCliente(int id) {
        return obterCliente(id);
    }

    /**
     * Verifica se outro cliente já possui o CPF informado.
     *
     * <p>A comparação é feita com {@code equals}, portanto o CPF deve
     * estar no mesmo formato do cadastrado (com ou sem pontuação). O
     * cliente passado em {@code ignorar} é desconsiderado, o que permite
     * que ele mantenha o próprio CPF ao ser alterado; a exclusão é feita
     * por identidade de objeto ({@code !=}).</p>
     *
     * @param cpf     CPF a ser verificado; não deve ser {@code null}
     * @param ignorar cliente a ser desconsiderado na verificação, ou
     *                {@code null} para considerar todos
     * @return {@code true} se algum outro cliente já possuir o CPF;
     *         {@code false} caso contrário
     */
    private boolean cpfCadastrado(String cpf, Cliente ignorar) {
        for (Cliente c : crud.listar()) {
            if (c != ignorar && cpf.equals(c.getCpf())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Lê um novo valor de texto pelo terminal.
     *
     * <p>Exibe o prompt "Novo &lt;rótulo&gt;: " e lê uma linha, removendo
     * os espaços do início e do fim. Se não houver mais entrada
     * disponível, ou se o texto digitado ficar vazio, nada é
     * alterado: no segundo caso, uma mensagem é exibida.</p>
     *
     * @param rotulo nome do campo a ser alterado, usado no prompt
     *               (por exemplo, "nome" ou "e-mail")
     * @return o texto digitado, sem espaços nas extremidades, ou
     *         {@code null} se não houver entrada ou se o valor for vazio
     */
    private String lerTexto(String rotulo) {

        System.out.print("Novo " + rotulo + ": ");

        if (!scanner.hasNextLine()) {
            return null;
        }

        String texto = scanner.nextLine().trim();

        if (texto.isEmpty()) {
            System.out.println("Valor vazio. Nada foi alterado.");
            return null;
        }

        return texto;
    }

    /**
     * Altera o nome de um cliente.
     *
     * <p>O novo nome é lido pelo terminal. Se o cliente for
     * {@code null} ou a entrada for vazia, nada é alterado. Em caso de
     * sucesso, os dados são gravados no JSON.</p>
     *
     * @param cliente cliente que terá o nome alterado
     */
    public void alterarNome(Cliente cliente) {
        if (cliente == null) return;

        String novo = lerTexto("nome");

        if (novo != null) {
            cliente.setNome(novo);
            salvarDados();
            System.out.println("Nome alterado com sucesso.");
        }
    }

    /**
     * Altera o CPF de um cliente.
     *
     * <p>O novo CPF é lido pelo terminal. A alteração não é feita se o
     * cliente for {@code null}, se a entrada for vazia ou se outro
     * cliente já possuir esse CPF. Em caso de sucesso, os dados são
     * gravados no JSON. O formato do CPF não é validado.</p>
     *
     * @param cliente cliente que terá o CPF alterado
     */
    public void alterarCpf(Cliente cliente) {
        if (cliente == null) return;

        String novo = lerTexto("CPF");

        if (novo == null) return;

        if (cpfCadastrado(novo, cliente)) {
            System.out.println( "Já existe um cliente cadastrado com esse CPF.");
            return;
        }

        cliente.setCpf(novo);
        salvarDados();
        System.out.println("CPF alterado com sucesso.");
    }

    /**
     * Altera o telefone de um cliente.
     *
     * <p>O novo telefone é lido pelo terminal. Se o cliente for
     * {@code null} ou a entrada for vazia, nada é alterado. Em caso de
     * sucesso, os dados são gravados no JSON. O formato do telefone não
     * é validado.</p>
     *
     * @param cliente cliente que terá o telefone alterado
     */
    public void alterarTelefone(Cliente cliente) {

        if (cliente == null) return;

        String novo = lerTexto("telefone");

        if (novo != null) {
            cliente.setTelefone(novo);
            salvarDados();
            System.out.println("Telefone alterado com sucesso.");
        }
    }

    /**
     * Altera o e-mail de um cliente.
     *
     * <p>O novo e-mail é lido pelo terminal. Se o cliente for
     * {@code null} ou a entrada for vazia, nada é alterado. Em caso de
     * sucesso, os dados são gravados no JSON. O formato do e-mail não é
     * validado.</p>
     *
     * @param cliente cliente que terá o e-mail alterado
     */
    public void alterarEmail(Cliente cliente) {

        if (cliente == null) return;

        String novo = lerTexto("e-mail");

        if (novo != null) {
            cliente.setEmail(novo);
            salvarDados();
            System.out.println("E-mail alterado com sucesso.");
        }
    }

    /**
     * Altera o endereço de um cliente.
     *
     * <p>O novo endereço é lido pelo terminal. Se o cliente for
     * {@code null} ou a entrada for vazia, nada é alterado. Em caso de
     * sucesso, os dados são gravados no JSON.</p>
     *
     * @param cliente cliente que terá o endereço alterado
     */
    public void alterarEndereco(Cliente cliente) {

        if (cliente == null) return;

        String novo = lerTexto("endereço");

        if (novo != null) {
            cliente.setEndereco(novo);
            salvarDados();
            System.out.println("Endereço alterado com sucesso.");
        }
    }

    /**
     * Altera o cartão de crédito de um cliente.
     *
     * <p>O novo cartão é lido pelo terminal. Se o cliente for
     * {@code null} ou a entrada for vazia, nada é alterado. Em caso de
     * sucesso, os dados são gravados no JSON. O formato do cartão não é
     * validado.</p>
     *
     * @param cliente cliente que terá o cartão alterado
     */
    public void alterarCartao(Cliente cliente) {

        if (cliente == null) return;

        String novo = lerTexto("cartão");

        if (novo != null) {
            cliente.setCartao(novo);
            salvarDados();
            System.out.println("Cartão alterado com sucesso.");
        }
    }

    /**
     * Exibe todos os clientes cadastrados no console.
     *
     * <p>Cada cliente é impresso por meio de seu método
     * {@code toString()}. Se não houver nenhum cliente, uma mensagem
     * informando isso é exibida.</p>
     */
    public void exibirClientes() {
        ArrayList<Cliente> clientes = crud.listar();

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        for (Cliente c : clientes) {
            System.out.println(c);
        }
    }

    /**
     * Retorna a quantidade de clientes cadastrados.
     *
     * <p>O valor é mantido em um contador estático, inicializado no
     * construtor com o total de clientes carregados e atualizado a cada
     * cadastro e remoção realizados por este gerenciador.</p>
     *
     * @return número de clientes cadastrados
     */
    public static int getNumClientes1() {
        return numClientes1;
    }

    /**
     * Retorna uma cópia da lista de clientes.
     *
     * <p>Adicionar ou remover itens da lista retornada não altera os
     * dados do gerenciador. Os objetos {@link Cliente} contidos, no
     * entanto, são os mesmos do cadastro.</p>
     *
     * @return cópia da lista de clientes; vazia se não houver nenhum
     */
    public ArrayList<Cliente> getClientes() {
        return crud.listar();
    }
}
