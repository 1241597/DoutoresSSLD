package com.example.lp1.BLL;

/**
 * Gerencia a simulação de tempo no sistema hospitalar
 * Tempo discreto de 1 a 24 unidades, com avanço manual
 */
public class SimuladorBLL {
    private int horaAtual;

    public SimuladorBLL() {
        this.horaAtual = 1; // Começa na hora 1
    }

    /**
     * Avança o tempo em uma unidade
     * Retorna para 1 após 24
     */
    public void avancarTempo() {
        horaAtual++;
        if (horaAtual > 24) {
            horaAtual = 1;
        }
    }

    /**
     * Retorna a hora atual do simulador
     */
    public int getHoraAtual() {
        return horaAtual;
    }

    /**
     * Reseta o dia para hora 1
     */
    public void resetarDia() {
        horaAtual = 1;
    }

    /**
     * Define manualmente a hora atual (útil para testes)
     */
    public void setHoraAtual(int hora) {
        if (hora >= 1 && hora <= 24) {
            this.horaAtual = hora;
        }
    }
}
