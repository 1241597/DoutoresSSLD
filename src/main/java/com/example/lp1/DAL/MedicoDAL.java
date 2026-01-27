package com.example.lp1.DAL;

import com.example.lp1.Model.Medico;
import com.example.lp1.Model.Especialidade;
import java.io.*;

public class MedicoDAL {

    private static final String CAMINHO_FICHEIRO = "Ficheiros/medicos.txt";
    private String separador = ";";

    public MedicoDAL() {
    }

    public MedicoDAL(String separador) {
        this.separador = separador;
    }

    /**
     * Carrega os médicos do ficheiro para um vetor.
     */
    public Medico[] carregarMedicos() {
        // 1. Array inicial temporário
        Medico[] medicosTemp = new Medico[50];
        int index = 0;

        File file = new File(CAMINHO_FICHEIRO);
        if (!file.exists()) {
            return new Medico[0];
        }

        boolean leuAlgumaLinha = false;
        int linhasValidas = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();

                if (!linha.isEmpty()) {
                    leuAlgumaLinha = true;
                    String[] partes = linha.split(separador);

                    // O ficheiro tem: Nome;Sigla;HoraEnt;HoraSai;Salario
                    if (partes.length == 5) {
                        // Verificar se precisamos de aumentar o array
                        if (index >= medicosTemp.length) {
                            medicosTemp = expandirArray(medicosTemp);
                        }

                        // --- EXTRAÇÃO DOS DADOS ---
                        String nome = partes[0];
                        String codEspecialidade = partes[1];
                        double hEntrada = Double.parseDouble(partes[2]);
                        double hSaida = Double.parseDouble(partes[3]);
                        double salario = Double.parseDouble(partes[4]);

                        // --- CRIAÇÃO DOS OBJETOS ---
                        Especialidade espObj = new Especialidade(codEspecialidade, "Indefinido");
                        Medico m = new Medico(nome, espObj, hEntrada, hSaida, salario);

                        medicosTemp[index++] = m;
                        linhasValidas++;

                    } else {
                        System.out.println(
                                "Linha com formato errado (esperado 5 campos, obteve " + partes.length + "): " + linha);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao ler ficheiro: " + e.getMessage());
        }

        // --- VALIDAÇÃO DE SEGURANÇA ---
        // Se o ficheiro não estava vazio, mas não conseguimos ler NENHUMA linha válida,
        // é muito provável que o separador esteja errado.
        // Lançamos exceção para impedir que a View grave uma lista vazia por cima!
        if (leuAlgumaLinha && linhasValidas == 0) {
            System.out.println("[AVISO] O separador '" + separador
                    + "' parece incorreto no ficheiro de Médicos. Nenhum registo carregado.");
            return new Medico[0];
        }

        // Criar array final com tamanho exato
        Medico[] resultado = new Medico[index];
        System.arraycopy(medicosTemp, 0, resultado, 0, index);

        return resultado;
    }

    /**
     * Expande o array para o dobro do tamanho.
     */
    private Medico[] expandirArray(Medico[] original) {
        Medico[] novo = new Medico[original.length * 2];
        System.arraycopy(original, 0, novo, 0, original.length);
        return novo;
    }

    /**
     * Grava o vetor de médicos no ficheiro.
     */
    public void gravarMedicos(Medico[] lista) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_FICHEIRO, false))) {

            for (int i = 0; i < lista.length; i++) {
                if (lista[i] != null) {
                    // Nota: Para gravar a especialidade, vamos buscar apenas o Código (ex: CARD)
                    // assumindo que getEspecialidade() nunca é nulo.
                    String codEsp = lista[i].getEspecialidade().getCodigo();

                    String linha = lista[i].getNome() + separador +
                            codEsp + separador +
                            lista[i].getHoraEntrada() + separador +
                            lista[i].getHoraSaida() + separador +
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
