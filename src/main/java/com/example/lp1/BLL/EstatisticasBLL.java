package com.example.lp1.BLL;

import com.example.lp1.Model.Estatisticas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EstatisticasBLL {

    private Estatisticas estatisticas;

    public EstatisticasBLL(Estatisticas estatisticas) {
        this.estatisticas = estatisticas;
    }

    // --- MÉDIA DIÁRIA
    public void registarDia(int utentesAtendidos) {
        // Before incrementing dias, snapshot current daily salaries so they represent the day that finished
        estatisticas.snapshotSalariosDoDia();

        estatisticas.incDias();
        estatisticas.addUtentes(utentesAtendidos);
        // marcar que este dia foi completado na simulação (útil para mostrar apenas dias completos)
        estatisticas.incDiasSimulados();

        // Reset the per-day accumulators so next day's salaries start from zero
        estatisticas.resetSalariosDiaAtual();
    }

    public double calcularMediaDiaria() {
        int diasContagem = estatisticas.getDiasSimulados();
        if (diasContagem <= 0) {
            diasContagem = Math.max(1, estatisticas.getTotalDias());
        }
        return (double) estatisticas.getTotalUtentesAtendidos() / diasContagem;
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
        if (nome == null) return;
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
        if (nome == null) return;
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

    // Carrega dados dos ficheiros e popula Estatisticas
    public void carregarDados() {
        Map<String, String> codigoParaNome = new HashMap<>();

        // Reset previous data to make method idempotent
        estatisticas.resetMedicosData();
        estatisticas.resetSintomasData();
        estatisticas.resetEspecialidadesData();
        // Note: not resetting diasSimulados because it represents runtime-simulated completed days

        // 1) carregar especialidades (código -> nome)
        try {
            List<String> linhas = Files.readAllLines(Paths.get("Ficheiros/especialidades.txt"));
            for (String linha : linhas) {
                if (linha == null) continue;
                linha = linha.trim();
                if (linha.isEmpty()) continue;
                String[] parts = linha.split(";");
                if (parts.length >= 2) {
                    String codigo = parts[0].trim();
                    String nome = parts[1].trim();
                    codigoParaNome.put(codigo, nome);
                }
            }
        } catch (IOException e) {
            System.out.println("[Estatisticas] Erro ao ler Ficheiros/especialidades.txt: " + e.getMessage());
        }

        // 2) carregar médicos e registar salários (horaFim - horaInicio) * valorHora
        try {
            List<String> linhas = Files.readAllLines(Paths.get("Ficheiros/medicos.txt"));
            for (String linha : linhas) {
                if (linha == null) continue;
                linha = linha.trim();
                if (linha.isEmpty()) continue;
                String[] parts = linha.split(";");
                if (parts.length >= 5) {
                    String nome = parts[0].trim();
                    // parts[1] é especialidade mas não é necessária aqui
                    try {
                        double inicio = Double.parseDouble(parts[2].trim());
                        double fim = Double.parseDouble(parts[3].trim());
                        double valorHora = Double.parseDouble(parts[4].trim());
                        double horas = fim - inicio;
                        if (horas < 0) horas = 0;
                        registarSalarioMedico(nome, horas, valorHora);
                    } catch (NumberFormatException ex) {
                        // ignora linha mal formatada
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("[Estatisticas] Erro ao ler Ficheiros/medicos.txt: " + e.getMessage());
        }

        // 3) carregar utentes: registar sintomas e especialidades; contar dias e total utentes
        int totalUtentes = 0;
        Set<String> diasUnicos = new HashSet<>();
        try {
            List<String> linhas = Files.readAllLines(Paths.get("Ficheiros/utentes.txt"));
            for (String linha : linhas) {
                if (linha == null) continue;
                linha = linha.trim();
                if (linha.isEmpty()) continue;
                String[] parts = linha.split("\\|");
                if (parts.length >= 2) {
                    String sintoma = parts[1].trim();
                    if (!sintoma.isEmpty()) {
                        registarSintoma(sintoma);
                    }
                }
                if (parts.length >= 4) {
                    String codigoEsp = parts[3].trim();
                    if (!codigoEsp.isEmpty()) {
                        String nomeEsp = codigoParaNome.getOrDefault(codigoEsp, codigoEsp);
                        registarEspecialidade(nomeEsp);
                    }
                }
                if (parts.length >= 5) {
                    diasUnicos.add(parts[4].trim());
                }
                totalUtentes++;
            }
        } catch (IOException e) {
            System.out.println("[Estatisticas] Erro ao ler Ficheiros/utentes.txt: " + e.getMessage());
        }

        // configurar dias e utentes nas estatisticas (deterministicamente, não acumulando)
        int dias = Math.max(1, diasUnicos.size()); // pelo menos 1 dia
        estatisticas.setTotalDias(dias);
        estatisticas.setTotalUtentesAtendidos(totalUtentes);
    }

    // GET
    public Estatisticas getEstatisticas() {
        return estatisticas;
    }
}
