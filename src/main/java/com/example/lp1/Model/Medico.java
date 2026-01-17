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
     * baseado no seu horário de trabalho
     */
    public boolean isDisponivelNaHora(int hora) {
        return hora >= horaEntrada && hora <= horaSaida;
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
