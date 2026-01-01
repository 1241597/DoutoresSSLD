package com.example.lp1.Model;

import com.example.lp1.Utils.Enums; // Importar o Enum
import com.example.lp1.Utils.Enums.nivelUrgencia; // Importar para usar o nome curto
import java.util.Arrays;

public class sintoma {
    private String nome;
    private nivelUrgencia urgencia; // Agora é um Enum, não um int
    private especialidade[] especialidades;

    public sintoma(String nome, nivelUrgencia urgencia, especialidade[] especialidades) {
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

    public especialidade[] getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(especialidade[] especialidades) {
        this.especialidades = especialidades;
    }

    @Override
    public String toString() {
        return "Sintoma: " + nome +
                " | Urgência: " + urgencia + " (" + urgencia.getNivelUrgencia() + ")" +
                " | Especialidades: " + Arrays.toString(especialidades);
    }
}