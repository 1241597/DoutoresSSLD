package com.example.lp1.BLL;

import com.example.lp1.DAL.ConfiguracaoDAL;
import com.example.lp1.DAL.EspecialidadeDAL;
import com.example.lp1.DAL.MedicoDAL;
import com.example.lp1.DAL.SintomaDAL;
import com.example.lp1.Model.Configuracao;
import com.example.lp1.Model.Especialidade;
import com.example.lp1.Model.Medico;
import com.example.lp1.Model.Sintoma;

public class ConfiguracaoBLL {

    private ConfiguracaoDAL dal = new ConfiguracaoDAL();
    private Configuracao model = dal.carregarConfiguracao();

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

    /**
     * Altera o separador e migra os ficheiros existentes.
     * 1. Lê os dados com o separador antigo.
     * 2. Atualiza a configuração.
     * 3. Grava os dados com o separador novo.
     */
    public void alterarSeparador(String novoSeparador) throws Exception {
        if (novoSeparador == null || novoSeparador.length() != 1) {
            throw new Exception("Separador inválido (deve ser 1 caracter).");
        }

        String separadorAntigo = model.getSeparadorFicheiro();

        // Se for igual, não faz nada
        if (separadorAntigo.equals(novoSeparador)) {
            return;
        }

        // Apenas atualiza a configuração, sem mexer nos ficheiros de dados (conforme
        // pedido pelo utilizador)
        model.setSeparadorFicheiro(novoSeparador);
        dal.gravarConfiguracao(model);

        System.out.println(">> Configuração atualizada. ATENÇÃO: Os ficheiros de dados NÃO foram alterados.");
    }

    public Configuracao getModel() {
        return this.model;
    }

    public void gravarConfiguracao() {
        dal.gravarConfiguracao(this.model);
    }
}