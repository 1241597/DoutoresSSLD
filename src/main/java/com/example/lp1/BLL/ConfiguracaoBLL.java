package com.example.lp1.BLL;

import com.example.lp1.DAL.ConfiguracaoDAL;
import com.example.lp1.Model.Configuracao;

/**
 * Camada de Lógica de Negócio (BLL).
 * Lança exceções genéricas com mensagens detalhadas em caso de erro.
 */
public class ConfiguracaoBLL {

    private ConfiguracaoDAL dal;
    private Configuracao model;

    public ConfiguracaoBLL() {
        this.dal = new ConfiguracaoDAL();
        this.model = dal.carregarConfiguracao();
    }

    /**
     * Valida a password.
     * @throws Exception Se a password for nula, vazia ou incorreta.
     */
    public void autenticar(String tentativa) throws Exception {
        if (tentativa == null || tentativa.length() == 0) {
            throw new Exception("A password não pode estar vazia.");
        }

        if (!model.getPassword().equals(tentativa)) {
            throw new Exception("Password incorreta. Tente novamente.");
        }
    }

    /**
     * Altera a password.
     * @throws Exception Se a nova password não cumprir os requisitos.
     */
    public void alterarPassword(String novaPassword) throws Exception {
        if (novaPassword == null || novaPassword.length() < 3) {
            throw new Exception("A password é muito curta (min. 3 caracteres).");
        }

        model.setPassword(novaPassword);
        dal.gravarConfiguracao(model);
    }
}