
package com.alexandre.academia.dao;

import com.alexandre.academia.model.Cliente;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Classe responsável pela persistência dos clientes em JSON.
 *
 * @author Alexandre
 */
public class ClienteDAO {

    /** Instância do Gson utilizada na conversão dos dados. */
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /** Caminho do arquivo JSON. */
    private static final Path ARQUIVO =
            Path.of("dados", "clientes.json");

    /**
     * Exporta os clientes cadastrados para um arquivo JSON.
     *
     * @param clientes lista de clientes que serão exportados
     */
    public static void exportarClientesParaJSON(
            ArrayList<Cliente> clientes) {

        try {
            // Cria a pasta dados caso ela não exista
            Files.createDirectories(ARQUIVO.getParent());

            // Converte a lista de clientes para JSON
            String json = gson.toJson(clientes);

            // Escreve os dados no arquivo
            Files.writeString(ARQUIVO, json);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao exportar clientes para JSON.", e);
        }
    }

    /**
     * Importa os clientes armazenados no arquivo JSON.
     *
     * @return lista de clientes recuperados do JSON
     */
    public static ArrayList<Cliente> importarClientesDoJSON() {

        try {
            // Verifica se o arquivo existe
            if (!Files.exists(ARQUIVO)) {
                return new ArrayList<>();
            }

            // Lê o conteúdo do arquivo
            String json = Files.readString(ARQUIVO);

            // Verifica se o arquivo está vazio
            if (json.isBlank()) {
                return new ArrayList<>();
            }

            // Define o tipo da lista para o Gson
            Type tipoLista = new TypeToken<ArrayList<Cliente>>() {
            }.getType();

            // Converte o JSON para ArrayList de Cliente
            ArrayList<Cliente> clientes =
                    gson.fromJson(json, tipoLista);

            // Retorna a lista recuperada
            return clientes != null ? clientes : new ArrayList<>();

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao importar clientes do JSON.", e);
        }
    }
}
