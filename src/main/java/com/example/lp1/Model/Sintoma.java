package com.example.lp1.Model;

import com.example.lp1.Utils.Enums.nivelUrgencia; // Importar para usar o nome curto
import java.util.Arrays;

public class Sintoma {
    private String nome;
    private nivelUrgencia urgencia; // Agora é um Enum, não um int
    private Especialidade[] especialidades;

    public Sintoma(String nome, nivelUrgencia urgencia, Especialidade[] especialidades) {
        this.nome = nome;
        this.urgencia = urgencia;
        this.especialidades = especialidades;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Getter retorna o Enum
    public nivelUrgencia getUrgencia() {
        return urgencia;
    }

    public void setUrgencia(nivelUrgencia urgencia) {
        this.urgencia = urgencia;
    }

    public Especialidade[] getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(Especialidade[] especialidades) {
        this.especialidades = especialidades;
    }

    @Override
    public String toString() {
        return "Sintoma: " + nome +
                " | Urgência: " + urgencia + " (" + urgencia.getNivelUrgencia() + ")" +
                " | Especialidades: " + Arrays.toString(especialidades);
    }
}