package com.example.lp1.Controller;

import com.example.lp1.BLL.ConfiguracaoBLL;
import com.example.lp1.Model.Configuracao;

public class ConfiguracaoController {

    // INICIALIZAÇÃO DIRETA: O Java faz 'new' automaticamente quando crias o
    // Controller
    private ConfiguracaoBLL bll = new ConfiguracaoBLL();

    // --- Autenticação ---

    public void autenticar(String password) throws Exception {
        bll.autenticar(password);
    }

    public void alterarPassword(String novaPassword) throws Exception {
        bll.alterarPassword(novaPassword);
    }

    public void alterarSeparador(String novoSeparador) throws Exception {
        bll.alterarSeparador(novoSeparador);
    }

    // --- Acesso ao Model (para a View poder ler/alterar) ---

    public Configuracao getConfiguracao() {
        return bll.getModel();
    }

    // --- Gravação ---

    public void gravarAlteracoes() {
        bll.gravarConfiguracao();
    }
}