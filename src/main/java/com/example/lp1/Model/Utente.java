package com.example.lp1.Model;

import java.util.Arrays;

public class Utente {
    private String nome;
    private Sintoma[] sintomas;

    public Utente(String nome, Sintoma[] sintomas) {
        this.nome = nome;
        this.sintomas = sintomas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Sintoma[] getSintomas() {
        return sintomas;
    }

    public void setSintomas(Sintoma[] sintomas) {
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
