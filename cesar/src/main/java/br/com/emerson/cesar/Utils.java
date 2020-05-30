package br.com.emerson.cesar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Utils {

    private static final Logger LOGGER = LogManager.getLogger(Utils.class);

    private Utils() {

    }

    public static String getParamsString(final Map<String, String> params) throws UnsupportedEncodingException {
        final StringBuilder result = new StringBuilder();
        LOGGER.info("Gerando query parameters");
        result.append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        final String resultString = result.toString();
        LOGGER.info("Query parameters gerados: {}", resultString);
        return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
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

    public static Reader getResultByStatus(int status, HttpsURLConnection con) throws IOException {
        InputStreamReader streamReader;
        LOGGER.info("Response status code: {}", status);
        if (status > 299) {
            streamReader = new InputStreamReader(con.getErrorStream());
            LOGGER.warn("Algo de errado ocorreu ao recuperar a resposta");
        } else {
            streamReader = new InputStreamReader(con.getInputStream());
            LOGGER.info("Sucesso ao recuperar a resposta");
        }
        return streamReader;
    }

    public static void writeToFile(String name, String json) {
        String filePath = System.getProperty("user.dir")+ File.separator + name + ".json";
        File myObj;
        StringBuilder result;
        LOGGER.info("Criando arquivo {}", filePath);
        try (BufferedWriter file = new BufferedWriter(new FileWriter(filePath))) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        result = new StringBuilder();
        myObj = new File(filePath);
        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              result.append(data);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        LOGGER.info("File content: {}", result);
    }
}