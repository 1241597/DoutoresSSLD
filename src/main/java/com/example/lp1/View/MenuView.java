package com.example.lp1.View;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MenuView {
    private Scanner scanner;
    
    public MenuView() {
        this.scanner = new Scanner(System.in);
    }
    
    public void mostrarCabecalho() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE GESTÃO DE URGÊNCIA HOSPITALAR   ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
    }
    
    public void mostrarMenu(int dia, int hora, String triagemStatus) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("MenuView.txt");
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder menu = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    menu.append(line).append("\n");
                }
                reader.close();
                
                String menuFormatado = menu.toString()
                    .replace("{DIA}", String.valueOf(dia))
                    .replace("{HORA}", String.valueOf(hora))
                    .replace("{TRIAGEM_STATUS}", triagemStatus);
                
                System.out.print(menuFormatado);
            } else {
                mostrarMenuFallback(dia, hora, triagemStatus);
            }
        } catch (Exception e) {
            mostrarMenuFallback(dia, hora, triagemStatus);
        }
    }
    
    private void mostrarMenuFallback(int dia, int hora, String triagemStatus) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  DIA: " + dia + " | HORA ATUAL: " + hora + ":00 (Unidade " + hora + "/24)");
        System.out.println("  TRIAGEM: " + triagemStatus);
        System.out.println("=".repeat(50));
        System.out.println("\n----- SIMULADOR DIA A DIA -----");
        System.out.println("1 - Avançar Hora");
        System.out.println("2 - Registar Utente");
        System.out.println("3 - Chamar Utente para Triagem");
        System.out.println("4 - Realizar Triagem");
        System.out.println("5 - Encaminhar Manualmente");
        System.out.println("6 - Listar Utentes");
        System.out.println("7 - Listar Médicos");
        System.out.println("8 - Listar Sintomas Disponíveis");
        System.out.println("0 - Sair");
        System.out.print("\nEscolha uma opção: ");
    }
    
    public String lerOpcao() {
        return scanner.nextLine().trim();
    }
    
    public String lerTexto(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    public void mostrarMensagem(String mensagem) {
        System.out.println(mensagem);
    }
    
    public void mostrarErro(String erro) {
        System.out.println("❌ ERRO: " + erro);
    }
    
    public void mostrarSucesso(String mensagem) {
        System.out.println("✅ " + mensagem);
    }
    
    public void mostrarNotificacao(String notificacao) {
        System.out.println("🔔 " + notificacao);
    }
    
    public void aguardarEnter() {
        System.out.println("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    public void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    public void fechar() {
        scanner.close();
    }
}
