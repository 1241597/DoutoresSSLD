package com.example.lp1.Model;

public class Medico {
    private String nome;
    private Especialidade especialidade;
    private double horaEntrada;
    private double horaSaida;
    private double salarioHora;
    private boolean disponivel;
    private Utente utenteAtual;
    private int horaInicioAtendimento;
    private int horaFimPrevista; // Hora prevista para terminar o atendimento
    private int[][] intervalosDescanso; // Intervalos de descanso [inicio, fim]

    public Medico(String nome, Especialidade especialidade, double horaEntrada, double horaSaida, double salarioHora) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.salarioHora = salarioHora;
        this.disponivel = false;
        this.utenteAtual = null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public double getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(double horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public double getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(double horaSaida) {
        this.horaSaida = horaSaida;
    }

    public double getSalarioHora() {
        return salarioHora;
    }

    public void setSalarioHora(double salarioHora) {
        this.salarioHora = salarioHora;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Utente getUtenteAtual() {
        return utenteAtual;
    }

    public void setUtenteAtual(Utente utenteAtual) {
        this.utenteAtual = utenteAtual;
    }

    public int getHoraInicioAtendimento() {
        return horaInicioAtendimento;
    }

    public void setHoraInicioAtendimento(int horaInicioAtendimento) {
        this.horaInicioAtendimento = horaInicioAtendimento;
    }

    /**
     * Verifica se o médico deve estar disponível na hora especificada
     * baseado no seu horário de trabalho e intervalos de descanso
     */
    public boolean isDisponivelNaHora(int hora) {
        // Verifica se está dentro do horário de trabalho
        if (hora < horaEntrada || hora > horaSaida) {
            return false;
        }

        // Verifica se está em intervalo de descanso
        if (intervalosDescanso != null) {
            for (int[] intervalo : intervalosDescanso) {
                if (intervalo != null && intervalo.length == 2) {
                    int inicio = intervalo[0];
                    int fim = intervalo[1];
                    if (hora >= inicio && hora < fim) {
                        return false; // Está em intervalo de descanso
                    }
                }
            }
        }

        return true;
    }

    /**
     * Inicia atendimento de um utente
     */
    public void iniciarAtendimento(Utente utente, int horaAtual, int duracao) {
        this.utenteAtual = utente;
        this.disponivel = false;
        this.horaInicioAtendimento = horaAtual;
        this.horaFimPrevista = horaAtual + duracao;
    }

    /**
     * Finaliza o atendimento atual
     */
    public void finalizarAtendimento() {
        this.utenteAtual = null;
        this.disponivel = true;
        this.horaInicioAtendimento = 0;
        this.horaFimPrevista = 0;
    }

    public int getHoraFimPrevista() {
        return horaFimPrevista;
    }

    public int[][] getIntervalosDescanso() {
        return intervalosDescanso;
    }

    public void setIntervalosDescanso(int[][] intervalosDescanso) {
        this.intervalosDescanso = intervalosDescanso;
    }

    @Override
    public String toString() {
        return "Médico{" +
                "nome='" + nome + '\'' +
                ", especialidade=" + especialidade.getCodigo() +
                ", horaEntrada=" + horaEntrada +
                ", horaSaida=" + horaSaida +
                ", disponível=" + disponivel +
                ", atendendo=" + (utenteAtual != null ? utenteAtual.getNome() : "ninguém") +
                '}';
    }
}
