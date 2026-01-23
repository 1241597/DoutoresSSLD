package com.example.lp1.Model;

public class Estatisticas {

    private int totalDias;
    private int totalUtentesAtendidos;

    private String[] medicos;
    private double[] salariosDia;
    private int totalMedicos;

    private String[] sintomas;
    private int[] contagemSintomas;
    private int totalSintomas;

    private String[] especialidades;
    private int[] contagemEspecialidades;
    private int totalEspecialidades;

    public Estatisticas() {
        totalDias = 0;
        totalUtentesAtendidos = 0;

        medicos = new String[50];
        salariosDia = new double[50];
        totalMedicos = 0;

        sintomas = new String[100];
        contagemSintomas = new int[100];
        totalSintomas = 0;

        especialidades = new String[20];
        contagemEspecialidades = new int[20];
        totalEspecialidades = 0;
    }

    // getters e setters
    public int getTotalDias() { return totalDias; }
    public void incDias() { totalDias++; }

    public String[] getMedicos() {return medicos;}
    public double[] getSalariosDia() {return salariosDia;}
    public int getTotalMedicos() {return totalMedicos;}
    public void incTotalMedicos() {totalMedicos++;}

    public int getTotalUtentesAtendidos() { return totalUtentesAtendidos; }
    public void addUtentes(int n) { totalUtentesAtendidos += n; }

    public String[] getSintomas() { return sintomas; }
    public int[] getContagemSintomas() { return contagemSintomas; }
    public int getTotalSintomas() { return totalSintomas; }
    public void incTotalSintomas() { totalSintomas++; }

    public String[] getEspecialidades() { return especialidades; }
    public int[] getContagemEspecialidades() { return contagemEspecialidades; }
    public int getTotalEspecialidades() { return totalEspecialidades; }
    public void incTotalEspecialidades() { totalEspecialidades++; }
}
