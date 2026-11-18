package com.alexandre.academia.dao;

import com.alexandre.academia.model.Administrador;
import com.alexandre.academia.model.Funcionario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FuncionarioDAO {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final Path ARQUIVO =
            Path.of("dados", "funcionarios.json");

    /**
     * Exporta os funcionários para JSON.
     */
    public static void exportarFuncionariosParaJSON(
            ArrayList<Funcionario> funcionarios) {

        try {
            Files.createDirectories(ARQUIVO.getParent());

            JsonArray jsonArray = new JsonArray();

            for (Funcionario funcionario : funcionarios) {

                JsonObject objeto = gson.toJsonTree(funcionario).getAsJsonObject();

                // Identifica o tipo concreto do funcionário
                if (funcionario instanceof Administrador) {
                    objeto.addProperty("tipo", "ADMINISTRADOR");
                } else {
                    objeto.addProperty("tipo", "FUNCIONARIO");
                }

                jsonArray.add(objeto);
            }

            Files.writeString(ARQUIVO, gson.toJson(jsonArray));

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao exportar funcionários para JSON.", e);
        }
    }

    /**
     * Importa os funcionários armazenados no JSON.
     *
     * Identifica o tipo de cada objeto antes de reconstruí-lo.
     */
    public static ArrayList<Funcionario> importarFuncionariosDoJSON() {

        ArrayList<Funcionario> funcionarios = new ArrayList<>();

        try {
            if (!Files.exists(ARQUIVO)) {
                return funcionarios;
            }

            String json = Files.readString(ARQUIVO);

            if (json.isBlank()) {
                return funcionarios;
            }

            JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

            for (int i = 0; i < jsonArray.size(); i++) {

                JsonObject objeto = jsonArray.get(i).getAsJsonObject();

                String tipo = objeto.get("tipo").getAsString();

                // Identifica a classe que deverá ser reconstruída
                if (tipo.equals("ADMINISTRADOR")) {

                    Administrador administrador = gson.fromJson(objeto, Administrador.class);

                    funcionarios.add(administrador);

                } else if (tipo.equals("FUNCIONARIO")) {

                    Funcionario funcionario = gson.fromJson(objeto, Funcionario.class);

                    funcionarios.add(funcionario);
                }
            }

            return funcionarios;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao importar funcionários do JSON.", e);
        }
    }
}
