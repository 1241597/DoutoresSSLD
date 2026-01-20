package com.example.lp1.BLL;

import com.example.lp1.Model.Especialidade;
import com.example.lp1.Model.Medico;
import com.example.lp1.Model.Utente;
import com.example.lp1.Utils.Enums.StatusUtente;

/**
 * Gerencia a atribuição de utentes a médicos
 * Lida com disponibilidade e atribuição automática/manual
 */
public class AtribuicaoBLL {

    /**
     * Encontra um médico disponível para a especialidade especificada
     * na hora atual
     */
    public Medico encontrarMedicoDisponivel(Especialidade especialidade, Medico[] medicos, int horaAtual) {
        if (especialidade == null || medicos == null) {
            return null;
        }

        for (Medico m : medicos) {
            if (m != null &&
                    m.getEspecialidade() != null &&
                    m.getEspecialidade().getCodigo().equals(especialidade.getCodigo()) &&
                    m.isDisponivel() &&
                    m.isDisponivelNaHora(horaAtual)) {
                return m;
            }
        }

        return null; // Não encontrou médico disponível
    }

    /**
     * Atribui automaticamente um utente a um médico disponível
     * Retorna true se conseguiu atribuir, false caso contrário
     */
    public boolean atribuirAutomaticamente(Utente utente, Medico[] medicos, int horaAtual, int duracao) {
        if (utente == null || utente.getEspecialidadeCalculada() == null) {
            return false; // Não pode atribuir automaticamente sem especialidade
        }

        Medico medicoDisponivel = encontrarMedicoDisponivel(
                utente.getEspecialidadeCalculada(),
                medicos,
                horaAtual);

        if (medicoDisponivel != null) {
            medicoDisponivel.iniciarAtendimento(utente, horaAtual, duracao);
            utente.setStatus(StatusUtente.COM_MEDICO);
            return true;
        }

        return false;
    }

    /**
     * Atribui manualmente um utente a um médico
     * Retorna true se conseguiu atribuir, false caso contrário
     */
    public boolean atribuirManualmente(Utente utente, Medico medico, int horaAtual, int duracao) {
        if (utente == null || medico == null) {
            return false;
        }

        if (!medico.isDisponivel() || !medico.isDisponivelNaHora(horaAtual)) {
            return false; // Médico não disponível
        }

        medico.iniciarAtendimento(utente, horaAtual, duracao);
        utente.setStatus(StatusUtente.COM_MEDICO);
        return true;
    }

    /**
     * Atualiza a disponibilidade de todos os médicos com base na hora atual
     * Médicos ficam disponíveis dentro do horário de trabalho
     * Médicos que estão atendendo não se tornam disponíveis automaticamente
     */
    public void atualizarDisponibilidade(Medico[] medicos, int horaAtual) {
        if (medicos == null) {
            return;
        }

        for (Medico m : medicos) {
            if (m != null) {
                // Se o médico já está atendendo, mantém indisponível
                if (m.getUtenteAtual() != null) {
                    continue;
                }

                // Verifica se está no horário de trabalho
                if (m.isDisponivelNaHora(horaAtual)) {
                    m.setDisponivel(true);
                } else {
                    m.setDisponivel(false);
                }
            }
        }
    }

    /**
     * Libera médico se ele terminou o atendimento ou passou da hora de saída
     * Médicos só saem após terminar o atendimento atual, mesmo se passar da hora
     */
    public void liberarMedicoSeNecessario(Medico medico, int horaAtual, boolean servicoConcluido) {
        if (medico == null) {
            return;
        }

        if (servicoConcluido) {
            medico.finalizarAtendimento();

            // Verifica se ainda está no horário de trabalho
            if (medico.isDisponivelNaHora(horaAtual)) {
                medico.setDisponivel(true);
            } else {
                medico.setDisponivel(false);
            }
        }
    }

    /**
     * Retorna array de médicos disponíveis na hora atual
     */
    public Medico[] listarMedicosDisponiveis(Medico[] medicos, int horaAtual) {
        if (medicos == null) {
            return new Medico[0];
        }

        // Contar quantos estão disponíveis
        int count = 0;
        for (Medico m : medicos) {
            if (m != null && m.isDisponivel() && m.isDisponivelNaHora(horaAtual)) {
                count++;
            }
        }

        // Criar array com tamanho exato
        Medico[] disponiveis = new Medico[count];
        int index = 0;
        for (Medico m : medicos) {
            if (m != null && m.isDisponivel() && m.isDisponivelNaHora(horaAtual)) {
                disponiveis[index++] = m;
            }
        }

        return disponiveis;
    }

    /**
     * Verifica e finaliza automaticamente atendimentos que já passaram da hora
     * prevista
     * Retorna array de nomes dos utentes finalizados
     */
    public String[] verificarEFinalizarAtendimentos(Medico[] medicos, int horaAtual) {
        String[] finalizadosTemp = new String[50]; // Temp array
        int count = 0;

        if (medicos == null) {
            return new String[0];
        }

        for (Medico m : medicos) {
            if (m != null && m.getUtenteAtual() != null) {
                // Verifica se já passou da hora prevista de fim
                if (horaAtual >= m.getHoraFimPrevista()) {
                    String nomeUtente = m.getUtenteAtual().getNome();
                    Utente utente = m.getUtenteAtual();

                    // Finalizar atendimento
                    utente.setStatus(StatusUtente.CONCLUIDO);
                    m.finalizarAtendimento();

                    // Verificar se médico ainda está no horário de trabalho
                    if (m.isDisponivelNaHora(horaAtual)) {
                        m.setDisponivel(true);
                    } else {
                        m.setDisponivel(false);
                    }

                    finalizadosTemp[count++] = nomeUtente + " (Dr. " + m.getNome() + ")";
                }
            }
        }

        // Criar array com tamanho exato
        String[] finalizados = new String[count];
        for (int i = 0; i < count; i++) {
            finalizados[i] = finalizadosTemp[i];
        }

        return finalizados;
    }
}
