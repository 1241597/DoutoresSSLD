package com.example.lp1.View;

import com.example.lp1.BLL.ConfiguracaoSimuladorBLL;
import com.example.lp1.Utils.Utils;

import java.util.Scanner;

/**
 * View para configurar os tempos de consulta por nível de urgência
 */
public class TemposConsultaView {
    private ConfiguracaoSimuladorBLL bll;
    private Scanner scanner;

    public TemposConsultaView() {
        this.bll = new ConfiguracaoSimuladorBLL();
        this.scanner = new Scanner(System.in);
    }

    public void executarMenu() {
        int opcao = -1;

        do {
            mostrarConfiguracoesAtuais();

            System.out.println("\n=== GESTÃO DE TEMPOS DE CONSULTA ===");
            System.out.println("1. Alterar tempo de consulta VERDE (Baixa Urgência)");
            System.out.println("2. Alterar tempo de consulta LARANJA (Média Urgência)");
            System.out.println("3. Alterar tempo de consulta VERMELHA (Urgente)");
            System.out.println("4. Gravar alterações");
            System.out.print("0. Voltar | Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
                System.out.println("Erro: Insira apenas números inteiros.");
                continue;
            }

            switch (opcao) {
                case 1:
                    alterarTempoConsulta("VERDE", bll.getDuracaoVerde());
                    break;
                case 2:
                    alterarTempoConsulta("LARANJA", bll.getDuracaoLaranja());
                    break;
                case 3:
                    alterarTempoConsulta("VERMELHA", bll.getDuracaoVermelha());
                    break;
                case 4:
                    gravarAlteracoes();
                    break;
                case 0:
                    System.out.println(">> A voltar...");
                    return;
                default:
                    if (opcao != -1)
                        System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void mostrarConfiguracoesAtuais() {
        System.out.println("\n--- CONFIGURAÇÕES ATUAIS ---");
        System.out.println("VERDE (Baixa):    " + bll.getDuracaoVerde() + " unidade(s) de tempo");
        System.out.println("LARANJA (Média):  " + bll.getDuracaoLaranja() + " unidade(s) de tempo");
        System.out.println("VERMELHA (Urgente): " + bll.getDuracaoVermelha() + " unidade(s) de tempo");
    }

    private void alterarTempoConsulta(String nivel, int valorAtual) {
        System.out.println("\n--- ALTERAR TEMPO DE CONSULTA " + nivel + " ---");
        System.out.println("Valor atual: " + valorAtual + " unidade(s)");
        System.out.print("Insira o novo tempo (em unidades): ");

        try {
            int novoTempo = Integer.parseInt(scanner.nextLine());

            if (novoTempo <= 0) {
                Utils.mostrarMensagem("[ERRO] O tempo deve ser maior que zero.");
                return;
            }

            switch (nivel) {
                case "VERDE":
                    bll.setDuracaoVerde(novoTempo);
                    break;
                case "LARANJA":
                    bll.setDuracaoLaranja(novoTempo);
                    break;
                case "VERMELHA":
                    bll.setDuracaoVermelha(novoTempo);
                    break;
            }

            Utils.mostrarMensagem(">> Tempo de consulta " + nivel + " alterado para " + novoTempo + " unidade(s).");
            Utils.mostrarMensagem(">> Não se esqueça de gravar as alterações (opção 4)!");

        } catch (NumberFormatException e) {
            Utils.mostrarMensagem("[ERRO] Valor inválido. Insira um número inteiro.");
        }
    }

    private void gravarAlteracoes() {
        try {
            bll.gravarConfiguracao();
            Utils.mostrarMensagem(">> Alterações gravadas com sucesso!");
        } catch (Exception e) {
            Utils.mostrarMensagem("[ERRO] Falha ao gravar: " + e.getMessage());
        }
    }
}
