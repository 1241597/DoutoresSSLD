package com.example.lp1;

import com.example.lp1.Utils.Utils;
import com.example.lp1.View.ConfiguracaoView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Utils utils = new Utils();
        utils.carregarFicheiros();

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
                    break;
                case 4:
                    System.out.println(">> A abrir as Configurações...");
                    ConfiguracaoView configuracaoView = new ConfiguracaoView();
                    configuracaoView.iniciar();
                    break;
                case 0:
                    System.out.println(">> A desligar o programa...");
                    System.exit(0);
                    break;
                default:
                    if(opcao != -1) System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }
}