package com.example.lp1;

import com.example.lp1.Utils.Utils;
import com.example.lp1.View.ConfiguracaoView;
import com.example.lp1.View.EstatisticasView;
import com.example.lp1.Model.Estatisticas;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // =========================================================
        // 1. CARREGAMENTO INICIAL
        // =========================================================
        System.out.println("\n[SISTEMA] A carregar dados para a memória...");

        Utils utils = new Utils();
        // A Utils já usa internamente a configuração para saber de onde ler
        utils.carregarFicheiros();

        System.out.println("[SISTEMA] Carregamento concluído.\n");

        // Criar o objecto de Estatisticas que será passado para a view
        Estatisticas estatisticas = new Estatisticas();

        // =========================================================
        // 2. MENU PRINCIPAL
        // =========================================================
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        do {
            System.out.println("\n=== HOSPITAL MANAGEMENT SYSTEM ===");
            System.out.println("1. Gestão de Médicos, Especialidades e Sintomas");
            System.out.println("2. Gestão do funcionamento do Hospital");
            System.out.println("3. Consultar Estatísticas e Notificações");
            System.out.println("4. Configurações (Acesso Restrito)");
            System.out.print("0. Sair | Escolha uma opção: ");

            try {
                String input = scanner.nextLine();
                if (!input.isEmpty()) {
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
                    System.out.println(">> A abrir gestão de Médicos...");
                    // MedicosView medicosView = new MedicosView();
                    // medicosView.iniciar();
                    break;

                case 2:
                    System.out.println(">> A abrir gestão do funcionamento...");
                    // SimulacaoView simView = new SimulacaoView();
                    // simView.iniciar();
                    break;

                case 3:
                    System.out.println(">> A abrir Estatísticas...");
                    EstatisticasView estatView = new EstatisticasView(estatisticas);
                    estatView.iniciar();
                    break;

                case 4:
                    // Abre o menu de configurações (pede password lá dentro)
                    ConfiguracaoView configuracaoView = new ConfiguracaoView();
                    configuracaoView.iniciar();
                    break;

                case 0:
                    System.out.println(">> A encerrar o sistema. Até logo!");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);

        scanner.close();
    }
}