package com.example.lp1.View;

import com.example.lp1.BLL.ConfiguracaoSimuladorBLL;
import com.example.lp1.Utils.Utils;

import java.util.Scanner;

/**
 * View para configurar a dinâmica de elevação da urgência dos sintomas
 */
public class DinamicaUrgenciaView {
    private ConfiguracaoSimuladorBLL bll;
    private Scanner scanner;

    public DinamicaUrgenciaView() {
        this.bll = new ConfiguracaoSimuladorBLL();
        this.scanner = new Scanner(System.in);
    }

    public void executarMenu() {
        int opcao = -1;

        do {
            mostrarConfiguracoesAtuais();

            System.out.println("\n=== DINÂMICA DA URGÊNCIA DOS SINTOMAS ===");
            System.out.println("1. Alterar tempo de escalação VERDE → LARANJA");
            System.out.println("2. Alterar tempo de escalação LARANJA → VERMELHA");
            System.out.println("3. Alterar tempo de escalação VERMELHA → SAÍDA");
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
                    alterarTempoEscalacao("VERDE → LARANJA", bll.getTempoVerdePararanja(), "VERDE_LARANJA");
                    break;
                case 2:
                    alterarTempoEscalacao("LARANJA → VERMELHA", bll.getTempoLaranjaParaVermelha(), "LARANJA_VERMELHA");
                    break;
                case 3:
                    alterarTempoEscalacao("VERMELHA → SAÍDA", bll.getTempoVermelhaParaSaida(), "VERMELHA_SAIDA");
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
        System.out.println("Escalação VERDE → LARANJA:    " + bll.getTempoVerdePararanja() + " unidade(s)");
        System.out.println("Escalação LARANJA → VERMELHA: " + bll.getTempoLaranjaParaVermelha() + " unidade(s)");
        System.out.println("Escalação VERMELHA → SAÍDA:   " + bll.getTempoVermelhaParaSaida() + " unidade(s)");
        System.out.println("\nNOTA: Se um paciente aguardar mais tempo que o configurado,");
        System.out.println("      o seu nível de urgência será automaticamente elevado.");
    }

    private void alterarTempoEscalacao(String descricao, int valorAtual, String tipo) {
        System.out.println("\n--- ALTERAR TEMPO DE ESCALAÇÃO " + descricao + " ---");
        System.out.println("Valor atual: " + valorAtual + " unidade(s)");
        System.out.print("Insira o novo tempo de espera (em unidades): ");

        try {
            int novoTempo = Integer.parseInt(scanner.nextLine());

            if (novoTempo <= 0) {
                Utils.mostrarMensagem("[ERRO] O tempo deve ser maior que zero.");
                return;
            }

            switch (tipo) {
                case "VERDE_LARANJA":
                    bll.setTempoVerdePararanja(novoTempo);
                    break;
                case "LARANJA_VERMELHA":
                    bll.setTempoLaranjaParaVermelha(novoTempo);
                    break;
                case "VERMELHA_SAIDA":
                    bll.setTempoVermelhaParaSaida(novoTempo);
                    break;
            }

            Utils.mostrarMensagem(
                    ">> Tempo de escalação " + descricao + " alterado para " + novoTempo + " unidade(s).");
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
