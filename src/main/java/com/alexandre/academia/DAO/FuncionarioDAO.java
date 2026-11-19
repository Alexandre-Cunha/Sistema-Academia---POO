package com.alexandre.academia.dao;

import com.alexandre.academia.model.Administrador;
import com.alexandre.academia.model.Funcionario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Classe responsável pela persistência dos funcionários em JSON.
 *
 * <p>Os funcionários são gravados em
 * {@code dados/funcionarios.json}. Cada objeto recebe o campo
 * {@code tipo} ({@code ADMINISTRADOR} ou {@code FUNCIONARIO}),
 * utilizado na leitura para reconstruir a classe correta.</p>
 *
 * @author Alexandre
 */
public class FuncionarioDAO {

    /** Instância do Gson utilizada na conversão dos dados. */
    private static final Gson gson =
            new GsonBuilder().setPrettyPrinting().create();

    /** Caminho do arquivo JSON dos funcionários. */
    private static final Path ARQUIVO =
            Path.of("dados", "funcionarios.json");

    /**
     * Exporta os funcionários para um arquivo JSON.
     *
     * <p>Cada funcionário é gravado com o campo {@code tipo},
     * que identifica se ele é um administrador ou funcionário comum.</p>
     *
     * @param funcionarios lista de funcionários que serão exportados
     * @throws RuntimeException se ocorrer erro ao gravar o arquivo
     */
    public static void exportarFuncionariosParaJSON(
            ArrayList<Funcionario> funcionarios) {

        try {
            Files.createDirectories(ARQUIVO.getParent());

            JsonArray jsonArray = new JsonArray();

            for (Funcionario funcionario : funcionarios) {

                JsonObject objeto =
                        gson.toJsonTree(funcionario).getAsJsonObject();

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
     * Importa os funcionários armazenados no arquivo JSON.
     *
     * <p>Identifica o tipo de cada objeto antes de reconstruí-lo.
     * Se o objeto não possuir o campo {@code tipo}, será considerado
     * um funcionário comum.</p>
     *
     * @return lista de funcionários recuperados do JSON; vazia se
     *         o arquivo não existir ou estiver em branco
     * @throws RuntimeException se ocorrer erro na leitura ou se
     *                          houver um tipo desconhecido
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

            JsonArray jsonArray =
                    JsonParser.parseString(json).getAsJsonArray();

            for (int i = 0; i < jsonArray.size(); i++) {

                JsonObject objeto =
                        jsonArray.get(i).getAsJsonObject();

                JsonElement tipoElemento = objeto.get("tipo");

                String tipo = (tipoElemento == null
                        || tipoElemento.isJsonNull())
                        ? "FUNCIONARIO"
                        : tipoElemento.getAsString();

                if (tipo.equals("ADMINISTRADOR")) {

                    Administrador administrador =
                            gson.fromJson(objeto, Administrador.class);

                    funcionarios.add(administrador);

                } else if (tipo.equals("FUNCIONARIO")) {

                    Funcionario funcionario =
                            gson.fromJson(objeto, Funcionario.class);

                    funcionarios.add(funcionario);

                } else {

                    throw new RuntimeException(
                            "Tipo de funcionário desconhecido no JSON: "
                            + tipo);
                }
            }

            return funcionarios;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao importar funcionários do JSON.", e);
        }
    }
}
