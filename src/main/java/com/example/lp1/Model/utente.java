package com.example.lp1.Model;

import java.util.Arrays;

public class utente {
    private String nome;
    private sintoma[] sintomas;

    public utente(String nome, sintoma[] sintomas) {
        this.nome = nome;
        this.sintomas = sintomas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public sintoma[] getSintomas() {
        return sintomas;
    }

    public void setSintomas(sintoma[] sintomas) {
        this.sintomas = sintomas;
    }

    @Override
    public String toString() {
        return "utente{" +
                "nome='" + nome + '\'' +
                ", sintomas=" + Arrays.toString(sintomas) +
                '}';
    }
}
