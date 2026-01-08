package com.example.lp1.View;

import com.example.lp1.Controller.ConfiguracaoController;
import com.example.lp1.Utils.Utils;

import java.util.Scanner;

public class ConfiguracaoView {
    private Scanner scanner = new Scanner(System.in);
    private ConfiguracaoController controller = new ConfiguracaoController();

    /**
     * PONTO DE ENTRADA (Chamado pelo Main).
     * 1. Pede a password.
     * 2. Se correta -> Abre o menu.
     * 3. Se incorreta -> Mostra erro e volta ao Main.
     */
    public void iniciar() {
        Utils.mostrarMensagem("\n=== ACESSO RESTRITO ===");
        System.out.print("Insira a password de administrador: ");
        String input = scanner.nextLine();

        try {
            controller.autenticar(input);

            Utils.mostrarMensagem(">> Acesso Autorizado.");
            executarMenuConfiguracoes();

        } catch (Exception e) {
            Utils.mostrarMensagem("[ERRO] " + e.getMessage());
        }
    }

    public void executarMenuConfiguracoes() {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        do {
            System.out.println("\n1. Gestão de Ficheiros.");
            System.out.println("2. Gestão de Tempos de Consulta .");
            System.out.println("3. Regras de Descanso dos Médicos.");
            System.out.println("4. Dinâmica da Urgência dos Sintomas.");
            System.out.println("5. Segurança.");
            System.out.print("0. Voltar | Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
                System.out.println("Erro: Insira apenas números inteiros.");
            }

            switch (opcao) {
                case 1:
                    System.out.println(">> A abrir gestão de Ficheiros...");
                    // menuGestao();
                    break;
                case 2:
                    System.out.println(">> A abrir gestão dos Tempos de Consulta...");
                    // menuSimulacao();
                    break;
                case 3:
                    System.out.println(">> A abrir gestão das Regras de Descanso dos Médicos...");
                    // menuEstatisticas();
                    break;
                case 4:
                    System.out.println(">> A abrir as Configurações da Dinâmica da Urgência dos Sintomas...");
                    // menuConfig();
                    break;
                case 5:
                    System.out.println(">> A abrir as Configurações de Segurança...");
                    tratarAlteracaoPassword();
                    break;
                case 0:
                    System.out.println(">> A voltar para o menu principal...");
                    return;
                default:
                    if (opcao != -1) System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    /**
     * Lógica visual para alterar a password (Opção 5).
     */
    private void tratarAlteracaoPassword() {
        System.out.println("\n--- ALTERAR PASSWORD ---");
        System.out.print("Insira a nova password: ");
        String novaPass = scanner.nextLine();

        try {
            // Manda o controller alterar
            controller.alterarPassword(novaPass);
            Utils.mostrarMensagem(">> Password alterada e gravada com sucesso!");
        } catch (Exception e) {
            Utils.mostrarMensagem("[ERRO] " + e.getMessage());
        }
    }
}