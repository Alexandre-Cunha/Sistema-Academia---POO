package com.alexandre.academia.view;

import com.alexandre.academia.controller.GerenciarLoja;
import com.alexandre.academia.model.Produto;
import java.util.Scanner;

/**
 * Menu de gerenciamento da loja.
 *
 * <p>Exibe, no terminal, as opções para cadastrar, listar, buscar,
 * atualizar e inativar os produtos da loja. A classe apenas lê os dados
 * digitados e mostra os resultados: as regras de negócio e a
 * persistência ficam a cargo de {@link GerenciarLoja}.</p>
 *
 * <p>Toda leitura de número é feita linha a linha e repetida até que o
 * usuário digite um valor válido, e os erros de validação lançados pelo
 * {@link GerenciarLoja} são exibidos como mensagem, sem interromper o
 * programa.</p>
 *
 * <p>Exemplo de uso:</p>
 * <pre>{@code
 * MenuLoja menu = new MenuLoja();
 * menu.exibir();
 * }</pre>
 *
 * @author Alexandre
 * @see GerenciarLoja
 * @see Produto
 */
public class MenuLoja {

    /** Leitura de dados pelo terminal. */
    private final Scanner scanner;

    /** Gerenciador que executa as operações da loja. */
    private final GerenciarLoja gerenciarLoja;

    /**
     * Cria o menu da loja.
     *
     * <p>Inicializa a leitura pelo terminal e um novo
     * {@link GerenciarLoja}, que carrega os produtos salvos.</p>
     */
    public MenuLoja() {
        this.scanner = new Scanner(System.in);
        this.gerenciarLoja = new GerenciarLoja();
    }

    /**
     * Exibe o menu da loja e executa a opção escolhida, repetindo até o
     * usuário digitar {@code 0}.
     *
     * <p>Opções disponíveis:</p>
     * <ul>
     *   <li>{@code 1} - cadastrar produto;</li>
     *   <li>{@code 2} - listar produtos ativos;</li>
     *   <li>{@code 3} - buscar produto pelo ID;</li>
     *   <li>{@code 4} - atualizar produto;</li>
     *   <li>{@code 5} - inativar produto;</li>
     *   <li>{@code 0} - voltar ao menu principal.</li>
     * </ul>
     *
     * <p>Se o usuário digitar algo que não seja um número inteiro, a
     * leitura é repetida até que um número seja informado. Números fora
     * das opções exibem "Opção inválida!".</p>
     */
    public void exibir() {

        int opcao;

        do {
            System.out.println("\n========== LOJA ==========");
            System.out.println("1 - Cadastrar produto");
            System.out.println("2 - Listar produtos");
            System.out.println("3 - Buscar produto");
            System.out.println("4 - Atualizar produto");
            System.out.println("5 - Inativar produto");
            System.out.println("0 - Voltar");
            System.out.println("==========================");

            opcao = lerInteiro("Escolha uma opção: ",
                    "Digite uma opção válida!");

            switch (opcao) {
                case 1:
                    cadastrarProduto();
                    break;

                case 2:
                    listarProdutos();
                    break;

                case 3:
                    buscarProduto();
                    break;

                case 4:
                    atualizarProduto();
                    break;

                case 5:
                    inativarProduto();
                    break;

                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    /**
     * Lê um número inteiro pelo terminal.
     *
     * <p>Exibe a mensagem, lê uma linha e tenta convertê-la em inteiro.
     * Se o texto não for um inteiro válido, exibe a mensagem de erro e
     * repete a leitura até que seja informado um valor válido.</p>
     *
     * @param mensagem texto exibido antes da leitura
     * @param erro     texto exibido quando o valor digitado é inválido
     * @return o número inteiro digitado
     */
    private int lerInteiro(String mensagem, String erro) {

        while (true) {
            System.out.print(mensagem);
            String texto = scanner.nextLine().trim();

            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println(erro);
            }
        }
    }

    /**
     * Lê um número decimal pelo terminal.
     *
     * <p>Aceita vírgula ou ponto como separador decimal (por exemplo,
     * {@code 39,90} ou {@code 39.90}). Se o texto não for um número
     * válido, exibe uma mensagem de erro e repete a leitura até que seja
     * informado um valor válido.</p>
     *
     * @param mensagem texto exibido antes da leitura
     * @return o número decimal digitado
     */
    private double lerDecimal(String mensagem) {

        while (true) {
            System.out.print(mensagem);
            String texto = scanner.nextLine().trim().replace(',', '.');

            try {
                double valor = Double.parseDouble(texto);

                if (Double.isFinite(valor)) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // cai na mensagem de erro abaixo
            }

            System.out.println("Digite um número válido!");
        }
    }

    /**
     * Lê pelo terminal os dados de um novo produto (descrição, nome,
     * preço e categoria) e o cadastra por meio de
     * {@link GerenciarLoja#cadastrarProduto(String, String, double, String)}.
     *
     * <p>Se os dados forem inválidos (descrição ou nome vazios, ou preço
     * menor ou igual a zero), exibe o motivo e nada é cadastrado. A
     * mensagem de sucesso só aparece quando o cadastro é concluído.</p>
     */
    private void cadastrarProduto() {

        System.out.println("\n=== CADASTRAR PRODUTO ===");

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine();

        double preco = lerDecimal("Preço: ");

        System.out.print("Categoria: ");
        String categoria = scanner.nextLine();

        try {
            gerenciarLoja.cadastrarProduto(
                    descricao, nome, preco, categoria
            );

            System.out.println("Produto cadastrado com sucesso!");

        } catch (IllegalArgumentException e) {
            System.out.println("Não foi possível cadastrar: " + e.getMessage());
        }
    }

    /**
     * Exibe no terminal todos os produtos ativos, um por linha, usando o
     * {@code toString()} de {@link Produto}.
     *
     * @see GerenciarLoja#listarProdutosAtivos()
     */
    private void listarProdutos() {

        System.out.println("\n=== PRODUTOS ATIVOS ===");

        for (Produto produto : gerenciarLoja.listarProdutosAtivos()) {
            System.out.println(produto);
        }
    }

    /**
     * Pede o ID de um produto e exibe seus dados.
     *
     * <p>Se não houver produto com esse ID, exibe "Produto não
     * encontrado.". Produtos inativos também são exibidos.</p>
     *
     * @see GerenciarLoja#buscarProduto(int)
     */
    private void buscarProduto() {

        System.out.println();
        int id = lerInteiro("Informe o ID do produto: ", "Digite um ID válido!");

        Produto produto = gerenciarLoja.buscarProduto(id);

        if (produto != null) {
            System.out.println(produto);
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    /**
     * Pede o ID de um produto e os novos dados (descrição, nome, preço
     * e categoria) e atualiza o produto.
     *
     * <p>Exibe uma mensagem de sucesso se o produto foi atualizado, ou
     * "Não foi possível atualizar o produto." se ele não existir ou
     * estiver inativo. Se os novos dados forem inválidos, exibe o
     * motivo e o produto não é alterado.</p>
     *
     * @see GerenciarLoja#atualizarProduto(int, String, String, double, String)
     */
    private void atualizarProduto() {

        System.out.println();
        int id = lerInteiro("Informe o ID do produto: ", "Digite um ID válido!");

        System.out.print("Nova descrição: ");
        String descricao = scanner.nextLine();

        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();

        double preco = lerDecimal("Novo preço: ");

        System.out.print("Nova categoria: ");
        String categoria = scanner.nextLine();

        try {
            boolean atualizado = gerenciarLoja.atualizarProduto(
                    id, descricao, nome, preco, categoria
            );

            System.out.println(atualizado ? "Produto atualizado com sucesso!" : "Não foi possível atualizar o produto.");

        } catch (IllegalArgumentException e) {
            System.out.println("Não foi possível atualizar: " + e.getMessage());
        }
    }

    /**
     * Pede o ID de um produto e o inativa, sem removê-lo do cadastro.
     *
     * <p>Exibe uma mensagem de sucesso se o produto foi inativado, ou
     * "Produto não encontrado ou já inativo." caso contrário.</p>
     *
     * @see GerenciarLoja#inativarProduto(int)
     */
    private void inativarProduto() {

        System.out.println();
        int id = lerInteiro("Informe o ID do produto: ",
                "Digite um ID válido!");

        boolean inativado = gerenciarLoja.inativarProduto(id);

        System.out.println(inativado ?  "Produto inativado com sucesso!" : "Produto não encontrado ou já inativo.");
    }
}