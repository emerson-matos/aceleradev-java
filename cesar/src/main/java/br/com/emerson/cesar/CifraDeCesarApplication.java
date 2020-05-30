package br.com.emerson.cesar;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CifraDeCesarApplication {

	private static final Logger LOGGER = LogManager.getLogger(CifraDeCesarApplication.class);

	private static final String TOKEN_VALUE = System.getenv("TOKEN");

	public static void main(String[] args) {
		LOGGER.info("Iniciando aplicação");
		try {
			connect();
		} catch (IOException e) {
			LOGGER.error("Erro {}", e);
		}
		LOGGER.info("Encerrando aplicação");
	}

	public static void connect() throws IOException {
		Map<String, String> parameters = new HashMap<>();
		int status = -1;
		URL url = null;
		HttpsURLConnection con = null;
		Reader streamReader = null;
		String content = null;
		LOGGER.info("Iniciando configuração da conexão");
		parameters.put("token", TOKEN_VALUE);
		url = new URL("https://api.codenation.dev/v1/challenge/dev-ps/generate-data" + Utils.getParamsString(parameters));
		
		con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		LOGGER.info("Configuração da conexão finalizada");
		con.setDoOutput(true);
		LOGGER.info("Connect URL {}", con.getURL());
		
		LOGGER.info("Iniciando conexão");
		con.connect();

		status = con.getResponseCode();
		streamReader = Utils.getResultByStatus(status, con);
		content = Utils.getContent(streamReader);
		Utils.writeToFile("jsonCifra", content);

		con.disconnect();
		LOGGER.info("Arquivo salvo, conexão finalizada");
	}
}
