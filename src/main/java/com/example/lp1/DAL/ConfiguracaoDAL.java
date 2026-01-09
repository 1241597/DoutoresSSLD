package com.example.lp1.DAL;

import com.example.lp1.Model.Configuracao;
import java.io.*;

public class ConfiguracaoDAL {

    // CAMINHOS DOS FICHEIROS DE CONFIGURAÇÃO
    private static final String FICHEIRO_PASSWORD = "Ficheiros/password.txt";
    private static final String FICHEIRO_CONFIGS  = "Ficheiros/config_geral.txt";

    /**
     * Carrega a configuração completa (Password + Preferências).
     */
    public Configuracao carregarConfiguracao() {
        // 1. Tenta ler a password
        String passwordLida = lerPasswordDoFicheiro();

        // 2. Cria o objeto Configuração com essa password
        Configuracao config = new Configuracao(passwordLida);

        // 3. Lê o resto (Pasta base e Separador)
        lerPreferenciasDoFicheiro(config);

        return config;
    }

    /**
     * Grava a configuração completa.
     */
    public void gravarConfiguracao(Configuracao config) {
        gravarPassword(config.getPassword());
        gravarPreferencias(config);
    }

    // --- MÉTODOS PRIVADOS (LEITURA) ---

    private String lerPasswordDoFicheiro() {
        File ficheiro = new File(FICHEIRO_PASSWORD);

        // Se o ficheiro não existe, devolve a password padrão
        if (!ficheiro.exists()) {
            return "admin123!";
        }

        try {
            FileReader fr = new FileReader(ficheiro);
            BufferedReader reader = new BufferedReader(fr);

            String linha = reader.readLine();

            reader.close();
            fr.close();

            if (linha != null && linha.length() > 0) {
                return linha; // Retorna a password lida
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler password: " + e.getMessage());
        }

        return "admin123!"; // Em caso de erro, devolve padrão
    }

    private void lerPreferenciasDoFicheiro(Configuracao config) {
        File ficheiro = new File(FICHEIRO_CONFIGS);

        if (!ficheiro.exists()) {
            // Se não existe, cria o ficheiro com os valores padrão
            gravarPreferencias(config);
            return;
        }

        try {
            FileReader fr = new FileReader(ficheiro);
            BufferedReader reader = new BufferedReader(fr);

            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split("=");

                if (partes.length == 2) {
                    String chave = partes[0];
                    String valor = partes[1];

                    switch (chave) {
                        case "DIRETORIO":
                            config.setDiretorioBase(valor);
                            break;
                        case "SEPARADOR":
                            config.setSeparadorFicheiro(valor);
                            break;
                    }
                }
            }
            reader.close();
            fr.close();
        } catch (IOException e) {
            System.out.println("Erro ao ler configurações: " + e.getMessage());
        }
    }

    // --- MÉTODOS PRIVADOS (ESCRITA) ---

    private void gravarPassword(String password) {
        File ficheiro = new File(FICHEIRO_PASSWORD);

        try {
            FileWriter fw = new FileWriter(ficheiro, false); // false = sobrescrever
            BufferedWriter writer = new BufferedWriter(fw);

            if (password != null) {
                writer.write(password);
            }

            writer.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Erro ao gravar password: " + e.getMessage());
        }
    }

    private void gravarPreferencias(Configuracao config) {
        File ficheiro = new File(FICHEIRO_CONFIGS);

        try {
            FileWriter fw = new FileWriter(ficheiro, false);
            BufferedWriter writer = new BufferedWriter(fw);

            // Grava a pasta base
            writer.write("DIRETORIO=" + config.getDiretorioBase());
            writer.newLine();

            // Grava o separador
            writer.write("SEPARADOR=" + config.getSeparadorFicheiro());

            writer.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Erro ao gravar configurações: " + e.getMessage());
        }
    }
}