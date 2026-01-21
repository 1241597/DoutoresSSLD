package com.example.lp1.BLL;

import com.example.lp1.Model.Especialidade;
import com.example.lp1.Model.Sintoma;
import com.example.lp1.Utils.Enums.nivelUrgencia;

import java.util.HashMap;
import java.util.Map;

/**
 * Gerencia a lógica de triagem de utentes
 * Calcula urgência e especialidade com base nos sintomas
 */
public class TriagemBLL {

    /**
     * Calcula o nível de urgência mais alto dos sintomas
     */
    public nivelUrgencia calcularNivelUrgencia(Sintoma[] sintomas) {
        if (sintomas == null || sintomas.length == 0) {
            return nivelUrgencia.VERDE; // Padrão: nível mais baixo
        }

        nivelUrgencia maiorUrgencia = nivelUrgencia.VERDE;

        for (Sintoma s : sintomas) {
            if (s != null && s.getUrgencia() != null) {
                if (s.getUrgencia().getNivelUrgencia() > maiorUrgencia.getNivelUrgencia()) {
                    maiorUrgencia = s.getUrgencia();
                }
            }
        }

        return maiorUrgencia;
    }

    /**
     * Calcula a especialidade com base nos sintomas
     * Regras:
     * 1. Se há sintoma VERMELHO, sua especialidade tem prioridade
     * 2. Caso contrário, conta sintomas por especialidade por nível de urgência
     * 3. Retorna a especialidade com mais sintomas, ou null se indeterminado
     */
    public Especialidade calcularEspecialidade(Sintoma[] sintomas) {
        if (sintomas == null || sintomas.length == 0) {
            return null;
        }

        // Primeiro: verificar se há sintomas VERMELHOS
        for (Sintoma s : sintomas) {
            if (s != null && s.getUrgencia() == nivelUrgencia.VERMELHA) {
                // Pegar a primeira especialidade do sintoma vermelho
                Especialidade[] esps = s.getEspecialidades();
                if (esps != null && esps.length > 0 && esps[0] != null) {
                    return esps[0]; // Prioridade ao sintoma urgente
                }
            }
        }

        // Segundo: contar todas as especialidades por urgência
        // Mapa: Código da Especialidade -> Contagem
        Map<String, Integer> contagemEspecialidades = new HashMap<>();

        for (Sintoma s : sintomas) {
            if (s != null && s.getEspecialidades() != null) {
                for (Especialidade esp : s.getEspecialidades()) {
                    if (esp != null) {
                        String codigo = esp.getCodigo();
                        contagemEspecialidades.put(codigo,
                                contagemEspecialidades.getOrDefault(codigo, 0) + 1);
                    }
                }
            }
        }

        // Se não há especialidades, retorna null
        if (contagemEspecialidades.isEmpty()) {
            return null;
        }

        // Encontrar a especialidade com maior contagem
        String codigoMaior = null;
        int maiorContagem = 0;

        for (Map.Entry<String, Integer> entry : contagemEspecialidades.entrySet()) {
            if (entry.getValue() > maiorContagem) {
                maiorContagem = entry.getValue();
                codigoMaior = entry.getKey();
            }
        }

        // Retornar a especialidade encontrada (pegar do primeiro sintoma que a contém)
        if (codigoMaior != null) {
            for (Sintoma s : sintomas) {
                if (s != null && s.getEspecialidades() != null) {
                    for (Especialidade esp : s.getEspecialidades()) {
                        if (esp != null && esp.getCodigo().equals(codigoMaior)) {
                            return esp;
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Sugere especialidade baseada em estatística (regra dos 80%)
     * Analisa sintomas e verifica se 80% ou mais apontam para uma especialidade
     * Retorna mensagem sugestiva, ou null se não aplicável
     */
    public String sugerirEspecialidadePorEstatistica(Sintoma[] sintomas, Especialidade[] especialidadesDisponiveis) {
        if (sintomas == null || sintomas.length == 0) {
            return null;
        }

        // Contar ocorrências de cada especialidade
        Map<String, Integer> contagem = new HashMap<>();
        int totalSintomas = 0;

        for (Sintoma s : sintomas) {
            if (s != null && s.getEspecialidades() != null) {
                for (Especialidade esp : s.getEspecialidades()) {
                    if (esp != null) {
                        String codigo = esp.getCodigo();
                        contagem.put(codigo, contagem.getOrDefault(codigo, 0) + 1);
                    }
                }
                totalSintomas++;
            }
        }

        // Verificar se alguma especialidade aparece em 80% ou mais dos sintomas
        for (Map.Entry<String, Integer> entry : contagem.entrySet()) {
            double percentagem = (entry.getValue() * 100.0) / totalSintomas;
            if (percentagem >= 80.0) {
                return "Sugestão: " + percentagem + "% dos sintomas indicam especialidade " + entry.getKey();
            }
        }

        return null;
    }
}
