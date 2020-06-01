package br.com.emerson.cesar;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Conexao {

    private static final Logger LOGGER = LogManager.getLogger(Conexao.class);

    private URL url = null;
    private Integer status = -1;
    private Reader result = null;
    private Map<String, String> parameters = new HashMap<>();

    private HttpsURLConnection con;

    public void configuraConexaoGET(String url) throws IOException {
        LOGGER.info("Iniciando configuração da conexão");
        url = url + Utils.getParamsString(parameters);
        this.setUrl(new URL(url));
        con = (HttpsURLConnection) this.url.openConnection();
        LOGGER.info("Configuração da conexão finalizada");
    }

    public void configuraConexaoPOST(String url) throws IOException {
        LOGGER.info("Iniciando configuração da conexão");
        this.setUrl(new URL(url + Utils.getParamsString(parameters)));
        con = (HttpsURLConnection) this.url.openConnection();
        LOGGER.info("Configuração da conexão finalizada");
    }
    public Reader getResult() {
        return result;
    }

    public String getContent() throws IOException {
        return Utils.getContent(this.getResult());
    }

    private void result() throws IOException {
        this.result = Utils.getResultByStatus(status, con);
    }

    public void adicionaParametro(String key, String value) {
        parameters.put(key, value);
    }

    private void setRequestMethod(String method) throws ProtocolException {
        con.setRequestMethod(method);
    }

    public void doGet() throws IOException {
        setRequestMethod("GET");
        con.setDoOutput(true);
        LOGGER.info("Conectando em URL {}", con.getURL());
        LOGGER.info("Iniciando conexão");
        con.connect();
        status = con.getResponseCode();
        result();
    }
    
    public void doPost() throws IOException {
        String formdataTemplate = "Content-Disposition: form-data;file=\"answer\" ;filename=\"{0}\";\r\nContent-Type: multipart/form-data;\r\n\r\n";
        String data = String.format(formdataTemplate, Utils.getFileContenString());
        byte[] postData = data.getBytes(StandardCharsets.UTF_8);
        
        LOGGER.info("Conectando em URL {}", con.getURL());
        LOGGER.info("Iniciando conexão");
        
        con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "multipart/form-data;");
		try (OutputStream wr = con.getOutputStream()) {
            wr.write(postData);
            wr.flush();
        }
        status = con.getResponseCode();
        result();

    }

    private void setUrl(URL url) {
        this.url = url;
    }

    public void fim() {
        con.disconnect();
    }

    public void salvaResultado(String arquivo) throws IOException {
        Utils.writeToFile(arquivo, this.getContent());
    }

    public void setContentType(String string) {
        con.setRequestProperty("Content-type", string);
    }
}
