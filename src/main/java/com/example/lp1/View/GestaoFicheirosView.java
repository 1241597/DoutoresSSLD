package com.example.lp1.View;

import com.example.lp1.Model.Configuracao;
import java.util.Scanner;

public class GestaoFicheirosView {

    /**
     * Menu específico para gerir caminhos e formatação.
     * Recebe o objeto 'config' que vem do Main/Controller.
     */
    public void executarGestaoView(Configuracao config) {

        // Segurança: Se por algum motivo a config vier nula, abortamos para não dar erro.
        if (config == null) {
            System.out.println("[ERRO] Configuração não carregada.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        do {
            System.out.println("\n=== GESTÃO DE FICHEIROS ===");
            // Mostra os valores atuais para o utilizador saber o que está a acontecer
            System.out.println("Pasta Base:    [" + config.getDiretorioBase() + "]");
            System.out.println("Separador CSV: [" + config.getSeparadorFicheiro() + "]");
            System.out.println("--------------------------------");
            System.out.println("1. Alterar Pasta de Leitura/Escrita");
            System.out.println("2. Alterar Separador (; ou ,)");
            System.out.println("3. Repor Padrão (Pasta 'Ficheiros/')");
            System.out.print("0. Voltar | Escolha uma opção: ");

            try {
                String input = scanner.nextLine();
                if (input.length() > 0) {
                    opcao = Integer.parseInt(input);
                } else {
                    opcao = -1;
                }
            } catch (NumberFormatException e) {
                opcao = -1;
                System.out.println("Erro: Insira apenas números inteiros.");
            }

            switch (opcao) {
                case 1:
                    alterarPasta(config, scanner);
                    break;
                case 2:
                    alterarSeparador(config, scanner);
                    break;
                case 3:
                    // Volta a pôr tudo na pasta "Ficheiros/"
                    config.setDiretorioBase("Ficheiros");
                    System.out.println(">> Pasta reposta para o padrão 'Ficheiros/'.");
                    break;
                case 0:
                    System.out.println(">> A gravar alterações e a voltar...");
                    return; // Sai deste menu
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    // --- Métodos Privados Auxiliares ---

    private void alterarPasta(Configuracao config, Scanner scanner) {
        System.out.println("\n>> Indique o nome da nova pasta (ex: Backups)");
        System.out.print("Novo Caminho: ");

        String novoCaminho = scanner.nextLine();

        // Validação simples: não aceita vazio
        if (novoCaminho != null && novoCaminho.length() > 0) {
            // O setDiretorioBase do Model já trata de pôr a barra "/" no fim
            config.setDiretorioBase(novoCaminho);
            System.out.println(">> Sucesso! O sistema vai procurar os ficheiros em: " + config.getDiretorioBase());
        } else {
            System.out.println(">> Operação cancelada (Caminho vazio).");
        }
    }

    private void alterarSeparador(Configuracao config, Scanner scanner) {
        System.out.println("\n>> Indique o caracter separador (ex: ; ou , )");
        System.out.print("Novo Separador: ");

        String novoSeparador = scanner.nextLine();

        // Validação: Tem de ter exatamente 1 caracter
        if (novoSeparador != null && novoSeparador.length() == 1) {
            config.setSeparadorFicheiro(novoSeparador);
            System.out.println(">> Separador atualizado para: \"" + novoSeparador + "\"");
        } else {
            System.out.println(">> Erro: O separador deve ser apenas um caracter.");
        }
    }
}