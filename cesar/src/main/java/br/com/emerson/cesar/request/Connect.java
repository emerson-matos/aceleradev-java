package br.com.emerson.cesar.request;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Connect {

    private static final String ENDPOINT = "https://api.codenation.dev/v1/challenge/dev-ps/";
    private static final Logger LOGGER = LogManager.getLogger(Connect.class);
    private static final String TOKEN_VALUE = "002d3ddc981008a130773f0fa5adda45d4b332b4";
    private static final String TOKEN_KEY = "token";

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();

    public Response makePost(String recurso, String json, Map<String, String> parameters) throws IOException {
        LOGGER.info("Iniciando configuração da conexão para requisição POST");
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("answer", "answer.json", RequestBody.create(JSON, json)).build();
        Request request = geraRequestBuilder(parameters, recurso).post(body).build();

        Response response = client.newCall(request).execute();
        LOGGER.info("Fim de requisição POST com sucesso {} e status code {}", response.isSuccessful(), response.code());
        return response;
    }

    public Response makeGet(String recurso, Map<String, String> parameters) throws IOException {
        LOGGER.info("Iniciando configuração da conexão para requisição POST");
        Request request = geraRequestBuilder(parameters, recurso).build();

        Response response = client.newCall(request).execute();
        LOGGER.info("Fim de requisição GET com resultado {} e status code {}", response.isSuccessful(), response.code());
        return response;
    }

    private Request.Builder geraRequestBuilder(Map<String, String> parameters, String recurso) {
        LOGGER.info("Iniciando configuração da url para realizar requisição");
        if(parameters == null) {
            parameters = new HashMap<>();
        }

        Request.Builder request = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ENDPOINT + recurso).newBuilder();
        parameters.put(TOKEN_KEY, TOKEN_VALUE);
        parameters.entrySet().forEach( parameter -> urlBuilder.addQueryParameter(parameter.getKey(), parameter.getValue()));
        LOGGER.info("Configuração da url realizar");
        return request.url(urlBuilder.build());
    }
}
