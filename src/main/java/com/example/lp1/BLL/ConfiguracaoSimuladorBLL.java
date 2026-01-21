package com.example.lp1.BLL;

import com.example.lp1.Utils.Enums.nivelUrgencia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;

/**
 * Carrega e gerencia configurações do simulador
 */
public class ConfiguracaoSimuladorBLL {
    private static final String CAMINHO_FICHEIRO = "Ficheiros/config_simulador.txt";

    private int duracaoVerde;
    private int duracaoLaranja;
    private int duracaoVermelha;

    // Tempos de escalação de urgência
    private int tempoVerdePararanja;
    private int tempoLaranjaParaVermelha;

    // Estrutura para guardar intervalos por médico
    private String[] nomesMedicos;
    private int[][][] intervalosPorMedico;
    private int numMedicos;

    public ConfiguracaoSimuladorBLL() {
        // Valores padrão caso o ficheiro não exista
        this.duracaoVerde = 2;
        this.duracaoLaranja = 3;
        this.duracaoVermelha = 4;
        this.tempoVerdePararanja = 5; // Padrão: 5 unidades
        this.tempoLaranjaParaVermelha = 3; // Padrão: 3 unidades

        this.nomesMedicos = new String[10];
        this.intervalosPorMedico = new int[10][][];
        this.numMedicos = 0;

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

                        // Processar intervalos de descanso por médico
                        if (parametro.startsWith("INTERVALO_")) {
                            String nomeMedico = parametro.substring(10).replace("_", " ");
                            int[][] intervalos = parseIntervalosDescanso(valor);
                            adicionarIntervaloMedico(nomeMedico, intervalos);
                            continue;
                        }

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
                                case "TEMPO_VERDE_PARA_LARANJA":
                                    tempoVerdePararanja = valorInt;
                                    break;
                                case "TEMPO_LARANJA_PARA_VERMELHA":
                                    tempoLaranjaParaVermelha = valorInt;
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
            System.out.println("  - Escalação VERDE→LARANJA: " + tempoVerdePararanja + " unidades");
            System.out.println("  - Escalação LARANJA→VERMELHA: " + tempoLaranjaParaVermelha + " unidades");
            if (numMedicos > 0) {
                System.out.println("  - INTERVALOS carregados para " + numMedicos + " médico(s)");
            }

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

    public int getTempoVerdePararanja() {
        return tempoVerdePararanja;
    }

    public int getTempoLaranjaParaVermelha() {
        return tempoLaranjaParaVermelha;
    }

    /**
     * Retorna os intervalos de descanso para um médico específico
     * 
     * @param nomeMedico Nome do médico
     * @return Array de intervalos ou null se não tiver
     */
    public int[][] getIntervalosMedico(String nomeMedico) {
        if (nomeMedico == null)
            return null;

        String nomeNormalizado = normalizarNome(nomeMedico);

        for (int i = 0; i < numMedicos; i++) {
            if (nomesMedicos[i] != null && normalizarNome(nomesMedicos[i]).equals(nomeNormalizado)) {
                return intervalosPorMedico[i];
            }
        }
        return null;
    }

    /**
     * Normaliza nome para comparação (remove acentos, converte para minúsculas)
     */
    private String normalizarNome(String nome) {
        String normalizado = Normalizer.normalize(nome, Normalizer.Form.NFD);
        normalizado = normalizado.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return normalizado.toLowerCase().trim();
    }

    /**
     * Adiciona intervalos para um médico
     */
    private void adicionarIntervaloMedico(String nomeMedico, int[][] intervalos) {
        // Expandir arrays se necessário
        if (numMedicos >= nomesMedicos.length) {
            String[] novosNomes = new String[nomesMedicos.length * 2];
            int[][][] novosIntervalos = new int[intervalosPorMedico.length * 2][][];
            System.arraycopy(nomesMedicos, 0, novosNomes, 0, nomesMedicos.length);
            System.arraycopy(intervalosPorMedico, 0, novosIntervalos, 0, intervalosPorMedico.length);
            nomesMedicos = novosNomes;
            intervalosPorMedico = novosIntervalos;
        }

        nomesMedicos[numMedicos] = nomeMedico;
        intervalosPorMedico[numMedicos] = intervalos;
        numMedicos++;
    }

    /**
     * Parse intervalos de descanso do formato "12-13,18-19"
     * Retorna array de intervalos [[12,13], [18,19]]
     */
    private int[][] parseIntervalosDescanso(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return new int[0][0];
        }

        try {
            String[] partes = valor.split(",");
            int[][] intervalos = new int[partes.length][2];

            for (int i = 0; i < partes.length; i++) {
                String[] intervalo = partes[i].trim().split("-");
                if (intervalo.length == 2) {
                    intervalos[i][0] = Integer.parseInt(intervalo[0].trim());
                    intervalos[i][1] = Integer.parseInt(intervalo[1].trim());
                }
            }

            return intervalos;
        } catch (Exception e) {
            System.out.println("[CONFIG] Erro ao processar intervalos: " + e.getMessage());
            return new int[0][0];
        }
    }
}
