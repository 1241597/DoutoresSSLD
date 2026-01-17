package com.example.lp1.BLL;

import com.example.lp1.Utils.Enums.nivelUrgencia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Carrega e gerencia configurações do simulador
 */
public class ConfiguracaoSimuladorBLL {
    private static final String CAMINHO_FICHEIRO = "Ficheiros/config_simulador.txt";

    private int duracaoVerde;
    private int duracaoLaranja;
    private int duracaoVermelha;

    public ConfiguracaoSimuladorBLL() {
        // Valores padrão caso o ficheiro não exista
        this.duracaoVerde = 2;
        this.duracaoLaranja = 3;
        this.duracaoVermelha = 4;

        carregarConfiguracao();
    }

    /**
     * Carrega as configurações do ficheiro
     */
    private void carregarConfiguracao() {
        File file = new File(CAMINHO_FICHEIRO);
        if (!file.exists()) {
            System.out.println("[CONFIG] Ficheiro de configuração não encontrado. Usando valores padrão.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();

                // Ignorar linhas vazias e comentários
                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }

                // Formato: PARAMETRO=VALOR
                if (linha.contains("=")) {
                    String[] partes = linha.split("=");
                    if (partes.length == 2) {
                        String parametro = partes[0].trim();
                        String valor = partes[1].trim();

                        try {
                            int valorInt = Integer.parseInt(valor);

                            switch (parametro) {
                                case "DURACAO_VERDE":
                                    duracaoVerde = valorInt;
                                    break;
                                case "DURACAO_LARANJA":
                                    duracaoLaranja = valorInt;
                                    break;
                                case "DURACAO_VERMELHA":
                                    duracaoVermelha = valorInt;
                                    break;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("[CONFIG] Valor inválido para " + parametro + ": " + valor);
                        }
                    }
                }
            }

            System.out.println("[CONFIG] Configurações carregadas:");
            System.out.println("  - VERDE: " + duracaoVerde + " unidades");
            System.out.println("  - LARANJA: " + duracaoLaranja + " unidades");
            System.out.println("  - VERMELHA: " + duracaoVermelha + " unidades");

        } catch (IOException e) {
            System.out.println("[CONFIG] Erro ao ler ficheiro: " + e.getMessage());
        }
    }

    /**
     * Retorna a duração do atendimento baseada no nível de urgência
     */
    public int getDuracaoPorUrgencia(nivelUrgencia urgencia) {
        if (urgencia == null) {
            return duracaoVerde; // Padrão
        }

        switch (urgencia) {
            case VERDE:
                return duracaoVerde;
            case LARANJA:
                return duracaoLaranja;
            case VERMELHA:
                return duracaoVermelha;
            default:
                return duracaoVerde;
        }
    }

    // Getters
    public int getDuracaoVerde() {
        return duracaoVerde;
    }

    public int getDuracaoLaranja() {
        return duracaoLaranja;
    }

    public int getDuracaoVermelha() {
        return duracaoVermelha;
    }
}
