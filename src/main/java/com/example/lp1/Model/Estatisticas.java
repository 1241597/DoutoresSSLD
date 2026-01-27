package com.example.lp1.Model;

public class Estatisticas {

    private int totalDias = 0;
    private int totalUtentesAtendidos = 0;

    private String[] medicos = new String[200];
    private double[] salariosDia = new double[200];
    private int totalMedicos = 0;

    private String[] sintomas = new String[1000];
    private int[] contagemSintomas = new int[1000];
    private int totalSintomas = 0;

    private String[] especialidades = new String[200];
    private int[] contagemEspecialidades = new int[200];
    private int totalEspecialidades = 0;

    // CONSTRUTOR
    public Estatisticas() {
    }

    // MÉTODOS para média diaria
    public void incDias() {
        totalDias++;
    }

    public void addUtentes(int n) {
        totalUtentesAtendidos += n;
    }

    public int getTotalDias() {
        return totalDias;
    }

    public int getTotalUtentesAtendidos() {
        return totalUtentesAtendidos;
    }

    // MÉDICOS
    public String[] getMedicos() {
        return medicos;
    }

    public double[] getSalariosDia() {
        return salariosDia;
    }

    public int getTotalMedicos() {
        return totalMedicos;
    }

    public void incTotalMedicos() {
        totalMedicos++;
    }

    // SINTOMAS
    public String[] getSintomas() {
        return sintomas;
    }

    public int[] getContagemSintomas() {
        return contagemSintomas;
    }

    public int getTotalSintomas() {
        return totalSintomas;
    }

    public void incTotalSintomas() {
        totalSintomas++;
    }

    // ESPECIALIDADES
    public String[] getEspecialidades() {
        return especialidades;
    }

    public int[] getContagemEspecialidades() {
        return contagemEspecialidades;
    }

    public int getTotalEspecialidades() {
        return totalEspecialidades;
    }

    public void incTotalEspecialidades() {
        totalEspecialidades++;
    }
}
