package com.alexandre.academia.view;

import com.alexandre.academia.controller.GerenciarFuncionarios;
import com.alexandre.academia.model.Administrador;
import com.alexandre.academia.model.Funcionario;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Menu responsável pelo gerenciamento de funcionários.
 *
 * @author Alexandre
 */
public class MenuFuncionario {

    private final Scanner scanner;
    private final GerenciarFuncionarios gerenciador;
    private final Administrador administrador;

    /**
     * Constrói o menu utilizando o administrador autenticado.
     *
     * @param scanner scanner compartilhado pelo sistema
     * @param gerenciador gerenciador de funcionários
     * @param administrador administrador autenticado
     */
    public MenuFuncionario(Scanner scanner,
                           GerenciarFuncionarios gerenciador,
                           Administrador administrador) {

        this.scanner = scanner;
        this.gerenciador = gerenciador;
        this.administrador = administrador;
    }

    /**
     * Exibe as opções de gerenciamento.
     */
    public void exibir() {

        int opcao;

        do {
            System.out.println("\n================================");
            System.out.println("     GERENCIAR FUNCIONÁRIOS");
            System.out.println("================================");
            System.out.println("Administrador: " + administrador.getNome());
            System.out.println("--------------------------------");
            System.out.println("1 - Cadastrar funcionário");
            System.out.println("2 - Listar funcionários");
            System.out.println("3 - Buscar funcionário");
            System.out.println("4 - Alterar dados");
            System.out.println("5 - Remover funcionário");
            System.out.println("6 - Alterar minha senha");
            System.out.println("0 - Voltar");
            System.out.println("================================");

            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {

                case 1:
                    cadastrarFuncionario();
                    break;

                case 2:
                    listarFuncionarios();
                    break;

                case 3:
                    buscarFuncionario();
                    break;

                case 4:
                    menuAlterarDados();
                    break;

                case 5:
                    removerFuncionario();
                    break;

                case 6:
                    alterarMinhaSenha();
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
     * Realiza o cadastro de um funcionário ou administrador.
     */
    private void cadastrarFuncionario() {

        System.out.println("\n=== CADASTRAR FUNCIONÁRIO ===");

        int tipo = lerPerfil();

        Funcionario funcionario;

        if (tipo == 1) {
            funcionario = new Funcionario();
        } else {
            funcionario = new Administrador();
        }

        funcionario.setUser(lerTexto("Usuário: "));
        funcionario.setSenha(lerTexto("Senha: "));
        funcionario.setNome(lerTexto("Nome: "));
        funcionario.setCargo(lerTexto("Cargo: "));
        funcionario.setCpf(lerTexto("CPF: "));
        funcionario.setEmail(lerTexto("E-mail: "));
        funcionario.setTelefone(lerTexto("Telefone: "));

        if (gerenciador.cadastrarFuncionario(funcionario)) {
            System.out.println("Funcionário cadastrado com sucesso!");
            System.out.println("ID: " + funcionario.getId());
        } else {
            System.out.println("Não foi possível cadastrar o funcionário.");
            System.out.println("Verifique os dados ou se o usuário já existe.");
        }
    }

    /**
     * Lê o perfil do funcionário.
     *
     * 1 - Funcionário
     * 2 - Administrador
     */
    private int lerPerfil() {

        int perfil;

        do {
            System.out.println("1 - Funcionário");
            System.out.println("2 - Administrador");

            perfil = lerInteiro("Escolha o tipo: ");

            if (perfil != 1 && perfil != 2) {
                System.out.println("Digite 1 ou 2!");
            }

        } while (perfil != 1 && perfil != 2);

        return perfil;
    }

    /**
     * Exibe todos os funcionários cadastrados.
     */
    private void listarFuncionarios() {

        System.out.println("\n--- FUNCIONÁRIOS CADASTRADOS ---");

        ArrayList<Funcionario> funcionarios =
                gerenciador.getFuncionarios();

        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }

        for (Funcionario funcionario : funcionarios) {
            System.out.println(funcionario);
            System.out.println("--------------------------------");
        }
    }

    /**
     * Busca um funcionário pelo usuário.
     */
    private void buscarFuncionario() {

        System.out.println("\n--- BUSCAR FUNCIONÁRIO ---");

        String user = lerTexto("Digite o usuário: ");

        Funcionario funcionario =
                gerenciador.obterFuncionario(user);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
        } else {
            System.out.println(funcionario);
        }
    }

    /**
     * Exibe as opções de alteração dos dados.
     */
    private void menuAlterarDados() {

        String user = lerTexto("Digite o usuário do funcionário: ");

        Funcionario funcionario =
                gerenciador.obterFuncionario(user);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        int opcao;

        do {
            System.out.println("\n--- ALTERAR DADOS ---");
            System.out.println("Funcionário: " + funcionario.getNome());
            System.out.println("1 - Alterar usuário");
            System.out.println("2 - Alterar senha");
            System.out.println("3 - Alterar nome");
            System.out.println("4 - Alterar cargo");
            System.out.println("5 - Alterar CPF");
            System.out.println("6 - Alterar e-mail");
            System.out.println("7 - Alterar telefone");
            System.out.println("0 - Voltar");

            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {

                case 1:
                    exibirResultado(
                            gerenciador.alterarUser(
                                    funcionario,
                                    lerTexto("Novo usuário: ")
                            ),
                            "Alteração de usuário"
                    );
                    break;

                case 2:
                    exibirResultado(
                            gerenciador.alterarSenha(
                                    funcionario,
                                    lerTexto("Nova senha: ")
                            ),
                            "Alteração de senha"
                    );
                    break;

                case 3:
                    exibirResultado(
                            gerenciador.alterarNome(
                                    funcionario,
                                    lerTexto("Novo nome: ")
                            ),
                            "Alteração de nome"
                    );
                    break;

                case 4:
                    exibirResultado(
                            gerenciador.alterarCargo(
                                    funcionario,
                                    lerTexto("Novo cargo: ")
                            ),
                            "Alteração de cargo"
                    );
                    break;

                case 5:
                    exibirResultado(
                            gerenciador.alterarCpf(
                                    funcionario,
                                    lerTexto("Novo CPF: ")
                            ),
                            "Alteração de CPF"
                    );
                    break;

                case 6:
                    exibirResultado(
                            gerenciador.alterarEmail(
                                    funcionario,
                                    lerTexto("Novo e-mail: ")
                            ),
                            "Alteração de e-mail"
                    );
                    break;

                case 7:
                    exibirResultado(
                            gerenciador.alterarTelefone(
                                    funcionario,
                                    lerTexto("Novo telefone: ")
                            ),
                            "Alteração de telefone"
                    );
                    break;

                case 0:
                    System.out.println("Voltando...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    /**
     * Remove um funcionário pelo usuário.
     */
    private void removerFuncionario() {

        System.out.println("\n--- REMOVER FUNCIONÁRIO ---");

        String user = lerTexto("Digite o usuário: ");

        Funcionario funcionario =
                gerenciador.obterFuncionario(user);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        if (funcionario.getId() == administrador.getId()) {
            System.out.println("Não é possível remover a própria conta durante o acesso.");
            return;
        }

        System.out.println(funcionario);

        String confirmacao =
                lerTexto("Deseja realmente remover? (S/N): ");

        if (confirmacao.equalsIgnoreCase("S")) {

            exibirResultado(
                    gerenciador.removerFuncionario(funcionario),
                    "Remoção de funcionário"
            );

        } else {
            System.out.println("Operação cancelada.");
        }
    }

    /**
     * Permite que o administrador autenticado altere sua própria senha.
     */
    private void alterarMinhaSenha() {

        System.out.println("\n--- ALTERAR MINHA SENHA ---");

        String senhaAtual =
                lerTexto("Digite sua senha atual: ");

        if (!senhaAtual.equals(administrador.getSenha())) {
            System.out.println("Senha atual incorreta!");
            return;
        }

        String novaSenha =
                lerTexto("Digite a nova senha: ");

        String confirmacao =
                lerTexto("Confirme a nova senha: ");

        if (!novaSenha.equals(confirmacao)) {
            System.out.println("As senhas não correspondem.");
            return;
        }

        exibirResultado(
                gerenciador.alterarSenha(administrador, novaSenha),
                "Alteração de senha"
        );
    }

    /**
     * Exibe o resultado de uma operação.
     *
     * @param sucesso resultado da operação
     * @param operacao descrição da operação
     */
    private void exibirResultado(boolean sucesso, String operacao) {

        if (sucesso) {
            System.out.println(operacao + " realizada com sucesso!");
        } else {
            System.out.println("Não foi possível realizar a operação: "
                    + operacao + ".");
        }
    }

    /**
     * Lê um texto do terminal.
     */
    private String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    /**
     * Lê um número inteiro do terminal.
     */
    private int lerInteiro(String mensagem) {

        while (true) {
            System.out.print(mensagem);

            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido!");
            }
        }
    }
}
