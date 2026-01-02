package com.example.lp1.DAL;

import com.example.lp1.Model.medico;
import com.example.lp1.Model.especialidade;
import java.io.*;

public class medicoDAL {

    private static final String CAMINHO_FICHEIRO = "Ficheiros/medicos.txt";
    private static final String SEPARADOR = ";";

    /**
     * Carrega os médicos do ficheiro para um vetor.
     */
    public medico[] carregarMedicos() {
        // 1. Array inicial temporário
        medico[] medicosTemp = new medico[50];
        int index = 0;

        File file = new File(CAMINHO_FICHEIRO);
        if (!file.exists()) {
            return new medico[0];
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();

                if (!linha.isEmpty()) {
                    String[] partes = linha.split(SEPARADOR);

                    // O ficheiro tem: Nome;Sigla;HoraEnt;HoraSai;Salario
                    if (partes.length == 5) {

                        // Verificar se precisamos de aumentar o array
                        if (index >= medicosTemp.length) {
                            medicosTemp = expandirArray(medicosTemp);
                        }

                        // --- EXTRAÇÃO DOS DADOS ---
                        String nome = partes[0];
                        String codEspecialidade = partes[1];

                        // Conversão de String para Double
                        double hEntrada = Double.parseDouble(partes[2]);
                        double hSaida = Double.parseDouble(partes[3]);
                        double salario = Double.parseDouble(partes[4]);

                        // --- CRIAÇÃO DO OBJETO ESPECIALIDADE ---
                        // Como o ficheiro só tem o código "CARD", criamos um objeto com o código.
                        // O nome fica "Indefinido" porque não está neste ficheiro de texto.
                        especialidade espObj = new especialidade(codEspecialidade, "Indefinido");

                        // --- CRIAÇÃO DO OBJETO MEDICO ---
                        medico m = new medico(nome, espObj, hEntrada, hSaida, salario);

                        // Guardar no vetor
                        medicosTemp[index++] = m;

                    } else {
                        System.out.println("Linha com formato errado: " + linha);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao ler ficheiro: " + e.getMessage());
        }

        // Criar array final com tamanho exato
        medico[] resultado = new medico[index];
        System.arraycopy(medicosTemp, 0, resultado, 0, index);

        return resultado;
    }

    /**
     * Expande o array para o dobro do tamanho.
     */
    private medico[] expandirArray(medico[] original) {
        medico[] novo = new medico[original.length * 2];
        System.arraycopy(original, 0, novo, 0, original.length);
        return novo;
    }

    /**
     * Grava o vetor de médicos no ficheiro.
     */
    public void gravarMedicos(medico[] lista) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_FICHEIRO, false))) {

            for (int i = 0; i < lista.length; i++) {
                if (lista[i] != null) {
                    // Nota: Para gravar a especialidade, vamos buscar apenas o Código (ex: CARD)
                    // assumindo que getEspecialidade() nunca é nulo.
                    String codEsp = lista[i].getEspecialidade().getCodigo();

                    String linha = lista[i].getNome() + SEPARADOR +
                            codEsp + SEPARADOR +
                            lista[i].getHoraEntrada() + SEPARADOR +
                            lista[i].getHoraSaida() + SEPARADOR +
                            lista[i].getSalarioHora();

                    writer.write(linha);
                    writer.newLine();
                }
            }
            System.out.println("Médicos gravados com sucesso!");

        } catch (IOException e) {
            System.out.println("Erro ao gravar: " + e.getMessage());
        }
    }
}
