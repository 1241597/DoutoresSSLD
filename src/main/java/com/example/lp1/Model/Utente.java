package com.example.lp1.Model;

import com.example.lp1.Utils.Enums.nivelUrgencia;
import com.example.lp1.Utils.Enums.StatusUtente;

public class Utente {
    private String nome;
    private Sintoma[] sintomas;
    private nivelUrgencia nivelUrgenciaCalculado;
    private Especialidade especialidadeCalculada;
    private int horaChegada;
    private int horaTriagem;
    private StatusUtente status;

    // Construtor original mantido para compatibilidade
    public Utente(String nome, Sintoma[] sintomas) {
        this.nome = nome;
        this.sintomas = sintomas;
        this.status = StatusUtente.AGUARDANDO_TRIAGEM;
    }

    // Novo construtor para o simulador
    public Utente(String nome, int horaChegada) {
        this.nome = nome;
        this.sintomas = new Sintoma[0];
        this.horaChegada = horaChegada;
        this.status = StatusUtente.AGUARDANDO_TRIAGEM;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Sintoma[] getSintomas() {
        return sintomas;
    }

    public void setSintomas(Sintoma[] sintomas) {
        this.sintomas = sintomas;
    }

    public nivelUrgencia getNivelUrgenciaCalculado() {
        return nivelUrgenciaCalculado;
    }

    public void setNivelUrgenciaCalculado(nivelUrgencia nivelUrgenciaCalculado) {
        this.nivelUrgenciaCalculado = nivelUrgenciaCalculado;
    }

    public Especialidade getEspecialidadeCalculada() {
        return especialidadeCalculada;
    }

    public void setEspecialidadeCalculada(Especialidade especialidadeCalculada) {
        this.especialidadeCalculada = especialidadeCalculada;
    }

    public int getHoraChegada() {
        return horaChegada;
    }

    public void setHoraChegada(int horaChegada) {
        this.horaChegada = horaChegada;
    }

    public int getHoraTriagem() {
        return horaTriagem;
    }

    public void setHoraTriagem(int horaTriagem) {
        this.horaTriagem = horaTriagem;
    }

    public StatusUtente getStatus() {
        return status;
    }

    public void setStatus(StatusUtente status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "nome='" + nome + '\'' +
                ", urgência=" + (nivelUrgenciaCalculado != null ? nivelUrgenciaCalculado : "N/A") +
                ", especialidade=" + (especialidadeCalculada != null ? especialidadeCalculada.getCodigo() : "N/A") +
                ", status=" + (status != null ? status.getDescricao() : "N/A") +
                ", horaChegada=" + horaChegada +
                '}';
    }
}
