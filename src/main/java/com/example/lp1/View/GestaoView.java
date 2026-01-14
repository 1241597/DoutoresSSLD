package com.example.lp1.View;

import java.util.Scanner;

public class GestaoView {

    public void iniciar() {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        do {
            System.out.println("\n=== GESTÃO ===");
            System.out.println("1. Gestão de Médicos");
            System.out.println("2. Gestão de Utentes");
            System.out.println("3. Gestão de Especialidades");
            System.out.println("4. Gestão de Sintomas");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            try {
                String in = scanner.nextLine();
                opcao = in.isEmpty() ? -1 : Integer.parseInt(in);
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    new MedicosView().iniciar();
                    break;
                case 2:
                    new UtentesView().iniciar();
                    break;
                case 3:
                    new EspecialidadesView().iniciar();
                    break;
                case 4:
                    new SintomasView().iniciar();
                    break;
                case 0:
                    System.out.println("A voltar...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }
}

