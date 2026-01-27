package com.example.lp1.BLL;

import com.example.lp1.Model.Medico;
import com.example.lp1.Model.Utente;

/**
 * Gerencia as notificações do sistema
 * Rastreia eventos temporais e mudanças de estado
 */
public class NotificacaoBLL {
    private String[] notificacoes;
    private int tamanho;
    private int horaAnterior;

    public NotificacaoBLL() {
        this.notificacoes = new String[10]; // Capacidade inicial
        this.tamanho = 0;
        this.horaAnterior = 0;
    }

    /**
     * Adiciona uma notificação ao array
     */
    public void adicionarNotificacao(String mensagem) {
        if (mensagem != null && !mensagem.isEmpty()) {
            // Expandir array se necessário
            if (tamanho >= notificacoes.length) {
                expandirArray();
            }
            notificacoes[tamanho++] = mensagem;
        }
    }

    /**
     * Expande o array de notificações dobrando sua capacidade
     */
    private void expandirArray() {
        String[] novoArray = new String[notificacoes.length * 2];
        for (int i = 0; i < notificacoes.length; i++) {
            novoArray[i] = notificacoes[i];
        }
        notificacoes = novoArray;
    }

    /**
     * Verifica eventos temporais após mudança de hora
     * Gera notificações sobre mudanças de disponibilidade de médicos
     */
    public void verificarEventosTemporais(Medico[] medicos, Utente[] utentes, int horaAtual) {
        // Notificar mudança de hora
        if (horaAtual != horaAnterior) {
            if (horaAtual == 1 && horaAnterior == 24) {
                adicionarNotificacao("Novo dia iniciado! Hora atual: " + horaAtual);
            } else {
                adicionarNotificacao("Tempo avançado. Hora atual: " + horaAtual);
            }
            horaAnterior = horaAtual;
        }

        // Verificar médicos que ficaram disponíveis ou indisponíveis
        if (medicos != null) {
            for (Medico m : medicos) {
                if (m != null) {
                    // Médico entrando no turno
                    if (m.getHoraEntrada() == horaAtual && m.getUtenteAtual() == null) {
                        adicionarNotificacao("Dr(a). " + m.getNome() +
                                " (" + m.getEspecialidade().getCodigo() + ") entrou em serviço");
                    }

                    // Médico saindo do turno (se não estiver atendendo)
                    if (m.getHoraSaida() == horaAtual && m.getUtenteAtual() == null) {
                        adicionarNotificacao("Dr(a). " + m.getNome() +
                                " (" + m.getEspecialidade().getCodigo() + ") terminou o turno");
                    }

                    // Médico ainda atendendo após hora de saída
                    if (horaAtual > m.getHoraSaida() && m.getUtenteAtual() != null) {
                        adicionarNotificacao("Dr(a). " + m.getNome() +
                                " continua atendendo " + m.getUtenteAtual().getNome() +
                                " após hora de saída");
                    }
                }
            }
        }

        // Verificar utentes esperando há muito tempo
        if (utentes != null) {
            for (Utente u : utentes) {
                if (u != null && u.getHoraChegada() > 0) {
                    // Ignorar utentes que já estão sendo atendidos ou concluídos
                    if (u.getStatus() == com.example.lp1.Utils.Enums.StatusUtente.COM_MEDICO ||
                            u.getStatus() == com.example.lp1.Utils.Enums.StatusUtente.CONCLUIDO) {
                        continue;
                    }

                    int tempoEspera = horaAtual - u.getHoraChegada();

                    // Alertar se urgente e esperando há mais de 2 horas
                    if (u.getNivelUrgenciaCalculado() != null &&
                            u.getNivelUrgenciaCalculado().getNivelUrgencia() == 3 &&
                            tempoEspera > 2) {
                        adicionarNotificacao("URGENTE: " + u.getNome() +
                                " aguarda há " + tempoEspera + " horas (nível VERMELHO)");
                    }

                    // Alertar se qualquer paciente espera há mais de 5 horas
                    if (tempoEspera > 5) {
                        adicionarNotificacao(u.getNome() +
                                " aguarda há " + tempoEspera + " horas");
                    }
                }
            }
        }
    }

    /**
     * Retorna todas as notificações acumuladas e limpa o array
     */
    public String[] obterELimparNotificacoes() {
        String[] copia = new String[tamanho];
        for (int i = 0; i < tamanho; i++) {
            copia[i] = notificacoes[i];
        }
        limparNotificacoes();
        return copia;
    }

    /**
     * Retorna as notificações sem limpar
     */
    public String[] obterNotificacoes() {
        String[] copia = new String[tamanho];
        for (int i = 0; i < tamanho; i++) {
            copia[i] = notificacoes[i];
        }
        return copia;
    }

    /**
     * Limpa todas as notificações
     */
    public void limparNotificacoes() {
        for (int i = 0; i < tamanho; i++) {
            notificacoes[i] = null;
        }
        tamanho = 0;
    }

    /**
     * Retorna o número de notificações pendentes
     */
    public int contarNotificacoes() {
        return tamanho;
    }
}
