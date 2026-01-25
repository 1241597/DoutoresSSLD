package com.example.lp1.BLL;

import com.example.lp1.Model.Estatisticas;

public class EstatisticasBLL {

    private Estatisticas estatisticas;

    public EstatisticasBLL(Estatisticas estatisticas) {
        this.estatisticas = estatisticas;
    }

    // --- MÉDIA DIÁRIA
    public void registarDia(int utentesAtendidos) {
        estatisticas.incDias();
        estatisticas.addUtentes(utentesAtendidos);
    }

    public double calcularMediaDiaria() {
        if (estatisticas.getTotalDias() == 0) return 0;
        return (double) estatisticas.getTotalUtentesAtendidos()
                / estatisticas.getTotalDias();
    }

    // --- SALÁRIOS POR MÉDICO
    public void registarSalarioMedico(String nomeMedico, double horas, double valorHora) {

        String[] medicos = estatisticas.getMedicos();
        double[] salarios = estatisticas.getSalariosDia();

        double salarioDia = horas * valorHora;

        for (int i = 0; i < estatisticas.getTotalMedicos(); i++) {
            if (medicos[i].equalsIgnoreCase(nomeMedico)) {
                salarios[i] += salarioDia;
                return;
            }
        }

        medicos[estatisticas.getTotalMedicos()] = nomeMedico;
        salarios[estatisticas.getTotalMedicos()] = salarioDia;
        estatisticas.incTotalMedicos();
    }


    // --- SINTOMAS
    public void registarSintoma(String nome) {
        String[] sintomas = estatisticas.getSintomas();
        int[] contagem = estatisticas.getContagemSintomas();

        for (int i = 0; i < estatisticas.getTotalSintomas(); i++) {
            if (sintomas[i].equalsIgnoreCase(nome)) {
                contagem[i]++;
                return;
            }
        }

        sintomas[estatisticas.getTotalSintomas()] = nome;
        contagem[estatisticas.getTotalSintomas()] = 1;
        estatisticas.incTotalSintomas();
    }

    // --- ESPECIALIDADES
    public void registarEspecialidade(String nome) {
        String[] esp = estatisticas.getEspecialidades();
        int[] cont = estatisticas.getContagemEspecialidades();

        for (int i = 0; i < estatisticas.getTotalEspecialidades(); i++) {
            if (esp[i].equalsIgnoreCase(nome)) {
                cont[i]++;
                return;
            }
        }

        esp[estatisticas.getTotalEspecialidades()] = nome;
        cont[estatisticas.getTotalEspecialidades()] = 1;
        estatisticas.incTotalEspecialidades();
    }

    // GET
    public Estatisticas getEstatisticas() {
        return estatisticas;
    }
}
