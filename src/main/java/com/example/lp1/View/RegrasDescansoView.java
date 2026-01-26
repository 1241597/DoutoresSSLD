package com.example.lp1.View;

import com.example.lp1.BLL.ConfiguracaoSimuladorBLL;
import com.example.lp1.Utils.Utils;

import java.util.Scanner;

/**
 * View para configurar as regras de descanso dos médicos
 */
public class RegrasDescansoView {
    private ConfiguracaoSimuladorBLL bll;
    private Scanner scanner;

    public RegrasDescansoView() {
        this.bll = new ConfiguracaoSimuladorBLL();
        this.scanner = new Scanner(System.in);
    }

    public void executarMenu() {
        int opcao = -1;

        do {
            mostrarConfiguracoesAtuais();

            System.out.println("\n=== REGRAS DE DESCANSO DOS MÉDICOS ===");
            System.out.println("1. Alterar tempo de trabalho contínuo permitido");
            System.out.println("2. Alterar tempo de descanso necessário");
            System.out.println("3. Gravar alterações");
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
                    alterarTempoContinuo();
                    break;
                case 2:
                    alterarTempoDescanso();
                    break;
                case 3:
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
        System.out.println("Tempo de trabalho contínuo: " + bll.getTempoTrabalhoContinuo() + " unidade(s)");
        System.out.println("Tempo de descanso necessário: " + bll.getTempoDescansoNecessario() + " unidade(s)");
        System.out.println("\nRegra: Após " + bll.getTempoTrabalhoContinuo() + " unidades de trabalho contínuo,");
        System.out
                .println("       o médico precisa de " + bll.getTempoDescansoNecessario() + " unidade(s) de descanso.");
    }

    private void alterarTempoContinuo() {
        System.out.println("\n--- ALTERAR TEMPO DE TRABALHO CONTÍNUO ---");
        System.out.println("Valor atual: " + bll.getTempoTrabalhoContinuo() + " unidade(s)");
        System.out.print("Insira o novo tempo de trabalho contínuo (em unidades): ");

        try {
            int novoTempo = Integer.parseInt(scanner.nextLine());

            if (novoTempo <= 0) {
                Utils.mostrarMensagem("[ERRO] O tempo deve ser maior que zero.");
                return;
            }

            bll.setTempoTrabalhoContinuo(novoTempo);
            Utils.mostrarMensagem(">> Tempo de trabalho contínuo alterado para " + novoTempo + " unidade(s).");
            Utils.mostrarMensagem(">> Não se esqueça de gravar as alterações (opção 3)!");

        } catch (NumberFormatException e) {
            Utils.mostrarMensagem("[ERRO] Valor inválido. Insira um número inteiro.");
        }
    }

    private void alterarTempoDescanso() {
        System.out.println("\n--- ALTERAR TEMPO DE DESCANSO NECESSÁRIO ---");
        System.out.println("Valor atual: " + bll.getTempoDescansoNecessario() + " unidade(s)");
        System.out.print("Insira o novo tempo de descanso necessário (em unidades): ");

        try {
            int novoTempo = Integer.parseInt(scanner.nextLine());

            if (novoTempo <= 0) {
                Utils.mostrarMensagem("[ERRO] O tempo deve ser maior que zero.");
                return;
            }

            bll.setTempoDescansoNecessario(novoTempo);
            Utils.mostrarMensagem(">> Tempo de descanso necessário alterado para " + novoTempo + " unidade(s).");
            Utils.mostrarMensagem(">> Não se esqueça de gravar as alterações (opção 3)!");

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
