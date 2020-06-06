package br.com.emerson.cesar;

import br.com.emerson.cesar.dto.RequestDTO;
import br.com.emerson.cesar.request.Connect;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;

public class CifraDeCesarApplication {

	private static final Logger LOGGER = LogManager.getLogger(CifraDeCesarApplication.class);

	private static final String NOME_ARQUIVO = "jsonCifra";

	private static final String RECURSO_SUBMIT = "submit-solution";
	private static final String RECURSO_GERAR = "generate-data";

	public static void main(String[] args) {
		LOGGER.info("Iniciando aplicação");
        RequestDTO obj;
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
        Connect con = new Connect();
        con.makePost(RECURSO_SUBMIT, Utils.getFileContenString(), null);
    }

    private static RequestDTO decifra() {
		String content = Utils.getFileContenString();
        RequestDTO obj = Mapper.parseToObject(content, RequestDTO.class);
		String decifrado = Decifrador.decode(obj.getDeslocamento(), obj.getCifrado());
		obj.setDecifrado(decifrado);
		return obj;
	}

    private static void criaResumo(RequestDTO obj) {
		String resumo = Decifrador.gerarResumo(obj.getDecifrado());
		obj.setResumoCriptografado(resumo);
		Mapper.writeValue(Utils.getFileName(NOME_ARQUIVO), obj);
	}

    private static void connect() throws IOException {
        Connect con = new Connect();
        Response res = con.makeGet(RECURSO_GERAR, null);
        Reader streamReader = Utils.getResultByStatus(res);
        String content = Utils.getContent(streamReader);
        Utils.writeToFile(NOME_ARQUIVO, content);
	}
}
