package br.com.emerson.cesar;

import br.com.emerson.cesar.dto.RequestDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;

public class Mapper {
    private static final Logger LOGGER = LogManager.getLogger(Mapper.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setSerializationInclusion(Include.NON_NULL);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.setVisibility(OBJECT_MAPPER.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
    }

    private Mapper() {

    }

    public static <T> T parseToObject(String content, Class<T> targetClass) {
        T obj = null;
        try {
            LOGGER.info("Iniciando parse do objeto {} para a class {}", content, targetClass.getName());
            obj = OBJECT_MAPPER.readValue(content, targetClass);
            LOGGER.info("Parse realizado com sucesso");
            return obj;
        } catch (JsonProcessingException e) {
            LOGGER.error("ERRO NO PARSE DO OBJ {}, {}", content, e);
        }
        return obj;
    }

    public static void writeValue(String file, RequestDTO obj) {
        try {
            LOGGER.info("Iniciando escrita do objeto {} no local {}", obj, file);
            OBJECT_MAPPER.writeValue(new FileOutputStream(file), obj);
            LOGGER.info("Escrita realizada com sucesso");
        } catch (IOException e) {
            LOGGER.error("ERRO NO PARSE DO OBJ {}, {}", obj, e);
        }
	}
}