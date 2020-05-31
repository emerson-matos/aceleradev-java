package br.com.emerson.cesar;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mapper {
    private static final Logger LOGGER = LogManager.getLogger(Mapper.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setSerializationInclusion(Include.NON_NULL);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private Mapper() {

    }

    public static <T> T parseToObject(String content, Class<T> targeClass) {
        try {
            return OBJECT_MAPPER.readValue(content, targeClass);
        } catch (JsonProcessingException e) {
            LOGGER.error("ERRO NO PARSE DO OBJ {}, {}", content, e);
        }
        return null;
    }
}