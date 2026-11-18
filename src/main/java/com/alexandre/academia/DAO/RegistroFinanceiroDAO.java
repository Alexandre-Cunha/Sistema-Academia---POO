package com.alexandre.academia.DAO;

import com.alexandre.academia.model.RegistroFinanceiro;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Classe responsável pela persistência dos registros financeiros em JSON.
 *
 * @author Alexandre
 */
public class RegistroFinanceiroDAO {

    /**
     * Ensina o Gson a gravar e ler LocalDate como texto (ex.: "2026-11-09").
     * Sem isso o Gson não consegue acessar os campos internos do LocalDate.
     */
    private static class LocalDateAdapter extends TypeAdapter<LocalDate> {

        @Override
        public void write(JsonWriter out, LocalDate data) throws IOException {
            out.value(data.toString());
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            return LocalDate.parse(in.nextString());
        }
    }

    /** Instância do Gson utilizada na conversão dos dados. */
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDate.class,new LocalDateAdapter().nullSafe()).create();

    /** Caminho do arquivo JSON. */
    private static final Path ARQUIVO =
            Path.of("dados", "registros_financeiros.json");

    /**
     * Exporta os registros financeiros para um arquivo JSON.
     *
     * @param registros lista de registros que serão exportados
     */
    public static void exportarRegistrosParaJSON(
            ArrayList<RegistroFinanceiro> registros) {

        try {
            Files.createDirectories(ARQUIVO.getParent());

            String json = gson.toJson(registros);

            Files.writeString(ARQUIVO, json);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao exportar registros financeiros para JSON.", e);
        }
    }

    /**
     * Importa os registros financeiros armazenados no JSON.
     *
     * @return lista de registros (vazia se o arquivo ainda não existir)
     */
    public static ArrayList<RegistroFinanceiro> importarRegistrosDoJSON() {

        try {
            if (!Files.exists(ARQUIVO)) {
                return new ArrayList<>();
            }

            String json = Files.readString(ARQUIVO);

            RegistroFinanceiro[] registrosArray =
                    gson.fromJson(json, RegistroFinanceiro[].class);

            if (registrosArray == null) {
                return new ArrayList<>();
            }

            ArrayList<RegistroFinanceiro> registros = new ArrayList<>(
                    Arrays.asList(registrosArray));

            // Descarta entradas nulas que possam existir no arquivo
            registros.removeIf(Objects::isNull);

            return registros;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao importar registros financeiros do JSON.", e);
        }
    }
}