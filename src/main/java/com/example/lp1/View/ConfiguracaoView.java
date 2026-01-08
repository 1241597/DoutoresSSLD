package com.example.lp1.View;

import java.util.Scanner;

public class ConfiguracaoView { // Nome da classe com Maiúscula (convenção Java)

    // O código tem de estar dentro de um método para funcionar
    public void executarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        do {
            // --- MENU VISUAL (3 Linhas) ---
            System.out.println("\n1. Gestão de Médicos, Especialidades e Sintomas.");
            System.out.println("2. Gestão do funcionamento do Hospital.");
            System.out.println("3. Consultar Estatísticas e Notificações.");
            System.out.println("4. Configurações.");
            System.out.print("0. Sair | Escolha uma opção: ");

            try {
                // Lê a linha toda e converte para int (evita erros de buffer)
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
                System.out.println("Erro: Insira apenas números inteiros.");
            }

            switch (opcao) {
                case 1:
                    System.out.println(">> A abrir gestão de Medicos, Especialidades e Sintomas...");
                    // menuGestao();
                    break;
                case 2:
                    System.out.println(">> A abrir gestão do funcionamento do Hospital...");
                    // menuSimulacao();
                    break;
                case 3:
                    System.out.println(">> A abrir consulta de Estatísticas e Notificações...");
                    // menuEstatisticas();
                    break;
                case 4:
                    System.out.println(">> A abrir as Configurações...");
                    // menuConfig();
                    break;
                case 0: // Alterado de 5 para 0 para coincidir com o texto "0. Sair"
                    System.out.println(">> A guardar dados e a desligar o programa...");
                    // Aqui deves chamar a função de guardar ficheiros antes de sair
                    System.exit(0);
                    break;
                default:
                    if (opcao != -1) System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }
}