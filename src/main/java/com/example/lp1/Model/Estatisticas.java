package com.example.lp1.Model;

public class Estatisticas {

    private int totalDias = 0;
    private int totalUtentesAtendidos = 0;

    // número de dias completos resultantes da simulação manual (incrementado por registarDia)
    private int diasSimulados = 0;

    private String[] medicos = new String[200];
    private double[] salariosDia = new double[200];
    private int totalMedicos = 0;

    // Salários gravados por dia (snapshot no fim de cada dia)
    // Substitui ArrayList por double[][] com contador para evitar uso de ArrayList
    private double[][] salariosPorDia = new double[1000][]; // suporta até 1000 dias por default
    private int totalDiasSnapshot = 0;

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

    public void incDiasSimulados() {
        diasSimulados++;
    }

    public int getDiasSimulados() {
        return diasSimulados;
    }

    // Snapshot do salário do dia atual (até totalMedicos) e adiciona à lista de dias
    public void snapshotSalariosDoDia() {
        double[] snapshot = new double[totalMedicos];
        for (int i = 0; i < totalMedicos; i++) {
            snapshot[i] = salariosDia[i];
        }
        // garantir espaço
        if (totalDiasSnapshot >= salariosPorDia.length) {
            // expandir array (duplicar tamanho)
            double[][] novo = new double[salariosPorDia.length * 2][];
            for (int i = 0; i < salariosPorDia.length; i++) novo[i] = salariosPorDia[i];
            salariosPorDia = novo;
        }
        salariosPorDia[totalDiasSnapshot++] = snapshot;
    }

    // Reset do acumulador de salarios do dia (preparar para o próximo dia)
    public void resetSalariosDiaAtual() {
        this.salariosDia = new double[200];
    }

    // Retorna um array com os snapshots gravados (cópia enxuta com o comprimento correto)
    public double[][] getSalariosPorDia() {
        if (totalDiasSnapshot == 0) return new double[0][];
        double[][] copia = new double[totalDiasSnapshot][];
        for (int i = 0; i < totalDiasSnapshot; i++) copia[i] = salariosPorDia[i];
        return copia;
    }

    // Helper to reset medicos/salarios when re-loading from files (avoids duplication)
    public void resetMedicosData() {
        this.medicos = new String[200];
        this.salariosDia = new double[200];
        this.totalMedicos = 0;
        this.salariosPorDia = new double[1000][]; // reset storage
        this.totalDiasSnapshot = 0;
    }

    // Helper to reset sintomas counters
    public void resetSintomasData() {
        this.sintomas = new String[1000];
        this.contagemSintomas = new int[1000];
        this.totalSintomas = 0;
    }

    // Helper to reset especialidades counters
    public void resetEspecialidadesData() {
        this.especialidades = new String[200];
        this.contagemEspecialidades = new int[200];
        this.totalEspecialidades = 0;
    }

    // Allow setting totalUtentes (used when reloading data)
    public void setTotalUtentesAtendidos(int n) {
        this.totalUtentesAtendidos = n;
    }

    // Allow setting totalDias (used only when previously zero)
    public void setTotalDias(int n) {
        this.totalDias = n;
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
