package com.example.lp1.Model;

public class medico {
    private String nome;
    private especialidade especialidade;
    private double horaEntrada;
    private double horaSaida;
    private double salarioHora;
    private double horasTrabalhadasConsecutivas;
    private double horasDescansoNecessarias;
    private boolean emDescanso;

    public medico(String nome, especialidade especialidade, double horaEntrada, double horaSaida, double salarioHora) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.salarioHora = salarioHora;
        this.horasTrabalhadasConsecutivas = 0;
        this.horasDescansoNecessarias = 0;
        this.emDescanso = false;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(especialidade especialidade) {
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

    public double getHorasTrabalhadasConsecutivas() {
        return horasTrabalhadasConsecutivas;
    }

    public void setHorasTrabalhadasConsecutivas(double horasTrabalhadasConsecutivas) {
        this.horasTrabalhadasConsecutivas = horasTrabalhadasConsecutivas;
    }

    public double getHorasDescansoNecessarias() {
        return horasDescansoNecessarias;
    }

    public void setHorasDescansoNecessarias(double horasDescansoNecessarias) {
        this.horasDescansoNecessarias = horasDescansoNecessarias;
    }

    public boolean isEmDescanso() {
        return emDescanso;
    }

    public void setEmDescanso(boolean emDescanso) {
        this.emDescanso = emDescanso;
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
