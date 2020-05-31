package br.com.emerson.cesar;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {
    private String token;
    private String cifrado;
    private String decifrado;

    @JsonProperty("numero_casas")
    private Integer deslocamento;

    @JsonProperty("resumo_criptografico")
    private String resumoCriptografico;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDecifrado() {
        return decifrado;
    }

    public void setDecifrado(String decifrado) {
        this.decifrado = decifrado;
    }

    public Integer getDeslocamento() {
        return deslocamento;
    }

    public void setDeslocamento(Integer deslocamento) {
        this.deslocamento = deslocamento;
    }

    public String getCifrado() {
        return cifrado;
    }

    public void setCifrado(String cifrado) {
        this.cifrado = cifrado;
    }

    public String getResumoCriptografado() {
        return resumoCriptografico;
    }

    public void setResumoCriptografado(String resumoCriptografico) {
        this.resumoCriptografico = resumoCriptografico;
    }

}