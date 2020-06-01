package br.com.emerson.cesar;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CifraDeCesarApplication {

	private static final Logger LOGGER = LogManager.getLogger(CifraDeCesarApplication.class);

	private static final String NOME_ARQUIVO = "jsonCifra";

	private static final String TOKEN_KEY = "token";
	private static final String TOKEN_VALUE = System.getenv("TOKEN");
	private static final String RECURSO_SUBMIT = "submit-solution";
	private static final String RECURSO_GERAR = "generate-data";
	private static final String URL = "https://api.codenation.dev/v1/challenge/dev-ps/";

	public static void main(String[] args) {
		LOGGER.info("Iniciando aplicação");
		Request obj = null;
		try {
			connect();
			obj = decifra();
			criaResumo(obj);
			submit();
		} catch (IOException e) {
			LOGGER.error("Erro {}", e);
		}
		LOGGER.info("Encerrando aplicação");
	}

	private static void submit() throws IOException {
		Conexao conexao = new Conexao();
		adicionaToken(conexao);
		conexao.configuraConexaoPOST(URL + RECURSO_SUBMIT);
		conexao.setContentType("multipart/form-data");
		conexao.doPost();
	}

	private static Request decifra() {
		String content = Utils.getFileContenString();
		Request obj = Mapper.parseToObject(content, Request.class);
		String decifrado = Decifrador.decode(obj.getDeslocamento(), obj.getCifrado());
		obj.setDecifrado(decifrado);
		return obj;
	}

	private static void criaResumo(Request obj) {
		String resumo = Decifrador.gerarResumo(obj.getDecifrado());
		obj.setResumoCriptografado(resumo);
		Mapper.writeValue(Utils.getFileName(NOME_ARQUIVO), obj);
	}

	public static void connect() throws IOException {
		Conexao conexao = new Conexao();
		adicionaToken(conexao);
		conexao.configuraConexaoGET(URL + RECURSO_GERAR);
		conexao.doGet();
		conexao.salvaResultado(NOME_ARQUIVO);
		conexao.fim();
	}

	private static void adicionaToken(Conexao conexao) {
		conexao.adicionaParametro(TOKEN_KEY, TOKEN_VALUE);
	}
}
