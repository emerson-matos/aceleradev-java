package br.com.emerson.cesar;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Decifrador {
    private static final Logger LOGGER = LogManager.getLogger(Decifrador.class);
    private static final Map<Integer, Character> DICIONARIO = new HashMap<>();

    static {
        Integer a;
        for (a = 97; a < 123; a++) {
            DICIONARIO.put(a, Character.toChars(a)[0]);
        }
    }

    private Decifrador() {

    }

    public static String decode(Integer deslocamento, String msg) {
        StringBuilder decifrado = new StringBuilder();
        msg = msg.toLowerCase();
        LOGGER.info("iniciando decode com deslocamento {}", deslocamento);
        msg.chars().forEach(a -> trataCaractere(decifrado, a, deslocamento));
        LOGGER.info("fim do decode");
        LOGGER.info("Resultado do decode: {}", decifrado);
        return decifrado.toString();
    }

    public static String gerarResumo(String value) {
        String sha1 = "";
        LOGGER.info("Gerando resumo da mensagem: {}", value);
        try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
	        digest.reset();
	        digest.update(value.getBytes("utf8"));
            sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
            LOGGER.info("Resumo gerado: {}", sha1);
		} catch (Exception e){
			LOGGER.error("Erro ao gerar resumo usando SHA, {}", e);
        }
        return sha1;
    }

    private static void trataCaractere(StringBuilder texto, Integer pos, Integer deslocamento) {
        if(pos > 96 && pos < 123) {
            Integer caractere = (pos-deslocamento);
            caractere = Math.abs(caractere);
            if(caractere < 123 && caractere > 96) {
                texto.append(DICIONARIO.get(caractere));
            } else if(caractere > 123){
                caractere = caractere - 26;
                texto.append(DICIONARIO.get(caractere));
            } else {
                caractere = caractere + 26;
                texto.append(DICIONARIO.get(caractere));
            }
        } else {
            texto.append(Character.toChars(pos));
        }
    }
}