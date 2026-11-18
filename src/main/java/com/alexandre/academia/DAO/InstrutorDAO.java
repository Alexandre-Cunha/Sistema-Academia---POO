package com.alexandre.academia.dao;

import com.alexandre.academia.model.Instrutor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class InstrutorDAO {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path ARQUIVO =
            Path.of("dados", "instrutores.json");

    /**
     * Exporta os instrutores para JSON.
     */
    public static void exportarInstrutoresParaJSON(
            ArrayList<Instrutor> instrutores) {

        try {
            Files.createDirectories(ARQUIVO.getParent());

            String json = gson.toJson(instrutores);

            Files.writeString(ARQUIVO, json);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao exportar instrutores para JSON.", e);
        }
    }

    /**
     * Importa os instrutores armazenados no JSON.
     */
    public static ArrayList<Instrutor> importarInstrutoresDoJSON() {

        try {
            if (!Files.exists(ARQUIVO)) {
                return new ArrayList<>();
            }

            String json = Files.readString(ARQUIVO);

            Instrutor[] instrutoresArray =
                    gson.fromJson(json, Instrutor[].class);

            if (instrutoresArray == null) {
                return new ArrayList<>();
            }

            return new ArrayList<>(
                    Arrays.asList(instrutoresArray));

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao importar instrutores do JSON.", e);
        }
    }
}
