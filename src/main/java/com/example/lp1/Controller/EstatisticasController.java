package com.example.lp1.Controller;

import com.example.lp1.BLL.EstatisticasBLL;
import com.example.lp1.Model.Estatisticas;

public class EstatisticasController {

    private EstatisticasBLL bll;

    public EstatisticasController(Estatisticas estatisticas) {
        bll = new EstatisticasBLL(estatisticas);
    }

    // USADO PELA VIEW
    public double obterMediaDiaria() {
        return bll.calcularMediaDiaria();
    }

    public Estatisticas obterEstatisticas() {
        return bll.getEstatisticas();
    }

    // USADO POR OUTROS MÓDULOS
    public void registarDia(int utentesAtendidos) {
        bll.registarDia(utentesAtendidos);
    }

    public void registarSalarioMedico(String nome, double horas, double valorHora) {
        bll.registarSalarioMedico(nome, horas, valorHora);
    }

    public void registarSintoma(String nome) {
        bll.registarSintoma(nome);
    }

    public void registarEspecialidade(String nome) {
        bll.registarEspecialidade(nome);
    }
}
