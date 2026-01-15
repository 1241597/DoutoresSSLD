package com.example.lp1.Model;

import com.example.lp1.Utils.Enums.nivelUrgencia;

public class utente {
    private String nome;
    private sintoma[] sintomas;
    private boolean triado;
    private nivelUrgencia urgenciaCalculada;
    private especialidade especialidadeCalculada;
    private medico medicoAtribuido;
    private double horaChegada;
    private double horaTriagem;
    private double horaAtendimento;

    public utente(String nome, double horaChegada) {
        this.nome = nome;
        this.sintomas = new sintoma[0];
        this.triado = false;
        this.urgenciaCalculada = null;
        this.especialidadeCalculada = null;
        this.medicoAtribuido = null;
        this.horaChegada = horaChegada;
        this.horaTriagem = -1;
        this.horaAtendimento = -1;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public sintoma[] getSintomas() {
        return sintomas;
    }

    public void setSintomas(sintoma[] sintomas) {
        this.sintomas = sintomas;
    }

    public boolean isTriado() {
        return triado;
    }

    public void setTriado(boolean triado) {
        this.triado = triado;
    }

    public nivelUrgencia getUrgenciaCalculada() {
        return urgenciaCalculada;
    }

    public void setUrgenciaCalculada(nivelUrgencia urgenciaCalculada) {
        this.urgenciaCalculada = urgenciaCalculada;
    }

    public especialidade getEspecialidadeCalculada() {
        return especialidadeCalculada;
    }

    public void setEspecialidadeCalculada(especialidade especialidadeCalculada) {
        this.especialidadeCalculada = especialidadeCalculada;
    }

    public medico getMedicoAtribuido() {
        return medicoAtribuido;
    }

    public void setMedicoAtribuido(medico medicoAtribuido) {
        this.medicoAtribuido = medicoAtribuido;
    }

    public double getHoraChegada() {
        return horaChegada;
    }

    public void setHoraChegada(double horaChegada) {
        this.horaChegada = horaChegada;
    }

    public double getHoraTriagem() {
        return horaTriagem;
    }

    public void setHoraTriagem(double horaTriagem) {
        this.horaTriagem = horaTriagem;
    }

    public double getHoraAtendimento() {
        return horaAtendimento;
    }

    public void setHoraAtendimento(double horaAtendimento) {
        this.horaAtendimento = horaAtendimento;
    }

    @Override
    public String toString() {
        return "utente{" +
                "nome='" + nome + '\'' +
                ", triado=" + triado +
                ", urgencia=" + (urgenciaCalculada != null ? urgenciaCalculada : "N/A") +
                ", especialidade=" + (especialidadeCalculada != null ? especialidadeCalculada.getNome() : "N/A") +
                ", medico=" + (medicoAtribuido != null ? medicoAtribuido.getNome() : "N/A") +
                ", horaChegada=" + horaChegada +
                '}';
    }
}
