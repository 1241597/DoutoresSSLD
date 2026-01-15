package com.example.lp1.Controller;

import com.example.lp1.BLL.SimuladorBLL;
import com.example.lp1.Model.sintoma;
import com.example.lp1.Utils.Enums.nivelUrgencia;
import com.example.lp1.Utils.Utils;
import com.example.lp1.View.MenuView;

public class SimuladorController {
    private SimuladorBLL simuladorBLL;
    private MenuView view;
    private Utils utils;
    
    public SimuladorController() {
        this.simuladorBLL = new SimuladorBLL();
        this.view = new MenuView();
        this.utils = new Utils();
    }
    
    public void iniciar() {
        utils.carregarFicheiros();
        simuladorBLL.inicializarMedicos(utils.getMedicos());
        
        view.mostrarCabecalho();
        
        boolean continuar = true;
        
        while (continuar) {
            mostrarMenuPrincipal();
            
            String opcao = view.lerOpcao();
            
            switch (opcao) {
                case "1":
                    avancarHora();
                    break;
                    
                case "2":
                    registarUtente();
                    break;
                    
                case "3":
                    chamarParaTriagem();
                    break;
                    
                case "4":
                    realizarTriagem();
                    break;
                    
                case "5":
                    encaminharManualmente();
                    break;
                    
                case "6":
                    listarUtentes();
                    break;
                    
                case "7":
                    listarMedicos();
                    break;
                    
                case "8":
                    listarSintomas();
                    break;
                    
                case "0":
                    continuar = false;
                    view.mostrarMensagem("\nEncerrando simulador...");
                    break;
                    
                default:
                    view.mostrarErro("Opção inválida!");
            }
            
            if (continuar) {
                view.aguardarEnter();
            }
        }
        
        view.fechar();
    }
    
    private void mostrarMenuPrincipal() {
        int dia = simuladorBLL.getDiaAtual();
        int hora = (int) simuladorBLL.getHoraAtual();
        String triagemStatus = simuladorBLL.isTriagemOcupada() ? "OCUPADA" : "LIVRE";
        
        view.mostrarMenu(dia, hora, triagemStatus);
    }
    
    private void avancarHora() {
        String[] notificacoes = simuladorBLL.avancarHora();
        
        view.mostrarMensagem("");
        for (String notif : notificacoes) {
            if (notif != null) {
                processarNotificacao(notif);
            }
        }
    }
    
    private void processarNotificacao(String notificacao) {
        String[] partes = notificacao.split("\\|", 2);
        String tipo = partes[0];
        String mensagem = partes.length > 1 ? partes[1] : "";
        
        switch (tipo) {
            case "HORA":
                view.mostrarMensagem("\n" + "=".repeat(60));
                view.mostrarMensagem("⏰ DIA " + simuladorBLL.getDiaAtual() + " - HORA " + mensagem + ":00");
                view.mostrarMensagem("=".repeat(60));
                break;
                
            case "NOVO_DIA":
                view.mostrarMensagem("\n" + "=".repeat(60));
                view.mostrarMensagem("🌅 NOVO DIA - DIA " + mensagem);
                view.mostrarMensagem("=".repeat(60));
                view.mostrarMensagem("⏰ Reiniciando ciclo de 24 horas...\n");
                break;
                
            case "MEDICO_DISPONIVEL":
                view.mostrarMensagem("✅ NOTIFICAÇÃO: " + mensagem);
                break;
                
            case "FIM_ATENDIMENTO":
                view.mostrarMensagem("✅ NOTIFICAÇÃO: " + mensagem);
                break;
                
            case "MEDICO_SAIU":
                view.mostrarMensagem("✅ NOTIFICAÇÃO: " + mensagem);
                break;
                
            case "ESCALAMENTO_LARANJA":
                view.mostrarMensagem("⚠️  NOTIFICAÇÃO: " + mensagem);
                break;
                
            case "ESCALAMENTO_VERMELHA":
                view.mostrarMensagem("🚨 NOTIFICAÇÃO: " + mensagem);
                break;
                
            case "MEDICO_FORA_HORARIO":
                view.mostrarMensagem("⏰ NOTIFICAÇÃO: " + mensagem);
                break;
                
            case "AVISO":
                view.mostrarMensagem("⚠️  AVISO: " + mensagem);
                break;
                
            case "INFO":
                view.mostrarMensagem("📋 " + mensagem);
                break;
                
            case "MEDICO_DESCANSO":
                view.mostrarMensagem("💤 NOTIFICAÇÃO: " + mensagem);
                break;
                
            case "SAIDA_URGENCIA":
                view.mostrarMensagem("🚨 " + mensagem);
                break;
        }
    }
    
    private void registarUtente() {
        String nome = view.lerTexto("\nNome do utente: ");
        
        if (nome.isEmpty()) {
            view.mostrarErro("Nome inválido!");
            return;
        }
        
        simuladorBLL.registarUtente(nome);
        view.mostrarSucesso("Utente " + nome + " registado às " + 
            (int)simuladorBLL.getHoraAtual() + ":00. Posição na fila: " + 
            simuladorBLL.getNumeroUtentesEspera());
    }
    
    private void chamarParaTriagem() {
        if (simuladorBLL.chamarParaTriagem()) {
            view.mostrarSucesso("Utente " + simuladorBLL.getUtenteEmTriagem().getNome() + 
                " chamado para triagem.");
        } else {
            if (simuladorBLL.isTriagemOcupada()) {
                view.mostrarErro("Triagem está ocupada. Aguarde.");
            } else {
                view.mostrarErro("Não há utentes na fila de espera.");
            }
        }
    }
    
    private void realizarTriagem() {
        view.mostrarMensagem("\n=== REALIZAR TRIAGEM ===");
        
        sintoma[] sintomasDisponiveis = utils.getSintomas();
        
        if (sintomasDisponiveis == null || sintomasDisponiveis.length == 0) {
            view.mostrarErro("Nenhum sintoma disponível no sistema.");
            return;
        }
        
        view.mostrarMensagem("\nSintomas disponíveis:");
        for (int i = 0; i < sintomasDisponiveis.length; i++) {
            sintoma s = sintomasDisponiveis[i];
            if (s != null) {
                view.mostrarMensagem((i + 1) + ". " + s.getNome() + " [" + s.getUrgencia() + "]");
            }
        }
        
        String input = view.lerTexto("\nDigite os números dos sintomas separados por vírgula (ex: 1,3,5): ");
        
        if (input.isEmpty()) {
            view.mostrarErro("Nenhum sintoma selecionado!");
            return;
        }
        
        String[] indices = input.split(",");
        sintoma[] sintomasSelecionados = new sintoma[indices.length];
        
        try {
            for (int i = 0; i < indices.length; i++) {
                int index = Integer.parseInt(indices[i].trim()) - 1;
                if (index >= 0 && index < sintomasDisponiveis.length) {
                    sintomasSelecionados[i] = sintomasDisponiveis[index];
                } else {
                    view.mostrarErro("Índice inválido: " + (index + 1));
                    return;
                }
            }
            
            String[] resultado = simuladorBLL.realizarTriagem(sintomasSelecionados);
            
            view.mostrarMensagem("\n=== Triagem concluída ===");
            for (String r : resultado) {
                if (r != null) {
                    String[] partes = r.split("\\|", 2);
                    if (partes.length > 1) {
                        switch (partes[0]) {
                            case "TRIAGEM_CONCLUIDA":
                                view.mostrarMensagem("Utente: " + partes[1]);
                                break;
                            case "URGENCIA":
                                view.mostrarMensagem("Urgência: " + partes[1]);
                                break;
                            case "ESPECIALIDADE":
                                view.mostrarMensagem("Especialidade: " + partes[1]);
                                break;
                            case "AGUARDA":
                            case "ENCAMINHADO":
                                view.mostrarMensagem(partes[1]);
                                break;
                            case "ERRO":
                                view.mostrarErro(partes[1]);
                                break;
                        }
                    }
                }
            }
            
        } catch (NumberFormatException e) {
            view.mostrarErro("Formato inválido! Use números separados por vírgula.");
        }
    }
    
    private void encaminharManualmente() {
        view.mostrarMensagem("\n=== ENCAMINHAR MANUALMENTE ===");
        
        String nomeUtente = view.lerTexto("Nome do utente: ");
        String nomeMedico = view.lerTexto("Nome do médico: ");
        
        if (nomeUtente.isEmpty() || nomeMedico.isEmpty()) {
            view.mostrarErro("Nome inválido!");
            return;
        }
        
        if (simuladorBLL.encaminharManualmente(nomeUtente, nomeMedico)) {
            view.mostrarSucesso("Utente " + nomeUtente + " encaminhado para Dr(a). " + nomeMedico);
        } else {
            view.mostrarErro("Não foi possível encaminhar. Verifique se o utente está triado e o médico está disponível.");
        }
    }
    
    private void listarUtentes() {
        view.mostrarMensagem("\n=== UTENTES ===");
        
        String[] lista = simuladorBLL.listarUtentes();
        
        for (String item : lista) {
            if (item != null) {
                String[] partes = item.split("\\|");
                
                switch (partes[0]) {
                    case "FILA_ESPERA":
                        view.mostrarMensagem("\n--- Fila de Espera (" + partes[1] + ") ---");
                        if (partes[1].equals("0")) {
                            view.mostrarMensagem("Vazia");
                        }
                        break;
                        
                    case "UTENTE_ESPERA":
                        view.mostrarMensagem(partes[1] + ". " + partes[2] + 
                            " | Triado: " + partes[3] + 
                            (partes[3].equals("Sim") ? " | Urgência: " + partes[4] : ""));
                        break;
                        
                    case "TRIAGEM":
                        view.mostrarMensagem("\n--- Em Triagem ---");
                        if (partes[1].equals("0")) {
                            view.mostrarMensagem("Vazia");
                        }
                        break;
                        
                    case "UTENTE_TRIAGEM":
                        view.mostrarMensagem("- " + partes[1]);
                        break;
                        
                    case "ATENDIMENTO":
                        view.mostrarMensagem("\n--- Em Atendimento ---");
                        break;
                        
                    case "UTENTE_ATENDIMENTO":
                        view.mostrarMensagem("- " + partes[1] + " com Dr(a). " + partes[2]);
                        break;
                }
            }
        }
    }
    
    private void listarMedicos() {
        view.mostrarMensagem("\n=== MÉDICOS ===");
        
        String[] lista = simuladorBLL.listarMedicos();
        
        for (String item : lista) {
            if (item != null) {
                String[] partes = item.split("\\|");
                
                if (partes[0].equals("MEDICO")) {
                    view.mostrarMensagem("Dr(a). " + partes[1] + 
                        " | " + partes[2] + 
                        " | Horário: " + partes[3] + ":00" +
                        " | Status: " + partes[4]);
                }
            }
        }
    }
    
    private void listarSintomas() {
        view.mostrarMensagem("\n=== SINTOMAS DISPONÍVEIS ===");
        
        sintoma[] sintomas = utils.getSintomas();
        
        if (sintomas == null || sintomas.length == 0) {
            view.mostrarErro("Nenhum sintoma disponível.");
            return;
        }
        
        view.mostrarMensagem("\nTotal de sintomas: " + sintomas.length + "\n");
        
        for (nivelUrgencia nivel : nivelUrgencia.values()) {
            view.mostrarMensagem("--- " + nivel + " ---");
            boolean encontrou = false;
            
            for (sintoma s : sintomas) {
                if (s != null && s.getUrgencia() == nivel) {
                    encontrou = true;
                    String especialidades = "";
                    if (s.getEspecialidades() != null && s.getEspecialidades().length > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < s.getEspecialidades().length; i++) {
                            if (s.getEspecialidades()[i] != null) {
                                if (sb.length() > 0) sb.append(", ");
                                sb.append(s.getEspecialidades()[i].getNome());
                            }
                        }
                        especialidades = " [" + sb.toString() + "]";
                    }
                    view.mostrarMensagem("  • " + s.getNome() + especialidades);
                }
            }
            
            if (!encontrou) {
                view.mostrarMensagem("  (Nenhum)");
            }
            view.mostrarMensagem("");
        }
    }
}
