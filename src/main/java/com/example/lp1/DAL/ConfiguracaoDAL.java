package com.example.lp1.DAL;

import com.example.lp1.Model.Configuracao;
import java.io.*;

/**
 * Classe responsável pelo acesso aos dados de configuração (DAL).
 * Gere a leitura e escrita da password no ficheiro de texto físico.
 */
public class ConfiguracaoDAL {

    // Define o caminho dentro de uma pasta para organização
    private static final String CAMINHO_FICHEIRO = "Ficheiros/password.txt";

    /**
     * Carrega a configuração do sistema lendo o ficheiro de texto.
     * Caso o ficheiro não exista, cria um novo ficheiro com uma password predefinida.
     * A leitura é feita de forma bruta, sem remover espaços em branco.
     *
     * @return Um objeto Configuracao contendo a password lida do ficheiro ou a predefinida.
     */
    public Configuracao carregarConfiguracao() {
        File ficheiro = new File(CAMINHO_FICHEIRO);
        String passwordLida = "admin123!";

        // Se o ficheiro não existe, cria o default e retorna
        if (!ficheiro.exists()) {
            Configuracao novaConfig = new Configuracao(passwordLida);
            gravarConfiguracao(novaConfig);
            return novaConfig;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ficheiro))) {
            String linha = reader.readLine();

            // Verifica se a linha existe e não está vazia (sem usar trim)
            if (linha != null && linha.length() > 0) {
                passwordLida = linha;
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler o ficheiro: " + e.getMessage());
        }

        return new Configuracao(passwordLida);
    }

    /**
     * Grava a password atual no ficheiro de texto.
     * Cria a diretoria "Ficheiros" caso ela ainda não exista.
     * Sobrescreve totalmente o conteúdo anterior do ficheiro.
     *
     * @param config O objeto Configuracao que contém a password a ser persistida.
     */
    public void gravarConfiguracao(Configuracao config) {
        File ficheiro = new File(CAMINHO_FICHEIRO);

        // --- IMPORTANTE: Cria a pasta se ela não existir ---
        if (ficheiro.getParentFile() != null && !ficheiro.getParentFile().exists()) {
            ficheiro.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ficheiro, false))) {

            String passwordParaGravar = config.getPassword();

            if (passwordParaGravar != null) {
                writer.write(passwordParaGravar);
            }

            // Opcional: Feedback apenas para debug, pode ser removido se quiseres silêncio total
            System.out.println("Password guardada com sucesso!");

        } catch (IOException e) {
            System.out.println("Erro ao gravar o ficheiro: " + e.getMessage());
        }
    }
}