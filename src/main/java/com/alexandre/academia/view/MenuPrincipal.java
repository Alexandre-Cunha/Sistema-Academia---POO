package com.alexandre.academia.view;

import com.alexandre.academia.controller.GerenciarFuncionarios;
import com.alexandre.academia.model.Administrador;

import java.util.Scanner;

/**
 * Exibe o menu principal do sistema da Academia Milho Verde.
 *
 * @author Alexandre
 */
public class MenuPrincipal {

    private final Scanner scanner;

    /** Gerenciador responsável pela autenticação dos funcionários. */
    private final GerenciarFuncionarios gerenciadorFuncionarios;

    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
        this.gerenciadorFuncionarios = new GerenciarFuncionarios();
    }

    /**
     * Exibe as opções principais do sistema.
     */
    public void exibir() {

        int opcao;

        do {
            System.out.println("\n================================");
            System.out.println("       ACADEMIA MILHO VERDE");
            System.out.println("================================");
            System.out.println("1 - Gerenciar Clientes");
            System.out.println("2 - Gerenciar Funcionários");
            System.out.println("3 - Gerenciar Instrutores");
            System.out.println("4 - Loja");
            System.out.println("5 - Lanchonete");
            System.out.println("6 - Finanças");
            System.out.println("0 - Sair");
            System.out.println("================================");
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
                    System.out.println(
                            "Módulo de Clientes em desenvolvimento."
                    );
                    break;

                case 2:
                    acessarMenuFuncionarios();
                    break;

                case 3:
                    MenuInstrutores menuInstrutores =
                            new MenuInstrutores();

                    menuInstrutores.exibir();
                    break;

                case 4:
                    MenuLoja menuLoja = new MenuLoja();
                    menuLoja.exibir();
                    break;

                case 5:
                    System.out.println(
                            "Módulo Lanchonete em desenvolvimento."
                    );
                    break;

                case 6:
                    System.out.println(
                            "Módulo Finanças em desenvolvimento."
                    );
                    break;

                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    /**
     * Solicita as credenciais e autentica um administrador.
     */
    private void acessarMenuFuncionarios() {

        System.out.println("\n===== LOGIN DO ADMINISTRADOR =====");

        System.out.print("Usuário: ");
        String user = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Administrador administrador =
                gerenciadorFuncionarios.autenticarAdministrador(user, senha);

        if (administrador != null) {

            System.out.println("\nLogin realizado com sucesso!");

            MenuFuncionario menuFuncionario =
        new MenuFuncionario(
                scanner,
                gerenciadorFuncionarios,
                administrador
        );

            menuFuncionario.exibir();

        } else {

            System.out.println(
                    "\nAcesso negado! Usuário ou senha inválidos."
            );
        }
    }
}
