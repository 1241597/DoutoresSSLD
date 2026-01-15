package com.example.lp1.BLL;

import com.example.lp1.Model.*;
import com.example.lp1.Utils.Enums.nivelUrgencia;

import java.util.HashMap;
import java.util.Map;

public class SimuladorBLL {
    private double horaAtual;
    private int diaAtual;
    private utente[] filaEspera;
    private utente[] filaTriagem;
    private boolean triagemOcupada;
    private Map<medico, Boolean> medicosDisponiveis;
    private Map<medico, utente> medicosEmAtendimento;
    
    public SimuladorBLL() {
        this.horaAtual = 1.0;
        this.diaAtual = 1;
        this.filaEspera = new utente[0];
        this.filaTriagem = new utente[0];
        this.triagemOcupada = false;
        this.medicosDisponiveis = new HashMap<>();
        this.medicosEmAtendimento = new HashMap<>();
    }
    
    public void inicializarMedicos(medico[] medicos) {
        for (medico m : medicos) {
            if (m != null) {
                medicosDisponiveis.put(m, false);
                medicosEmAtendimento.put(m, null);
            }
        }
    }
    
    public String[] avancarHora() {
        horaAtual += 1.0;
        
        if (horaAtual > 24.0) {
            return reiniciarDia();
        }
        
        String[] notificacoes = new String[100];
        int index = 0;
        
        notificacoes[index++] = "HORA|" + (int)horaAtual;
        
        String[] notifDescanso = gerenciarDescansoMedicos();
        for (String n : notifDescanso) {
            if (n != null) notificacoes[index++] = n;
        }
        
        String[] notifUrgencia = verificarEscalamentoUrgencia();
        for (String n : notifUrgencia) {
            if (n != null) notificacoes[index++] = n;
        }
        
        String[] notifMedicos = atualizarDisponibilidadeMedicos();
        for (String n : notifMedicos) {
            if (n != null) notificacoes[index++] = n;
        }
        
        String[] notifAtendimentos = finalizarAtendimentos();
        for (String n : notifAtendimentos) {
            if (n != null) notificacoes[index++] = n;
        }
        
        String[] notifForaHorario = verificarMedicosForaHorario();
        for (String n : notifForaHorario) {
            if (n != null) notificacoes[index++] = n;
        }
        
        return compactarArray(notificacoes, index);
    }
    
    private String[] reiniciarDia() {
        diaAtual++;
        horaAtual = 1.0;
        
        String[] notificacoes = new String[50];
        int index = 0;
        
        notificacoes[index++] = "NOVO_DIA|" + diaAtual;
        
        for (Map.Entry<medico, utente> entry : medicosEmAtendimento.entrySet()) {
            if (entry.getValue() != null) {
                notificacoes[index++] = "AVISO|Dr(a). " + entry.getKey().getNome() + 
                    " ainda está em atendimento com " + entry.getValue().getNome();
            }
        }
        
        if (filaEspera.length > 0) {
            notificacoes[index++] = "INFO|" + filaEspera.length + " utente(s) aguardando na fila de espera.";
        }
        
        return compactarArray(notificacoes, index);
    }
    
    private String[] atualizarDisponibilidadeMedicos() {
        String[] notificacoes = new String[20];
        int index = 0;
        
        for (Map.Entry<medico, Boolean> entry : medicosDisponiveis.entrySet()) {
            medico m = entry.getKey();
            boolean emAtendimento = medicosEmAtendimento.get(m) != null;
            
            if (horaAtual == m.getHoraEntrada() && !entry.getValue() && !emAtendimento && !m.isEmDescanso()) {
                medicosDisponiveis.put(m, true);
                notificacoes[index++] = "MEDICO_DISPONIVEL|Dr(a). " + m.getNome() + 
                    " (" + m.getEspecialidade().getNome() + ") ficou DISPONÍVEL.";
            }
        }
        
        return compactarArray(notificacoes, index);
    }
    
    private String[] finalizarAtendimentos() {
        String[] notificacoes = new String[20];
        int index = 0;
        
        for (Map.Entry<medico, utente> entry : medicosEmAtendimento.entrySet()) {
            medico m = entry.getKey();
            utente u = entry.getValue();
            
            if (u != null) {
                double duracaoConsulta = calcularDuracaoConsulta(u.getUrgenciaCalculada());
                
                if (horaAtual - u.getHoraAtendimento() >= duracaoConsulta) {
                    notificacoes[index++] = "FIM_ATENDIMENTO|Utente " + u.getNome() + 
                        " terminou atendimento com Dr(a). " + m.getNome() + 
                        " (" + (int)duracaoConsulta + " unidade(s))";
                    medicosEmAtendimento.put(m, null);
                    
                    m.setHorasTrabalhadasConsecutivas(m.getHorasTrabalhadasConsecutivas() + duracaoConsulta);
                    
                    if (m.getHorasTrabalhadasConsecutivas() >= 5.0) {
                        double horasDescanso = Math.floor(m.getHorasTrabalhadasConsecutivas() / 5.0);
                        m.setHorasDescansoNecessarias(horasDescanso);
                        m.setEmDescanso(true);
                        medicosDisponiveis.put(m, false);
                        notificacoes[index++] = "MEDICO_DESCANSO|Dr(a). " + m.getNome() + 
                            " precisa de " + (int)horasDescanso + " unidade(s) de descanso (trabalhou " + 
                            (int)m.getHorasTrabalhadasConsecutivas() + " horas consecutivas).";
                    } else {
                        boolean dentroHorario = horaAtual >= m.getHoraEntrada() && horaAtual < m.getHoraSaida();
                        if (dentroHorario) {
                            medicosDisponiveis.put(m, true);
                        } else {
                            medicosDisponiveis.put(m, false);
                            notificacoes[index++] = "MEDICO_SAIU|Dr(a). " + m.getNome() + 
                                " saiu do serviço (terminou atendimento fora do horário).";
                        }
                    }
                }
            }
        }
        
        return compactarArray(notificacoes, index);
    }
    
    
    private String[] verificarMedicosForaHorario() {
        String[] notificacoes = new String[20];
        int index = 0;
        
        for (Map.Entry<medico, utente> entry : medicosEmAtendimento.entrySet()) {
            medico m = entry.getKey();
            utente u = entry.getValue();
            
            if (u != null && horaAtual >= m.getHoraSaida()) {
                double horasExtra = horaAtual - m.getHoraSaida();
                if (horasExtra == 0) {
                    notificacoes[index++] = "MEDICO_FORA_HORARIO|Dr(a). " + m.getNome() + 
                        " está fora do horário mas continua em atendimento com " + u.getNome();
                }
            }
        }
        
        return compactarArray(notificacoes, index);
    }
    
    public void registarUtente(String nome) {
        utente novoUtente = new utente(nome, horaAtual);
        filaEspera = adicionarAoArray(filaEspera, novoUtente);
    }
    
    public boolean chamarParaTriagem() {
        if (triagemOcupada) {
            return false;
        }
        
        if (filaEspera.length == 0) {
            return false;
        }
        
        utente utente = filaEspera[0];
        filaEspera = removerDoArray(filaEspera, 0);
        filaTriagem = adicionarAoArray(filaTriagem, utente);
        triagemOcupada = true;
        
        return true;
    }
    
    public String[] realizarTriagem(sintoma[] sintomas) {
        if (filaTriagem.length == 0) {
            return new String[]{"ERRO|Não há utente em triagem."};
        }
        
        utente utente = filaTriagem[0];
        utente.setSintomas(sintomas);
        utente.setHoraTriagem(horaAtual);
        
        calcularUrgenciaEEspecialidade(utente);
        
        utente.setTriado(true);
        filaTriagem = removerDoArray(filaTriagem, 0);
        triagemOcupada = false;
        
        String[] resultado = new String[5];
        resultado[0] = "TRIAGEM_CONCLUIDA|" + utente.getNome();
        resultado[1] = "URGENCIA|" + utente.getUrgenciaCalculada();
        resultado[2] = "ESPECIALIDADE|" + (utente.getEspecialidadeCalculada() != null ? 
            utente.getEspecialidadeCalculada().getNome() : "Não determinada");
        
        boolean encaminhado = encaminharAutomaticamente(utente);
        
        if (!encaminhado) {
            filaEspera = adicionarAoArray(filaEspera, utente);
            resultado[3] = "AGUARDA|Utente aguarda na sala de espera.";
        } else {
            resultado[3] = "ENCAMINHADO|Utente encaminhado automaticamente.";
        }
        
        return resultado;
    }
    
    private void calcularUrgenciaEEspecialidade(utente utente) {
        sintoma[] sintomas = utente.getSintomas();
        
        if (sintomas == null || sintomas.length == 0) {
            return;
        }
        
        nivelUrgencia maxUrgencia = nivelUrgencia.VERDE;
        for (sintoma s : sintomas) {
            if (s != null && s.getUrgencia().getNivelUrgencia() > maxUrgencia.getNivelUrgencia()) {
                maxUrgencia = s.getUrgencia();
            }
        }
        utente.setUrgenciaCalculada(maxUrgencia);
        
        Map<especialidade, Integer> contagemPorUrgencia = new HashMap<>();
        
        for (sintoma s : sintomas) {
            if (s != null && s.getEspecialidades() != null) {
                for (especialidade esp : s.getEspecialidades()) {
                    if (esp != null) {
                        if (s.getUrgencia() == nivelUrgencia.VERMELHA) {
                            contagemPorUrgencia.put(esp, contagemPorUrgencia.getOrDefault(esp, 0) + 100);
                        } else if (s.getUrgencia() == nivelUrgencia.LARANJA) {
                            contagemPorUrgencia.put(esp, contagemPorUrgencia.getOrDefault(esp, 0) + 10);
                        } else {
                            contagemPorUrgencia.put(esp, contagemPorUrgencia.getOrDefault(esp, 0) + 1);
                        }
                    }
                }
            }
        }
        
        if (!contagemPorUrgencia.isEmpty()) {
            especialidade especialidadeMaisProvavel = null;
            int maxContagem = 0;
            
            for (Map.Entry<especialidade, Integer> entry : contagemPorUrgencia.entrySet()) {
                if (entry.getValue() > maxContagem) {
                    maxContagem = entry.getValue();
                    especialidadeMaisProvavel = entry.getKey();
                }
            }
            
            utente.setEspecialidadeCalculada(especialidadeMaisProvavel);
        }
    }
    
    private boolean encaminharAutomaticamente(utente utente) {
        if (utente.getEspecialidadeCalculada() == null) {
            return false;
        }
        
        medico medicoDisponivel = encontrarMedicoDisponivel(utente.getEspecialidadeCalculada());
        
        if (medicoDisponivel != null) {
            atribuirMedico(utente, medicoDisponivel);
            return true;
        }
        
        return false;
    }
    
    private String[] verificarEscalamentoUrgencia() {
        String[] notificacoes = new String[50];
        int index = 0;
        
        utente[] utentesParaRemover = new utente[filaEspera.length];
        int removeIndex = 0;
        
        for (utente u : filaEspera) {
            if (u != null && u.isTriado() && u.getUrgenciaCalculada() != null) {
                double tempoEspera = horaAtual - u.getHoraTriagem();
                
                if (u.getUrgenciaCalculada() == nivelUrgencia.VERDE && tempoEspera >= 3.0) {
                    u.setUrgenciaCalculada(nivelUrgencia.LARANJA);
                    u.setHoraTriagem(horaAtual);
                    notificacoes[index++] = "ESCALAMENTO_LARANJA|Utente " + u.getNome() + 
                        " escalou de VERDE para LARANJA (tempo de espera: " + (int)tempoEspera + " unidades)";
                } else if (u.getUrgenciaCalculada() == nivelUrgencia.LARANJA && tempoEspera >= 3.0) {
                    u.setUrgenciaCalculada(nivelUrgencia.VERMELHA);
                    u.setHoraTriagem(horaAtual);
                    notificacoes[index++] = "ESCALAMENTO_VERMELHA|Utente " + u.getNome() + 
                        " escalou de LARANJA para VERMELHA (tempo de espera: " + (int)tempoEspera + " unidades)";
                } else if (u.getUrgenciaCalculada() == nivelUrgencia.VERMELHA && tempoEspera >= 2.0) {
                    notificacoes[index++] = "SAIDA_URGENCIA|🚨 CRÍTICO: Utente " + u.getNome() + 
                        " deve sair da urgência! (tempo de espera crítico: " + (int)tempoEspera + " unidades)";
                    utentesParaRemover[removeIndex++] = u;
                }
            }
        }
        
        for (int i = 0; i < removeIndex; i++) {
            filaEspera = removerUtenteDoArray(filaEspera, utentesParaRemover[i]);
        }
        
        return compactarArray(notificacoes, index);
    }
    
    private String[] gerenciarDescansoMedicos() {
        String[] notificacoes = new String[20];
        int index = 0;
        
        for (Map.Entry<medico, Boolean> entry : medicosDisponiveis.entrySet()) {
            medico m = entry.getKey();
            
            if (m.isEmDescanso() && m.getHorasDescansoNecessarias() > 0) {
                m.setHorasDescansoNecessarias(m.getHorasDescansoNecessarias() - 1);
                
                if (m.getHorasDescansoNecessarias() <= 0) {
                    m.setEmDescanso(false);
                    m.setHorasTrabalhadasConsecutivas(0);
                    
                    boolean dentroHorario = horaAtual >= m.getHoraEntrada() && horaAtual < m.getHoraSaida();
                    if (dentroHorario && medicosEmAtendimento.get(m) == null) {
                        medicosDisponiveis.put(m, true);
                        notificacoes[index++] = "MEDICO_DISPONIVEL|Dr(a). " + m.getNome() + 
                            " terminou descanso e está DISPONÍVEL.";
                    }
                }
            }
        }
        
        return compactarArray(notificacoes, index);
    }
    
    private double calcularDuracaoConsulta(nivelUrgencia urgencia) {
        if (urgencia == null) return 2.0;
        
        switch (urgencia) {
            case VERDE:
                return 1.0;
            case LARANJA:
                return 2.0;
            case VERMELHA:
                return 3.0;
            default:
                return 2.0;
        }
    }
    
    private medico encontrarMedicoDisponivel(especialidade esp) {
        for (Map.Entry<medico, Boolean> entry : medicosDisponiveis.entrySet()) {
            medico m = entry.getKey();
            if (entry.getValue() && 
                !m.isEmDescanso() &&
                m.getEspecialidade().getCodigo().equals(esp.getCodigo()) &&
                medicosEmAtendimento.get(m) == null) {
                return m;
            }
        }
        return null;
    }
    
    public boolean encaminharManualmente(String nomeUtente, String nomeMedico) {
        utente utente = null;
        int indexUtente = -1;
        
        for (int i = 0; i < filaEspera.length; i++) {
            if (filaEspera[i] != null && filaEspera[i].getNome().equalsIgnoreCase(nomeUtente)) {
                utente = filaEspera[i];
                indexUtente = i;
                break;
            }
        }
        
        if (utente == null || !utente.isTriado()) {
            return false;
        }
        
        medico medico = null;
        for (Map.Entry<medico, Boolean> entry : medicosDisponiveis.entrySet()) {
            if (entry.getKey().getNome().equalsIgnoreCase(nomeMedico)) {
                medico = entry.getKey();
                break;
            }
        }
        
        if (medico == null || !medicosDisponiveis.get(medico) || medicosEmAtendimento.get(medico) != null) {
            return false;
        }
        
        filaEspera = removerDoArray(filaEspera, indexUtente);
        atribuirMedico(utente, medico);
        return true;
    }
    
    private void atribuirMedico(utente utente, medico medico) {
        utente.setMedicoAtribuido(medico);
        utente.setHoraAtendimento(horaAtual);
        medicosEmAtendimento.put(medico, utente);
        medicosDisponiveis.put(medico, false);
    }
    
    public String[] listarUtentes() {
        String[] resultado = new String[filaEspera.length + filaTriagem.length + medicosEmAtendimento.size() + 10];
        int index = 0;
        
        resultado[index++] = "FILA_ESPERA|" + filaEspera.length;
        for (int i = 0; i < filaEspera.length; i++) {
            utente u = filaEspera[i];
            resultado[index++] = "UTENTE_ESPERA|" + (i + 1) + "|" + u.getNome() + "|" + 
                (u.isTriado() ? "Sim" : "Não") + "|" + 
                (u.isTriado() ? u.getUrgenciaCalculada().toString() : "N/A");
        }
        
        resultado[index++] = "TRIAGEM|" + filaTriagem.length;
        for (utente u : filaTriagem) {
            if (u != null) {
                resultado[index++] = "UTENTE_TRIAGEM|" + u.getNome();
            }
        }
        
        resultado[index++] = "ATENDIMENTO|";
        for (Map.Entry<medico, utente> entry : medicosEmAtendimento.entrySet()) {
            if (entry.getValue() != null) {
                resultado[index++] = "UTENTE_ATENDIMENTO|" + entry.getValue().getNome() + 
                    "|" + entry.getKey().getNome();
            }
        }
        
        return compactarArray(resultado, index);
    }
    
    public String[] listarMedicos() {
        String[] resultado = new String[medicosDisponiveis.size() + 1];
        int index = 0;
        
        for (Map.Entry<medico, Boolean> entry : medicosDisponiveis.entrySet()) {
            medico m = entry.getKey();
            String status;
            
            if (horaAtual < m.getHoraEntrada() || horaAtual >= m.getHoraSaida()) {
                status = "Fora de serviço";
            } else if (medicosEmAtendimento.get(m) != null) {
                status = "Em atendimento (" + medicosEmAtendimento.get(m).getNome() + ")";
            } else if (entry.getValue()) {
                status = "Disponível";
            } else {
                status = "Indisponível";
            }
            
            resultado[index++] = "MEDICO|" + m.getNome() + "|" + m.getEspecialidade().getNome() + 
                "|" + m.getHoraEntrada() + "-" + m.getHoraSaida() + "|" + status;
        }
        
        return compactarArray(resultado, index);
    }
    
    public double getHoraAtual() {
        return horaAtual;
    }
    
    public int getDiaAtual() {
        return diaAtual;
    }
    
    public boolean isTriagemOcupada() {
        return triagemOcupada;
    }
    
    public utente getUtenteEmTriagem() {
        return filaTriagem.length > 0 ? filaTriagem[0] : null;
    }
    
    public int getNumeroUtentesEspera() {
        return filaEspera.length;
    }
    
    private utente[] adicionarAoArray(utente[] array, utente elemento) {
        utente[] novoArray = new utente[array.length + 1];
        System.arraycopy(array, 0, novoArray, 0, array.length);
        novoArray[array.length] = elemento;
        return novoArray;
    }
    
    private utente[] removerDoArray(utente[] array, int index) {
        if (index < 0 || index >= array.length) {
            return array;
        }
        utente[] novoArray = new utente[array.length - 1];
        System.arraycopy(array, 0, novoArray, 0, index);
        System.arraycopy(array, index + 1, novoArray, index, array.length - index - 1);
        return novoArray;
    }
    
    private utente[] removerUtenteDoArray(utente[] array, utente utente) {
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == utente) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            return removerDoArray(array, index);
        }
        return array;
    }
    
    private String[] compactarArray(String[] array, int tamanho) {
        String[] resultado = new String[tamanho];
        System.arraycopy(array, 0, resultado, 0, tamanho);
        return resultado;
    }
}
