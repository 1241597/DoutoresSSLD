package com.example.lp1.Model;

public class Medico {
    private String nome;
    private Especialidade especialidade;
    private double horaEntrada;
    private double horaSaida;
    private double salarioHora;

    public Medico(String nome, Especialidade especialidade, double horaEntrada, double horaSaida, double salarioHora) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.salarioHora = salarioHora;
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

    @Override
    public String toString() {
        return "medico{" +
                "nome='" + nome + '\'' +
                ", especialidade=" + especialidade +
                ", horaEntrada=" + horaEntrada +
                ", horaSaida=" + horaSaida +
                ", salarioHora=" + salarioHora +
                '}';
    }
}
