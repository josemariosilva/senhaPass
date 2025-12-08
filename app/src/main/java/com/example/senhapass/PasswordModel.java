package com.example.senhapass;

public class PasswordModel {
    private int id;
    private String descricao;
    private String senha;

    public PasswordModel(int id, String descricao, String senha) {
        this.id = id;
        this.descricao = descricao;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getSenha() {
        return senha;
    }
}
