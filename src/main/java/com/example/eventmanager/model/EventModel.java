package com.example.eventmanager.model;

public class EventModel {
    private int id;
    private String nome;
    private String telefone;
    private String rua;
    private String numero;
    private String sobre;
    private String cep;

    public EventModel(int id, String nome, String telefone, String rua, String numero, String sobre, String cep) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.rua = rua;
        this.numero = numero;
        this.sobre = sobre;
        this.cep = cep;
    }

    public EventModel() {
        this.nome = "";
        this.telefone = "";
        this.rua = "";
        this.numero = "";
        this.sobre = "";
        this.cep = "";
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getRua() {
        return rua;
    }

    public String getNumero() {
        return numero;
    }

    public String getSobre() {
        return sobre;
    }

    public String getCep() {
        return cep;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setSobre(String sobre) {
        this.sobre = sobre;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public String toString() {
        return "EventModel{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", rua='" + rua + '\'' +
                ", numero='" + numero + '\'' +
                ", sobre='" + sobre + '\'' +
                ", cep='" + cep + '\'' +
                '}';
    }
}
