package com.alexandre.academia.controller;

import com.alexandre.academia.dao.ProdutoDAO;
import com.alexandre.academia.model.Produto;
import java.util.ArrayList;

/**
 * Gerencia os produtos da loja da academia.
 *
 * <p>Cada produto é identificado por um ID único, gerado
 * automaticamente no cadastro. As operações básicas de CRUD são
 * delegadas a um {@link CrudGenerico}, enquanto a persistência dos
 * dados em JSON é feita por meio de {@link ProdutoDAO}.</p>
 *
 * <p>Os produtos não são apagados: a remoção é feita por inativação
 * ({@link #inativarProduto(int)}), o que mantém o produto no cadastro
 * e preserva o histórico de vendas. Produtos inativos não podem ser
 * atualizados.</p>
 *
 * <p>Diferente dos demais gerenciadores, os erros de validação são
 * sinalizados com {@link IllegalArgumentException}, e não com mensagens
 * no console.</p>
 *
 * <p>Exemplo de uso:</p>
 * <pre>{@code
 * GerenciarLoja loja = new GerenciarLoja();
 *
 * loja.cadastrarProduto("Garrafa térmica 500 ml", "Squeeze", 39.90, "Acessórios");
 * ArrayList<Produto> disponiveis = loja.listarProdutosAtivos();
 * loja.inativarProduto(1);
 * }</pre>
 *
 * @author Alexandre
 * @see CrudGenerico
 * @see Produto
 * @see ProdutoDAO
 */
public class GerenciarLoja {

    /** CRUD genérico responsável pelas operações comuns. */
    private final CrudGenerico<Produto> crud;

    /**
     * Constrói o gerenciador carregando os produtos da loja salvos.
     *
     * <p>Os produtos são lidos por meio de
     * {@link ProdutoDAO#importarProdutosLoja()}. Toda alteração feita
     * posteriormente é gravada com
     * {@link ProdutoDAO#exportarProdutosLoja(ArrayList)}.</p>
     */
    public GerenciarLoja() {
        this.crud = new CrudGenerico<>(
                ProdutoDAO.importarProdutosLoja(),
                ProdutoDAO::exportarProdutosLoja
        );
    }

    /**
     * Cadastra um novo produto na loja.
     *
     * <p>O produto recebe um ID gerado por {@link CrudGenerico#gerarId()},
     * é marcado como pertencente à loja ({@link Produto#LOJA}) e
     * cadastrado como ativo. Os dados são gravados no JSON.</p>
     *
     * <p>A categoria não é validada.</p>
     *
     * @param descricao   descrição do produto; é obrigatória
     * @param nomeProduto nome do produto; é obrigatório
     * @param preco       preço do produto; deve ser maior que zero
     * @param categoria   categoria do produto
     * @throws IllegalArgumentException se a descrição ou o nome forem
     *                                  nulos ou em branco, ou se o
     *                                  preço for menor ou igual a zero
     */
    public void cadastrarProduto(String descricao, String nomeProduto,
                                 double preco, String categoria) {

        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException(
                    "A descrição do produto é obrigatória.");
        }

        if (nomeProduto == null || nomeProduto.isBlank()) {
            throw new IllegalArgumentException(
                    "O nome do produto é obrigatório.");
        }

        if (preco <= 0) {
            throw new IllegalArgumentException(
                    "O preço deve ser maior que zero.");
        }

        Produto produto = new Produto(
                crud.gerarId(),
                descricao,
                nomeProduto,
                preco,
                categoria,
                Produto.LOJA
        );

        produto.setAtivo(true);

        crud.cadastrar(produto);
    }

    /**
     * Busca um produto pelo ID.
     *
     * <p>Produtos inativos também são retornados. Use
     * {@link Produto#isAtivo()} para verificar a situação.</p>
     *
     * @param id identificador do produto procurado
     * @return produto encontrado, ou {@code null} se nenhum produto
     *         possuir esse ID
     */
    public Produto buscarProduto(int id) {
        return crud.buscarPorId(id);
    }

    /**
     * Retorna todos os produtos cadastrados, inclusive os inativos.
     *
     * <p>A lista retornada é uma cópia: adicionar ou remover itens dela
     * não altera os dados do gerenciador. Os objetos {@link Produto}
     * contidos, no entanto, são os mesmos do cadastro.</p>
     *
     * @return lista com todos os produtos; vazia se não houver nenhum
     */
    public ArrayList<Produto> listarProdutos() {
        return crud.listar();
    }

    /**
     * Retorna somente os produtos ativos.
     *
     * @return nova lista com os produtos ativos; vazia se não houver
     *         nenhum
     */
    public ArrayList<Produto> listarProdutosAtivos() {

        ArrayList<Produto> ativos = new ArrayList<>();

        for (Produto produto : crud.listar()) {
            if (produto.isAtivo()) {
                ativos.add(produto);
            }
        }

        return ativos;
    }

    /**
     * Atualiza os dados de um produto.
     *
     * <p>Substitui a descrição, o nome, o preço e a categoria do produto
     * e grava os dados no JSON. A categoria não é validada e o nome não
     * precisa ser único.</p>
     *
     * @param id          identificador do produto a ser atualizado
     * @param descricao   nova descrição; é obrigatória
     * @param nomeProduto novo nome; é obrigatório
     * @param preco       novo preço; deve ser maior que zero
     * @param categoria   nova categoria
     * @return {@code true} se o produto foi atualizado; {@code false}
     *         se não existir produto com esse ID ou se ele estiver
     *         inativo
     * @throws IllegalArgumentException se o produto existir e estiver
     *                                  ativo, mas a descrição ou o nome
     *                                  forem nulos ou em branco, ou o
     *                                  preço for menor ou igual a zero
     */
    public boolean atualizarProduto(int id, String descricao,
                                    String nomeProduto, double preco,
                                    String categoria) {

        Produto produto = crud.buscarPorId(id);

        if (produto == null || !produto.isAtivo()) {
            return false;
        }

        if (descricao == null || descricao.isBlank() || nomeProduto == null || nomeProduto.isBlank() || preco <= 0) {
            throw new IllegalArgumentException( "Informe dados válidos para o produto.");
        }

        produto.setDescricao(descricao);
        produto.setNomeProduto(nomeProduto);
        produto.setPreco(preco);
        produto.setCategoria(categoria);

        crud.salvarAlteracoes();

        return true;
    }

    /**
     * Inativa um produto sem removê-lo do cadastro.
     *
     * <p>O produto continua salvo, para manter o histórico de vendas,
     * mas deixa de aparecer em {@link #listarProdutosAtivos()} e não
     * pode mais ser atualizado. Os dados são gravados no JSON.</p>
     *
     * @param id identificador do produto a ser inativado
     * @return {@code true} se o produto foi inativado; {@code false} se
     *         não existir produto com esse ID ou se ele já estiver
     *         inativo
     */
    public boolean inativarProduto(int id) {

        Produto produto = crud.buscarPorId(id);

        if (produto == null || !produto.isAtivo()) {
            return false;
        }

        produto.setAtivo(false);

        crud.salvarAlteracoes();

        return true;
    }
}