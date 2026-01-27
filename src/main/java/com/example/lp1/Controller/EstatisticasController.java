package com.example.lp1.Controller;

import com.example.lp1.BLL.EstatisticasBLL;
import com.example.lp1.Model.Estatisticas;

public class EstatisticasController {

    private EstatisticasBLL bll;

    // Default: carregar os ficheiros
    public EstatisticasController(Estatisticas estatisticas) {
        this(estatisticas, true);
    }

    // Permite controlar se deve carregar os ficheiros (evita duplo carregamento)
    public EstatisticasController(Estatisticas estatisticas, boolean carregarDados) {
        bll = new EstatisticasBLL(estatisticas);
        if (carregarDados) {
            // carregar dados dos ficheiros para popular as estatísticas
            bll.carregarDados();
        }
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
