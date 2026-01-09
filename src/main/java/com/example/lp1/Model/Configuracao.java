package com.example.lp1.Model;

import java.io.File;

public class Configuracao {

    // --- 1. Dados de Segurança e Formatação ---
    private String password;
    private String separadorFicheiro;

    // --- 2. Pastas ---
    // A pasta que o utilizador escolheu (pode estar vazia ou ter ficheiros novos)
    private String diretorioEscolhido;

    // A pasta "Original" do projeto (Segurança / Fallback)
    private final String DIRETORIO_PADRAO = "Ficheiros" + File.separator;

    // --- 3. Nomes dos Ficheiros (Os essenciais que pediste) ---
    private static final String FILE_MEDICOS = "medicos.txt";
    private static final String FILE_ESPECIALIDADES = "especialidades.txt";
    private static final String FILE_SINTOMAS = "sintomas.txt";

    // Ficheiros de Sistema (Password e Configuração)
    private static final String FILE_PASSWORD = "password.txt";
    private static final String FILE_CONFIG = "config_geral.txt";

    public Configuracao(String password) {
        this.password = password;
        this.separadorFicheiro = ";"; // Separador por defeito

        // Inicialmente, a pasta escolhida é a padrão "Ficheiros/"
        this.diretorioEscolhido = DIRETORIO_PADRAO;
    }

    // --- LÓGICA INTELIGENTE (FALLBACK) ---

    /**
     * Verifica se o ficheiro existe na pasta nova.
     * Se não existir, devolve o caminho da pasta original.
     */
    private String resolverCaminho(String nomeFicheiro) {
        // Tenta o caminho na pasta escolhida pelo utilizador
        String caminhoPreferido = diretorioEscolhido + nomeFicheiro;
        File ficheiroTeste = new File(caminhoPreferido);

        // Se o ficheiro existe lá, usa-o
        if (ficheiroTeste.exists()) {
            return caminhoPreferido;
        }

        // Se não existe, usa o da pasta padrão "Ficheiros/"
        return DIRETORIO_PADRAO + nomeFicheiro;
    }

    // --- 4. Getters Específicos (Os 3 Principais) ---
    // O resto do programa só chama estes métodos. Não precisa de saber a lógica.

    public String getCaminhoMedicos() {
        return resolverCaminho(FILE_MEDICOS);
    }

    public String getCaminhoEspecialidades() {
        return resolverCaminho(FILE_ESPECIALIDADES);
    }

    public String getCaminhoSintomas() {
        return resolverCaminho(FILE_SINTOMAS);
    }

    // --- Getters de Sistema ---

    public String getCaminhoPassword() {
        // Também aplicamos a lógica aqui. Se criares uma password.txt na pasta nova, ele usa essa.
        return resolverCaminho(FILE_PASSWORD);
    }

    public String getCaminhoConfigGeral() {
        return resolverCaminho(FILE_CONFIG);
    }

    // --- 5. Getters e Setters Gerais ---

    public String getDiretorioBase() {
        return diretorioEscolhido;
    }

    public void setDiretorioBase(String diretorio) {
        if (diretorio != null && diretorio.length() > 0) {
            // Garante a barra no final
            if (diretorio.endsWith(File.separator)) {
                this.diretorioEscolhido = diretorio;
            } else {
                this.diretorioEscolhido = diretorio + File.separator;
            }
        }
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getSeparadorFicheiro() { return separadorFicheiro; }
    public void setSeparadorFicheiro(String s) {
        if(s != null && s.length() == 1) this.separadorFicheiro = s;
    }
}