package com.example.lp1.Model;

public class Configuracao {

    private String password;

    public Configuracao(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Configuracao{" +
                "password='" + password + '\'' +
                '}';
    }
}
