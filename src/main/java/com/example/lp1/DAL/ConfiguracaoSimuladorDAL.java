package com.example.lp1.DAL;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * DAL para gravar e carregar configurações do simulador
 */
public class ConfiguracaoSimuladorDAL {
    private static final String CAMINHO_FICHEIRO = "Ficheiros/config_simulador.txt";

    /**
     * Grava os parâmetros de configuração no ficheiro
     */
    public void gravarConfiguracao(Map<String, String> parametros) throws IOException {
        File file = new File(CAMINHO_FICHEIRO);

        // Ler o ficheiro existente para preservar comentários e intervalos de médicos
        Map<String, String> linhasOriginais = new HashMap<>();
        StringBuilder conteudoCompleto = new StringBuilder();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    conteudoCompleto.append(linha).append("\r\n");

                    // Guardar linhas que não vamos alterar
                    if (linha.trim().startsWith("INTERVALO_")) {
                        String[] partes = linha.split("=");
                        if (partes.length == 2) {
                            linhasOriginais.put(partes[0].trim(), partes[1].trim());
                        }
                    }
                }
            }
        }

        // Escrever novo ficheiro com valores atualizados
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("# Configuração do Simulador de Urgência\r\n");
            writer.write("# Formato: PARAMETRO=VALOR\r\n");
            writer.write("\r\n");

            // B. TEMPOS DE CONSULTA
            writer.write("# ========================================\r\n");
            writer.write("# B. TEMPOS DE CONSULTA (em unidades de tempo)\r\n");
            writer.write("# ========================================\r\n");
            writer.write("# Duração do atendimento por nível de urgência\r\n");
            writer.write("# VERDE (Baixa urgência)\r\n");
            writer.write("DURACAO_VERDE=" + parametros.getOrDefault("DURACAO_VERDE", "1") + "\r\n");
            writer.write("\r\n");
            writer.write("# LARANJA (Média urgência)\r\n");
            writer.write("DURACAO_LARANJA=" + parametros.getOrDefault("DURACAO_LARANJA", "2") + "\r\n");
            writer.write("\r\n");
            writer.write("# VERMELHA (Urgente)\r\n");
            writer.write("DURACAO_VERMELHA=" + parametros.getOrDefault("DURACAO_VERMELHA", "3") + "\r\n");
            writer.write("\r\n");
            writer.write("# Nota: Se um utente tiver sintomas mistos (ex: VERDE + VERMELHA),\r\n");
            writer.write("# será usada a duração da pior urgência (neste caso, VERMELHA = 3 unidades)\r\n");
            writer.write("\r\n");

            // C. REGRAS DE DESCANSO
            writer.write("# ========================================\r\n");
            writer.write("# C. REGRAS DE DESCANSO DOS MÉDICOS\r\n");
            writer.write("# ========================================\r\n");
            writer.write("# Tempo de trabalho contínuo antes de precisar descansar (em unidades de tempo)\r\n");
            writer.write("TEMPO_TRABALHO_CONTINUO=" + parametros.getOrDefault("TEMPO_TRABALHO_CONTINUO", "5") + "\r\n");
            writer.write("\r\n");
            writer.write("# Tempo de descanso necessário após trabalho contínuo (em unidades de tempo)\r\n");
            writer.write(
                    "TEMPO_DESCANSO_NECESSARIO=" + parametros.getOrDefault("TEMPO_DESCANSO_NECESSARIO", "1") + "\r\n");
            writer.write("\r\n");

            // Preservar intervalos de médicos
            writer.write("# Intervalos de descanso fixos por médico (OPCIONAL - para compatibilidade)\r\n");
            writer.write("# Formato: INTERVALO_NomeMedico=hora-hora,hora-hora\r\n");
            writer.write("# Espaços no nome devem ser substituídos por _\r\n");
            for (Map.Entry<String, String> entry : linhasOriginais.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue() + "\r\n");
            }
            writer.write("# António Ferreira não tem intervalos (não incluir linha)\r\n");
            writer.write("\r\n");

            // D. ELEVAÇÃO DO NÍVEL DE URGÊNCIA
            writer.write("# ========================================\r\n");
            writer.write("# D. ELEVAÇÃO DO NÍVEL DE URGÊNCIA\r\n");
            writer.write("# ========================================\r\n");
            writer.write("# Tempos de escalação de urgência (em unidades de tempo)\r\n");
            writer.write("# Tempo para urgência VERDE passar a LARANJA quando aguardando médico\r\n");
            writer.write(
                    "TEMPO_VERDE_PARA_LARANJA=" + parametros.getOrDefault("TEMPO_VERDE_PARA_LARANJA", "3") + "\r\n");
            writer.write("\r\n");
            writer.write("# Tempo para urgência LARANJA passar a VERMELHA quando aguardando médico\r\n");
            writer.write("TEMPO_LARANJA_PARA_VERMELHA=" + parametros.getOrDefault("TEMPO_LARANJA_PARA_VERMELHA", "3")
                    + "\r\n");
            writer.write("\r\n");
            writer.write("# Tempo para urgência VERMELHA resultar em saída/abandono (erro crítico)\r\n");
            writer.write(
                    "TEMPO_VERMELHA_PARA_SAIDA=" + parametros.getOrDefault("TEMPO_VERMELHA_PARA_SAIDA", "2") + "\r\n");
            writer.write("\r\n");
        }
    }
}
