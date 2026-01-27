package com.example.lp1.View;

import com.example.lp1.Controller.ConfiguracaoController;
import com.example.lp1.Model.Configuracao; // <--- Não te esqueças de importar isto!
import com.example.lp1.Utils.Utils;

import java.util.Scanner;

public class ConfiguracaoView {
    private Scanner scanner = new Scanner(System.in);
    private ConfiguracaoController controller = new ConfiguracaoController();

    public void iniciar() {
        Utils.mostrarMensagem("\n=== ACESSO RESTRITO ===");
        System.out.print("Insira a password de administrador: ");
        String input = scanner.nextLine();

        try {
            // O controller.autenticar deve retornar true/false ou lançar exceção
            // Assumindo que lança exceção se falhar, como tinhas:
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
            System.out.println("2. Gestão de Tempos de Consulta.");
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
                    GestaoFicheirosView gestaoFicheirosView = new GestaoFicheirosView();

                    // --- CORREÇÃO AQUI ---
                    // 1. Pedimos a configuração atual ao Controller
                    Configuracao configAtual = controller.getConfiguracao();

                    // 2. Passamos essa configuração para a View (em vez de null)
                    gestaoFicheirosView.executarGestaoView(configAtual);

                    // 3. Quando voltar do menu, gravamos as alterações (caso o user tenha mudado a
                    // pasta)
                    controller.gravarAlteracoes();
                    break;

                case 2:
                    System.out.println(">> A abrir gestão dos Tempos de Consulta...");
                    TemposConsultaView temposView = new TemposConsultaView();
                    temposView.executarMenu();
                    break;
                case 3:
                    System.out.println(">> A abrir gestão das Regras de Descanso dos Médicos...");
                    RegrasDescansoView regrasView = new RegrasDescansoView();
                    regrasView.executarMenu();
                    break;
                case 4:
                    System.out.println(">> A abrir as Configurações da Dinâmica da Urgência dos Sintomas...");
                    DinamicaUrgenciaView dinamicaView = new DinamicaUrgenciaView();
                    dinamicaView.executarMenu();
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

    private void tratarAlteracaoPassword() {
        System.out.println("\n--- ALTERAR PASSWORD ---");
        System.out.print("Insira a nova password: ");
        String novaPass = scanner.nextLine();

        try {
            controller.alterarPassword(novaPass);
            Utils.mostrarMensagem(">> Password alterada e gravada com sucesso!");
        } catch (Exception e) {
            Utils.mostrarMensagem("[ERRO] " + e.getMessage());
        }
    }
}