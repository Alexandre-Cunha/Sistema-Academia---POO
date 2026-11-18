
package com.alexandre.academia.view;

import com.alexandre.academia.controller.GerenciarInstrutores;
import com.alexandre.academia.model.Instrutor;
import java.util.Scanner;

/**
 * Menu de gerenciamento de instrutores.
 *
 * @author Alexandre
 */
public class MenuInstrutores {

    private final Scanner scanner;
    private final GerenciarInstrutores gerenciarInstrutores;

    public MenuInstrutores() {
        this.scanner = new Scanner(System.in);
        this.gerenciarInstrutores = new GerenciarInstrutores();
    }

    public void exibir() {

        int opcao;

        do {
            System.out.println("\n======= INSTRUTORES =======");
            System.out.println("1 - Cadastrar instrutor");
            System.out.println("2 - Listar instrutores");
            System.out.println("3 - Buscar instrutor");
            System.out.println("4 - Remover instrutor");
            System.out.println("0 - Voltar");
            System.out.println("===========================");
            System.out.print("Escolha uma opção: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Digite uma opção válida!");
                scanner.next();
                System.out.print("Escolha uma opção: ");
            }

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {

                case 1:
                    cadastrarInstrutor();
                    break;

                case 2:
                    listarInstrutores();
                    break;

                case 3:
                    buscarInstrutor();
                    break;

                case 4:
                    removerInstrutor();
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
     * Solicita os dados e cadastra um instrutor.
     */
    private void cadastrarInstrutor() {

        System.out.println("\n=== CADASTRAR INSTRUTOR ===");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        System.out.print("Especialidade: ");
        String especialidade = scanner.nextLine();

        Instrutor instrutor = new Instrutor(
                especialidade,
                nome,
                cpf,
                telefone,
                email
        );

        gerenciarInstrutores.cadastrarInstrutor(instrutor);
    }

    /**
     * Exibe todos os instrutores cadastrados.
     */
    private void listarInstrutores() {

        System.out.println("\n=== INSTRUTORES CADASTRADOS ===");

        gerenciarInstrutores.exibirInstrutores();
    }

    /**
     * Busca um instrutor pelo CPF.
     */
    private void buscarInstrutor() {

        System.out.println("\n=== BUSCAR INSTRUTOR ===");

        System.out.print("Informe o CPF: ");
        String cpf = scanner.nextLine();

        Instrutor instrutor =
                gerenciarInstrutores.obterInstrutor(cpf);

        if (instrutor != null) {
            System.out.println("\nInstrutor encontrado:");
            System.out.println(instrutor);
        } else {
            System.out.println("Instrutor não encontrado.");
        }
    }

    /**
     * Remove um instrutor pelo CPF.
     */
    private void removerInstrutor() {

        System.out.println("\n=== REMOVER INSTRUTOR ===");

        System.out.print("Informe o CPF: ");
        String cpf = scanner.nextLine();

        Instrutor instrutor =
                gerenciarInstrutores.obterInstrutor(cpf);

        if (instrutor != null) {
            gerenciarInstrutores.removerInstrutor(instrutor);
        } else {
            System.out.println("Instrutor não encontrado.");
        }
    }
}
