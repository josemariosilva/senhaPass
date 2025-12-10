package com.example.senhapass;

public class PasswordModel {
    private int id;
    private String descricao;
    private String senha;

    // Construtor completo
    public PasswordModel(int id, String descricao, String senha) {
        this.id = id;
        this.descricao = descricao;
        this.senha = senha;
    }

    // Construtor usado ao criar novo registro (id será gerado pelo banco)
    public PasswordModel(String descricao, String senha) {
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
