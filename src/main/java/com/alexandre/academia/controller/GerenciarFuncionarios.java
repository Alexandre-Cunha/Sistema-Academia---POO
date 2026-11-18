package com.alexandre.academia.controller;

import com.alexandre.academia.model.Administrador;
import com.alexandre.academia.model.Funcionario;
import com.alexandre.academia.dao.FuncionarioDAO;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Gerencia o cadastro de funcionários da academia.
 *
 * <p>O nome de usuário (user) identifica cada funcionário e deve ser
 * único no sistema. As operações básicas de CRUD são delegadas a um
 * {@link CrudGenerico}, enquanto a persistência dos dados em JSON é
 * feita por meio de {@link FuncionarioDAO}.</p>
 *
 * <p>Além do cadastro, da busca e da remoção, a classe oferece métodos
 * interativos para alterar os dados de um funcionário (usuário, senha,
 * nome, cargo, CPF, e-mail e telefone), que leem o novo valor pelo
 * terminal, e também permite verificar e alterar a senha do
 * administrador.</p>
 *
 * <p>Os métodos desta classe exibem mensagens de retorno ao usuário no
 * console ({@code System.out}) para informar o resultado de cada
 * operação.</p>
 *
 * <p>Exemplo de uso:</p>
 * <pre>{@code
 * GerenciarFuncionarios gerenciador = new GerenciarFuncionarios();
 *
 * gerenciador.cadastrarFuncionario(funcionario);
 * Funcionario f = gerenciador.obterFuncionario("maria.silva");
 * gerenciador.alterarSenha(f);
 * }</pre>
 *
 * @author Alexandre
 * @see CrudGenerico
 * @see Funcionario
 * @see FuncionarioDAO
 * @see Administrador
 */
public class GerenciarFuncionarios {

    /** CRUD genérico de funcionários. */
    private CrudGenerico<Funcionario> crud;

    /** Nomes de usuário já utilizados. */
    private ArrayList<String> usuarios;

    /** Leitura de dados pelo terminal. */
    private Scanner scanner;

    /**
     * Constrói o gerenciador carregando os funcionários salvos.
     *
     * <p>Os funcionários são lidos do arquivo JSON por meio de
     * {@link FuncionarioDAO#importarFuncionariosDoJSON()} e a lista de
     * nomes de usuário já utilizados é reconstruída a partir deles.
     * Toda alteração feita posteriormente é gravada com
     * {@link FuncionarioDAO#exportarFuncionariosParaJSON(ArrayList)}.</p>
     */
    public GerenciarFuncionarios() {

        ArrayList<Funcionario> funcionarios =
                FuncionarioDAO.importarFuncionariosDoJSON();

        crud = new CrudGenerico<>(funcionarios, FuncionarioDAO::exportarFuncionariosParaJSON);

        this.usuarios = new ArrayList<>();
        this.scanner = new Scanner(System.in);

        // Reconstrói a lista de usuários
        for (Funcionario f : crud.listar()) {
            usuarios.add(f.getUser());
        }
    }

    /**
     * Salva as alterações no JSON.
     *
     * <p>Chama {@link CrudGenerico#salvarAlteracoes()}, que grava a
     * lista atual de funcionários por meio do {@link FuncionarioDAO}.</p>
     */
    private void salvarDados() {
        crud.salvarAlteracoes();
    }

    /**
     * Busca um funcionário pelo nome de usuário.
     *
     * <p>A comparação é feita com {@code equals}, portanto diferencia
     * letras maiúsculas e minúsculas.</p>
     *
     * @param user nome de usuário do funcionário procurado
     * @return funcionário encontrado, ou {@code null} se o usuário for
     *         {@code null} ou se não houver funcionário com esse
     *         usuário
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
     * Confere se a senha informada é a senha do administrador.
     *
     * @param senhaAdm senha a ser verificada
     * @return {@code true} se a senha não for {@code null} e for igual
     *         à senha atual do administrador; {@code false} caso
     *         contrário
     * @see Administrador#getSenhaAdm()
     */
    public static boolean verificarSenhaAdm(String senhaAdm) {
        return senhaAdm != null && senhaAdm.equals(Administrador.getSenhaAdm());
    }

    /**
     * Informa se o nome de usuário já está em uso.
     *
     * @param user nome de usuário a ser verificado
     * @return {@code true} se o usuário não for {@code null} e já
     *         estiver cadastrado; {@code false} caso contrário
     */
    public boolean usuarioCadastrado(String user) {
        return user != null && usuarios.contains(user);
    }

    /**
     * Cadastra um funcionário.
     *
     * <p>O cadastro é recusado, com uma mensagem no console, quando o
     * funcionário é {@code null}, quando o usuário ou a senha são nulos
     * ou em branco, quando já existe um funcionário com o mesmo usuário
     * ou quando o {@link CrudGenerico} não consegue cadastrá-lo. Em caso
     * de sucesso, o ID gerado é exibido.</p>
     *
     * @param funcionario funcionário a ser cadastrado
     */
    public void cadastrarFuncionario(Funcionario funcionario) {
        if (funcionario == null || funcionario.getUser() == null || funcionario.getUser().isBlank()) {
            System.out.println( "Funcionário inválido: o usuário é obrigatório.");
            return;
        }

        if (funcionario.getSenha() == null || funcionario.getSenha().isBlank()) {
            System.out.println(
                    "Funcionário inválido: a senha é obrigatória.");
            return;
        }

        if (usuarioCadastrado(funcionario.getUser())) {
            System.out.println( "Já existe um funcionário com esse usuário.");
            return;
        }

        if (crud.cadastrar(funcionario)) {
            usuarios.add(funcionario.getUser());
            System.out.println("Funcionário cadastrado com sucesso. ID: " + funcionario.getId());
        } else {
            System.out.println("Não foi possível cadastrar o funcionário.");
        }
    }

    /**
     * Remove um funcionário.
     *
     * <p>Apenas o nome de usuário do funcionário informado é
     * considerado: o funcionário cadastrado com esse usuário é
     * localizado e removido, e o usuário volta a ficar disponível. Se o
     * parâmetro ou o seu usuário forem {@code null}, ou se nenhum
     * funcionário com esse usuário existir, uma mensagem de erro é
     * exibida no console.</p>
     *
     * @param funcionario funcionário a ser removido
     */
    public void removerFuncionario(Funcionario funcionario) {

        if (funcionario == null || funcionario.getUser() == null) {
            System.out.println("Funcionário inválido.");
            return;
        }

        Funcionario encontrado =
                obterFuncionario(funcionario.getUser());

        if (encontrado != null && crud.remover(encontrado.getId())) {
            usuarios.remove(encontrado.getUser());
            System.out.println("Funcionário removido com sucesso.");
        } else {
            System.out.println("Funcionário não encontrado.");
        }
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
     *               (por exemplo, "senha" ou "e-mail")
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
            System.out.println(
                    "Valor vazio. Nada foi alterado.");
            return null;
        }

        return texto;
    }

    /**
     * Altera o nome de usuário.
     *
     * <p>O novo usuário é lido pelo terminal. A alteração não é feita se
     * a entrada for vazia, se o valor for igual ao usuário atual ou se
     * já existir outro funcionário com esse usuário. Em caso de sucesso,
     * a lista de usuários é atualizada e os dados são gravados no
     * JSON.</p>
     *
     * @param funcionario funcionário que terá o usuário alterado
     */
    public void alterarUser(Funcionario funcionario) {

        String novo = lerTexto("usuário");

        if (novo == null) {
            return;
        }

        if (novo.equals(funcionario.getUser())) {
            System.out.println(
                    "O usuário informado é o mesmo. Nada foi alterado.");
            return;
        }

        if (usuarioCadastrado(novo)) {
            System.out.println(
                    "Já existe um funcionário com esse usuário.");
            return;
        }

        usuarios.remove(funcionario.getUser());

        funcionario.setUser(novo);

        usuarios.add(novo);

        salvarDados();

        System.out.println("Usuário alterado com sucesso.");
    }

    /**
     * Altera a senha de um funcionário.
     *
     * <p>A nova senha é lida pelo terminal. Se a entrada for vazia,
     * nada é alterado. Em caso de sucesso, os dados são gravados no
     * JSON.</p>
     *
     * @param funcionario funcionário que terá a senha alterada
     */
    public void alterarSenha(Funcionario funcionario) {

        String novo = lerTexto("senha");

        if (novo != null) {
            funcionario.setSenha(novo);
            salvarDados();
            System.out.println("Senha alterada com sucesso.");
        }
    }

    /**
     * Altera o nome de um funcionário.
     *
     * <p>O novo nome é lido pelo terminal. Se a entrada for vazia,
     * nada é alterado. Em caso de sucesso, os dados são gravados no
     * JSON.</p>
     *
     * @param funcionario funcionário que terá o nome alterado
     */
    public void alterarNome(Funcionario funcionario) {

        String novo = lerTexto("nome");

        if (novo != null) {
            funcionario.setNome(novo);
            salvarDados();
            System.out.println("Nome alterado com sucesso.");
        }
    }

    /**
     * Altera o cargo de um funcionário.
     *
     * <p>O novo cargo é lido pelo terminal. Se a entrada for vazia,
     * nada é alterado. Em caso de sucesso, os dados são gravados no
     * JSON.</p>
     *
     * @param funcionario funcionário que terá o cargo alterado
     */
    public void alterarCargo(Funcionario funcionario) {

        String novo = lerTexto("cargo");

        if (novo != null) {
            funcionario.setCargo(novo);
            salvarDados();
            System.out.println("Cargo alterado com sucesso.");
        }
    }

    /**
     * Altera o CPF de um funcionário.
     *
     * <p>O novo CPF é lido pelo terminal. Se a entrada for vazia,
     * nada é alterado. Em caso de sucesso, os dados são gravados no
     * JSON. O formato do CPF não é validado.</p>
     *
     * @param funcionario funcionário que terá o CPF alterado
     */
    public void alterarCpf(Funcionario funcionario) {

        String novo = lerTexto("CPF");

        if (novo != null) {
            funcionario.setCpf(novo);
            salvarDados();
            System.out.println("CPF alterado com sucesso.");
        }
    }

    /**
     * Altera o e-mail de um funcionário.
     *
     * <p>O novo e-mail é lido pelo terminal. Se a entrada for vazia,
     * nada é alterado. Em caso de sucesso, os dados são gravados no
     * JSON. O formato do e-mail não é validado.</p>
     *
     * @param funcionario funcionário que terá o e-mail alterado
     */
    public void alterarEmail(Funcionario funcionario) {

        String novo = lerTexto("e-mail");

        if (novo != null) {
            funcionario.setEmail(novo);
            salvarDados();
            System.out.println("E-mail alterado com sucesso.");
        }
    }

    /**
     * Altera o telefone de um funcionário.
     *
     * <p>O novo telefone é lido pelo terminal. Se a entrada for vazia,
     * nada é alterado. Em caso de sucesso, os dados são gravados no
     * JSON. O formato do telefone não é validado.</p>
     *
     * @param funcionario funcionário que terá o telefone alterado
     */
    public void alterarTelefone(Funcionario funcionario) {

        String novo = lerTexto("telefone");

        if (novo != null) {
            funcionario.setTelefone(novo);
            salvarDados();
            System.out.println("Telefone alterado com sucesso.");
        }
    }

    /**
     * Altera a senha do administrador.
     *
     * <p>Se a nova senha for {@code null} ou estiver em branco, nada é
     * alterado e uma mensagem de erro é exibida. A senha é atualizada
     * diretamente em {@link Administrador#setSenhaAdm(String)}; este
     * método não grava os dados de funcionários no JSON.</p>
     *
     * @param novaSenhaAdm nova senha do administrador
     */
    public void alterarSenhaAdm(String novaSenhaAdm) {

        if (novaSenhaAdm == null || novaSenhaAdm.isBlank()) {
            System.out.println(
                    "Senha inválida. Nada foi alterado.");
            return;
        }

        Administrador.setSenhaAdm(novaSenhaAdm);

        System.out.println(
                "Senha do administrador alterada com sucesso.");
    }

    /**
     * Exibe todos os funcionários cadastrados no console.
     *
     * <p>Cada funcionário é impresso por meio de seu método
     * {@code toString()}. Se não houver nenhum funcionário, uma mensagem
     * informando isso é exibida.</p>
     */
    public void exibirFuncionarios() {

        ArrayList<Funcionario> funcionarios = crud.listar();

        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }

        for (Funcionario f : funcionarios) {
            System.out.println(f);
        }
    }

    /**
     * Retorna a lista de funcionários.
     *
     * <p>A lista retornada é uma cópia: adicionar ou remover itens dela
     * não altera os dados do gerenciador. Os objetos {@link Funcionario}
     * contidos, no entanto, são os mesmos do cadastro.</p>
     *
     * @return lista de funcionários cadastrados; vazia se não houver
     *         nenhum
     */
    public ArrayList<Funcionario> getFuncionarios() {
        return crud.listar();
    }

    /**
     * Substitui a lista de funcionários e atualiza o JSON.
     *
     * <p>Cria um novo {@link CrudGenerico} a partir da lista recebida
     * (que passa a ser usada diretamente, sem cópia), reconstrói a lista
     * de nomes de usuário já utilizados e grava os dados imediatamente
     * no arquivo JSON. Se a lista for {@code null}, nada é alterado e
     * uma mensagem de erro é exibida.</p>
     *
     * @param funcionarios nova lista de funcionários
     */
    public void setFuncionarios(ArrayList<Funcionario> funcionarios) {
        if (funcionarios == null) {
            System.out.println("Lista de funcionários inválida.");
            return;
        }

        crud = new CrudGenerico<>(funcionarios, FuncionarioDAO::exportarFuncionariosParaJSON);

        usuarios = new ArrayList<>();

        for (Funcionario f : crud.listar()) {
            usuarios.add(f.getUser());
        }

        salvarDados();
    }
}