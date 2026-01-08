package com.example.lp1.Controller;

import com.example.lp1.BLL.ConfiguracaoBLL;

public class ConfiguracaoController {

    private ConfiguracaoBLL bll;

    public ConfiguracaoController() {
        this.bll = new ConfiguracaoBLL();
    }

    /**
     * Valida a password de entrada.
     * @throws Exception Se a password estiver errada.
     */
    public void autenticar(String password) throws Exception {
        bll.autenticar(password);
    }

    /**
     * Altera a password.
     * @throws Exception Se a nova password for inválida.
     */
    public void alterarPassword(String novaPassword) throws Exception {
        bll.alterarPassword(novaPassword);
    }
}