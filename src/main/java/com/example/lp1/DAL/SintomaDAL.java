package com.example.lp1.DAL;

import com.example.lp1.Model.Sintoma;
import com.example.lp1.Model.Especialidade;
import com.example.lp1.Utils.Enums.nivelUrgencia; // Importar o Enum
import java.io.*;

public class SintomaDAL {

    private static final String CAMINHO_FICHEIRO = "Ficheiros/sintomas.txt";
    private static final String SEPARADOR = ";";

    public Sintoma[] carregarSintomas() {
        Sintoma[] sintomasTemp = new Sintoma[50];
        int index = 0;

        File file = new File(CAMINHO_FICHEIRO);
        if (!file.exists()) return new Sintoma[0];

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();

                if (!linha.isEmpty()) {
                    String[] partes = linha.split(SEPARADOR);

                    if (partes.length >= 3) {
                        if (index >= sintomasTemp.length) {
                            sintomasTemp = expandirArray(sintomasTemp);
                        }

                        String nome = partes[0].trim();
                        String textoCor = partes[1].trim(); // Ex: "Vermelha" ou "Laranja"

                        // --- CONVERSÃO PARA ENUM ---
                        // Convertemos o texto do ficheiro para MAIÚSCULAS para bater certo com o Enum
                        // "Vermelha" -> "VERMELHA" -> Enum.VERMELHA
                        nivelUrgencia urgenciaEnum;
                        try {
                            urgenciaEnum = nivelUrgencia.valueOf(textoCor.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            // Se a cor não existir no Enum, definimos uma padrão ou ignoramos
                            System.out.println("Cor desconhecida: " + textoCor);
                            continue;
                        }

                        // Ler especialidades (parte dinâmica)
                        int qtdEspecialidades = partes.length - 2;
                        Especialidade[] listaEsp = new Especialidade[qtdEspecialidades];

                        for (int i = 0; i < qtdEspecialidades; i++) {
                            String sigla = partes[i + 2].trim();
                            listaEsp[i] = new Especialidade(sigla, "Indefinido");
                        }

                        // Criar o objeto com o Enum
                        Sintoma s = new Sintoma(nome, urgenciaEnum, listaEsp);
                        sintomasTemp[index++] = s;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler sintomas: " + e.getMessage());
        }

        Sintoma[] resultado = new Sintoma[index];
        System.arraycopy(sintomasTemp, 0, resultado, 0, index);
        return resultado;
    }

    public void gravarSintomas(Sintoma[] lista) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_FICHEIRO, false))) {

            for (int i = 0; i < lista.length; i++) {
                if (lista[i] != null) {
                    Sintoma s = lista[i];

                    // --- CONVERSÃO DO ENUM PARA TEXTO BONITO ---
                    // O enum devolve "VERMELHA". Nós queremos "Vermelha" (para o ficheiro ficar igual)
                    String nomeEnum = s.getUrgencia().name();
                    String corFormatada = nomeEnum.charAt(0) + nomeEnum.substring(1).toLowerCase();

                    // Montar a linha
                    String linha = s.getNome() + SEPARADOR + corFormatada;

                    Especialidade[] esps = s.getEspecialidades();
                    if (esps != null) {
                        for (int j = 0; j < esps.length; j++) {
                            if (esps[j] != null) {
                                linha += SEPARADOR + esps[j].getCodigo();
                            }
                        }
                    }

                    writer.write(linha);
                    writer.newLine();
                }
            }
            System.out.println("Sintomas gravados com sucesso!");

        } catch (IOException e) {
            System.out.println("Erro ao gravar: " + e.getMessage());
        }
    }

    private Sintoma[] expandirArray(Sintoma[] original) {
        Sintoma[] novo = new Sintoma[original.length * 2];
        System.arraycopy(original, 0, novo, 0, original.length);
        return novo;
    }
}