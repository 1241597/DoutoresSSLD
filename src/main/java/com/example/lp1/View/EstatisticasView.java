package com.example.lp1.View;

import com.example.lp1.Controller.EstatisticasController;
import com.example.lp1.Model.Estatisticas;

import java.util.Scanner;

public class EstatisticasView {

    private EstatisticasController controller;
    private Scanner scanner;

    public EstatisticasView(Estatisticas estatisticas) {
        controller = new EstatisticasController(estatisticas);
        scanner = new Scanner(System.in);
    }

    public void iniciar() {

        int opcao;

        do {
            System.out.println("\n=== ESTATÍSTICAS ===");
            System.out.println("1. Média de utentes por dia");
            System.out.println("2. Utentes por sintoma");
            System.out.println("3. Especialidades mais procuradas");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    System.out.printf("Média diária: %.2f%n",
                            controller.obterMediaDiaria());
                    break;

                case 2:
                    mostrarSintomas();
                    break;

                case 3:
                    mostrarEspecialidades();
                    break;

                case 0:
                    System.out.println(">> A voltar ao menu...");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    private void mostrarSintomas() {
        Estatisticas e = controller.obterEstatisticas();

        System.out.println("\nUtentes por Sintoma:");
        for (int i = 0; i < e.getTotalSintomas(); i++) {
            System.out.println("- " + e.getSintomas()[i]
                    + ": " + e.getContagemSintomas()[i]);
        }
    }

    private void mostrarEspecialidades() {
        Estatisticas e = controller.obterEstatisticas();

        System.out.println("\nEspecialidades:");
        for (int i = 0; i < e.getTotalEspecialidades(); i++) {
            System.out.println("- " + e.getEspecialidades()[i]
                    + ": " + e.getContagemEspecialidades()[i]);
        }
    }
}
