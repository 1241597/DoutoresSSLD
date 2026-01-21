package com.example.lp1.BLL;

import com.example.lp1.DAL.ConfiguracaoSimuladorDAL;
import com.example.lp1.Utils.Enums.nivelUrgencia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

/**
 * Carrega e gerencia configurações do simulador
 */
public class ConfiguracaoSimuladorBLL {
    private static final String CAMINHO_FICHEIRO = "Ficheiros/config_simulador.txt";

    private int duracaoVerde;
    private int duracaoLaranja;
    private int duracaoVermelha;

    // Regras de descanso dos médicos
    private int tempoTrabalhoContinuo;
    private int tempoDescansoNecessario;

    // Tempos de escalação de urgência
    private int tempoVerdePararanja;
    private int tempoLaranjaParaVermelha;
    private int tempoVermelhaParaSaida;

    // Estrutura para guardar intervalos por médico
    private String[] nomesMedicos;
    private int[][][] intervalosPorMedico;
    private int numMedicos;

    public ConfiguracaoSimuladorBLL() {
        // Valores padrão caso o ficheiro não exista
        this.duracaoVerde = 1;
        this.duracaoLaranja = 2;
        this.duracaoVermelha = 3;
        this.tempoTrabalhoContinuo = 5; // 5 unidades de trabalho contínuo
        this.tempoDescansoNecessario = 1; // 1 unidade de descanso
        this.tempoVerdePararanja = 3; // Padrão: 3 unidades
        this.tempoLaranjaParaVermelha = 3; // Padrão: 3 unidades
        this.tempoVermelhaParaSaida = 2; // Padrão: 2 unidades

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
                                case "TEMPO_VERMELHA_PARA_SAIDA":
                                    tempoVermelhaParaSaida = valorInt;
                                    break;
                                case "TEMPO_TRABALHO_CONTINUO":
                                    tempoTrabalhoContinuo = valorInt;
                                    break;
                                case "TEMPO_DESCANSO_NECESSARIO":
                                    tempoDescansoNecessario = valorInt;
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
            System.out.println("  - Trabalho contínuo: " + tempoTrabalhoContinuo + " unidades");
            System.out.println("  - Descanso necessário: " + tempoDescansoNecessario + " unidades");
            System.out.println("  - Escalação VERDE→LARANJA: " + tempoVerdePararanja + " unidades");
            System.out.println("  - Escalação LARANJA→VERMELHA: " + tempoLaranjaParaVermelha + " unidades");
            System.out.println("  - Escalação VERMELHA→SAÍDA: " + tempoVermelhaParaSaida + " unidades");
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

    public int getTempoVermelhaParaSaida() {
        return tempoVermelhaParaSaida;
    }

    public int getTempoTrabalhoContinuo() {
        return tempoTrabalhoContinuo;
    }

    public int getTempoDescansoNecessario() {
        return tempoDescansoNecessario;
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

    // ========================================
    // SETTERS - Para permitir alteração de configurações
    // ========================================

    public void setDuracaoVerde(int duracao) {
        if (duracao > 0)
            this.duracaoVerde = duracao;
    }

    public void setDuracaoLaranja(int duracao) {
        if (duracao > 0)
            this.duracaoLaranja = duracao;
    }

    public void setDuracaoVermelha(int duracao) {
        if (duracao > 0)
            this.duracaoVermelha = duracao;
    }

    public void setTempoTrabalhoContinuo(int tempo) {
        if (tempo > 0)
            this.tempoTrabalhoContinuo = tempo;
    }

    public void setTempoDescansoNecessario(int tempo) {
        if (tempo > 0)
            this.tempoDescansoNecessario = tempo;
    }

    public void setTempoVerdePararanja(int tempo) {
        if (tempo > 0)
            this.tempoVerdePararanja = tempo;
    }

    public void setTempoLaranjaParaVermelha(int tempo) {
        if (tempo > 0)
            this.tempoLaranjaParaVermelha = tempo;
    }

    public void setTempoVermelhaParaSaida(int tempo) {
        if (tempo > 0)
            this.tempoVermelhaParaSaida = tempo;
    }

    /**
     * Grava as configurações atuais no ficheiro
     */
    public void gravarConfiguracao() throws IOException {
        ConfiguracaoSimuladorDAL dal = new ConfiguracaoSimuladorDAL();

        Map<String, String> parametros = new HashMap<>();
        parametros.put("DURACAO_VERDE", String.valueOf(duracaoVerde));
        parametros.put("DURACAO_LARANJA", String.valueOf(duracaoLaranja));
        parametros.put("DURACAO_VERMELHA", String.valueOf(duracaoVermelha));
        parametros.put("TEMPO_TRABALHO_CONTINUO", String.valueOf(tempoTrabalhoContinuo));
        parametros.put("TEMPO_DESCANSO_NECESSARIO", String.valueOf(tempoDescansoNecessario));
        parametros.put("TEMPO_VERDE_PARA_LARANJA", String.valueOf(tempoVerdePararanja));
        parametros.put("TEMPO_LARANJA_PARA_VERMELHA", String.valueOf(tempoLaranjaParaVermelha));
        parametros.put("TEMPO_VERMELHA_PARA_SAIDA", String.valueOf(tempoVermelhaParaSaida));

        dal.gravarConfiguracao(parametros);

        System.out.println("[CONFIG] Configurações gravadas com sucesso!");
    }
}
