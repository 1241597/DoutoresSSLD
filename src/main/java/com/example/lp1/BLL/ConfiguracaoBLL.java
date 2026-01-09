package com.example.lp1.BLL;

import com.example.lp1.DAL.ConfiguracaoDAL;
import com.example.lp1.Model.Configuracao;

public class ConfiguracaoBLL {

    // 1. Criamos o DAL primeiro (Inicialização Direta)
    private ConfiguracaoDAL dal = new ConfiguracaoDAL();

    // 2. Usamos o DAL logo a seguir para carregar o Model
    // Como o Java lê de cima para baixo, o 'dal' já existe nesta linha.
    private Configuracao model = dal.carregarConfiguracao();

    // --- Lógica de Negócio ---

    public void autenticar(String tentativa) throws Exception {
        if (tentativa == null || tentativa.isEmpty()) {
            throw new Exception("A password não pode estar vazia.");
        }
        if (!model.getPassword().equals(tentativa)) {
            throw new Exception("Password incorreta.");
        }
    }

    public void alterarPassword(String novaPassword) throws Exception {
        if (novaPassword == null || novaPassword.length() < 3) {
            throw new Exception("A password é muito curta (min. 3 caracteres).");
        }
        model.setPassword(novaPassword);
        dal.gravarConfiguracao(model);
    }

    // --- Getters e Gravação ---

    public Configuracao getConfiguracao() {
        return this.model;
    }

    public void gravarConfiguracao() {
        dal.gravarConfiguracao(this.model);
    }
}