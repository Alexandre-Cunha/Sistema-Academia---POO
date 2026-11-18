package com.alexandre.academia.view;

import com.alexandre.academia.view.MenuLoja;
import com.alexandre.academia.view.MenuInstrutores;
import java.util.Scanner;

/**
 * Exibe o menu principal do sistema da Academia Milho Verde.
 *
 * @author Alexandre
 */
public class MenuPrincipal {

    private final Scanner scanner;

    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
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

            switch (opcao) {

                case 1:
                    System.out.println("Módulo de Clientes em desenvolvimento.");
                    break;

                case 2:
                    System.out.println("Módulo de Funcionários em desenvolvimento.");
                    break;

                case 3:
                    MenuInstrutores menuInstrutores = new MenuInstrutores();
                    menuInstrutores.exibir();
                    break;

                case 4:
                    MenuLoja menuLoja = new MenuLoja();
                    menuLoja.exibir();
                    break;

                case 5:
                    System.out.println("Módulo Lanchonete em desenvolvimento.");
                    break;

                case 6:
                    System.out.println("Módulo Finanças em desenvolvimento.");
                    break;

                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }
}
