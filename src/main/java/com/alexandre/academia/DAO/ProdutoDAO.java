
package com.alexandre.academia.dao;

import com.alexandre.academia.model.Produto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Responsável pela persistência dos produtos da Loja
 * e da Lanchonete em arquivos JSON separados.
 *
 * @author Alexandre
 */
public class ProdutoDAO {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path ARQUIVO_LOJA =
            Path.of("dados", "produtos_loja.json");

    private static final Path ARQUIVO_LANCHONETE =
            Path.of("dados", "produtos_lanchonete.json");

    // =========================
    // PRODUTOS DA LOJA
    // =========================

    public static void exportarProdutosLoja(
            ArrayList<Produto> produtos) {

        exportarProdutos(produtos, ARQUIVO_LOJA);
    }

    public static ArrayList<Produto> importarProdutosLoja() {

        return importarProdutos(ARQUIVO_LOJA);
    }

    // =========================
    // PRODUTOS DA LANCHONETE
    // =========================

    public static void exportarProdutosLanchonete(
            ArrayList<Produto> produtos) {

        exportarProdutos(produtos, ARQUIVO_LANCHONETE);
    }

    public static ArrayList<Produto> importarProdutosLanchonete() {

        return importarProdutos(ARQUIVO_LANCHONETE);
    }

    // =========================
    // MÉTODOS AUXILIARES
    // =========================

    private static void exportarProdutos(
            ArrayList<Produto> produtos, Path arquivo) {

        try {
            Files.createDirectories(arquivo.getParent());

            String json = gson.toJson(produtos);

            Files.writeString(arquivo, json);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao exportar produtos para JSON.", e);
        }
    }

    private static ArrayList<Produto> importarProdutos(
            Path arquivo) {

        try {
            if (!Files.exists(arquivo)) {
                return new ArrayList<>();
            }

            String json = Files.readString(arquivo);

            Produto[] produtosArray =
                    gson.fromJson(json, Produto[].class);

            if (produtosArray == null) {
                return new ArrayList<>();
            }

            return new ArrayList<>(
                    Arrays.asList(produtosArray));

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao importar produtos do JSON.", e);
        }
    }
}
