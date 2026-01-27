package com.example.lp1.View;

import com.example.lp1.Controller.EstatisticasController;
import com.example.lp1.Model.Estatisticas;

import java.util.Scanner;
import java.util.Locale;
import java.text.NumberFormat;

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
            System.out.println("2. Salários de médicos por dia");
            System.out.println("3. Utentes por sintomas");
            System.out.println("4. Top 3 de especialidades");
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
                    mostrarSalarios();
                    break;


                case 3:
                    mostrarSintomas();
                    break;

                case 4:
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

    private void mostrarSalarios() {
        Estatisticas e = controller.obterEstatisticas();

        // Use Portuguese locale so decimals use comma
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "PT"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        // Prefer snapshots per day if available (each snapshot corresponds to a completed day)
        double[][] snapshots = e.getSalariosPorDia();
        int totalDiasCompletos = snapshots != null ? snapshots.length : 0;

        if (totalDiasCompletos > 0) {
            for (int dia = 1; dia <= totalDiasCompletos; dia++) {
                System.out.println();
                System.out.println("-------- Dia " + dia + " --------");
                System.out.println("Salários por Médico (Dia):");
                double[] snapshot = snapshots[dia - 1];
                int nMedicos = Math.min(snapshot.length, e.getTotalMedicos());
                for (int i = 0; i < nMedicos; i++) {
                    String valor = nf.format(snapshot[i]);
                    System.out.printf("- %s: %s €%n",
                            e.getMedicos()[i],
                            valor);
                }
            }
            return;
        }

        // Fallback: preferir dias simulados completos (incrementados por avancarTempo/registarDia)
        int diasSimulados = e.getDiasSimulados();
        if (diasSimulados <= 0) {
            // fallback para os dias carregados dos ficheiros (ou 1 se não houver)
            diasSimulados = Math.max(1, e.getTotalDias());
        }

        for (int dia = 1; dia <= diasSimulados; dia++) {
            System.out.println();
            System.out.println("-------- Dia " + dia + " --------");
            System.out.println("Salários por Médico (Dia):");
            for (int i = 0; i < e.getTotalMedicos(); i++) {
                String valor = nf.format(e.getSalariosDia()[i]);
                System.out.printf("- %s: %s €%n",
                        e.getMedicos()[i],
                        valor);
            }
        }
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

        String[] esp = e.getEspecialidades();
        int[] cont = e.getContagemEspecialidades();
        int total = e.getTotalEspecialidades();

        // total de pacientes
        int soma = 0;
        for (int i = 0; i < total; i++) {
            soma += cont[i];
        }

        // ordenação simples (bubble sort)
        for (int i = 0; i < total - 1; i++) {
            for (int j = 0; j < total - i - 1; j++) {
                if (cont[j] < cont[j + 1]) {
                    int tmpC = cont[j];
                    cont[j] = cont[j + 1];
                    cont[j + 1] = tmpC;

                    String tmpE = esp[j];
                    esp[j] = esp[j + 1];
                    esp[j + 1] = tmpE;
                }
            }
        }

        System.out.println("\nTop 3 Especialidades:");
        for (int i = 0; i < 3 && i < total; i++) {
            double percent = soma == 0 ? 0 :
                    (double) cont[i] * 100 / soma;

            System.out.printf("%dº %s - %.2f%% (%d pacientes)%n",
                    i + 1, esp[i], percent, cont[i]);
        }
    }
}
