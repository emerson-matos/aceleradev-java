package br.com.emerson.cesar;

import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Scanner;

public final class Utils {

    private static final Logger LOGGER = LogManager.getLogger(Utils.class);
    private static String filePath;

    private Utils() {

    }

    public static String getContent(Reader streamReader) throws IOException {
        BufferedReader in = new BufferedReader(streamReader);
        String inputLine;
        StringBuilder content = new StringBuilder();
        LOGGER.info("Recuperando conteudo");
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        LOGGER.info("Conteudo: {}", content);
        return content.toString();
    }

    public static Reader getResultByStatus(Response res) throws IOException {
        InputStreamReader streamReader;
        int status = res.code();
        LOGGER.info("Response status code: {}", status);
        if (status > 299) {
            streamReader = new InputStreamReader(res.body().byteStream());
            LOGGER.warn("Algo de errado ocorreu ao recuperar a resposta");
        } else {
            streamReader = new InputStreamReader(res.body().byteStream());
            LOGGER.info("Sucesso ao recuperar a resposta");
        }
        return streamReader;
    }

    public static void writeToFile(String name, String json) {
        filePath = getFileName(name);
        LOGGER.info("Criando arquivo {}", filePath);
        try (BufferedWriter file = new BufferedWriter(new FileWriter(filePath))) {
            file.write(json);
            file.flush();
            LOGGER.info("Arquivo salvo no local {}", filePath);
        } catch (IOException e) {
            LOGGER.error("Erro ao salvar arquivo {}", e);
        }
    }

    public static String getFileName(String name) {
        return System.getProperty("user.dir") + File.separator + name + ".json";
    }

    public static String getFileContenString() {
        File myObj = new File(filePath);
        StringBuilder result = new StringBuilder();
        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                result.append(data);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        LOGGER.info("File content: {}", result);
        return result.toString();
    }
}