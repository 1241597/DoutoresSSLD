package com.example.lp1.DAL;

import com.example.lp1.Model.Especialidade;
import java.io.*;

public class EspecialidadeDAL {

    private static final String CAMINHO_FICHEIRO = "Ficheiros/especialidades.txt";
    private String separador = ";";

    public EspecialidadeDAL() {
        // Construtor vazio usa o padrão
    }

    public EspecialidadeDAL(String separador) {
        this.separador = separador;
    }

    /**
     * LÊ o ficheiro e retorna um array de objetos Especialidade.
     * Usa a lógica de expandir o array conforme necessário.
     */
    public Especialidade[] carregarEspecialidades() {
        Especialidade[] especialidadesTemp = new Especialidade[50];
        int index = 0;

        File file = new File(CAMINHO_FICHEIRO);
        if (!file.exists()) {
            return new Especialidade[0]; // Retorna array vazio se não houver ficheiro
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();

                // Ignorar linhas vazias
                if (!linha.isEmpty()) {
                    String[] partes = linha.split(separador);

                    // Validar se tem as colunas necessárias (Codigo e Nome)
                    if (partes.length >= 2) {

                        // VERIFICAÇÃO: Se o array está cheio, expande
                        if (index >= especialidadesTemp.length) {
                            especialidadesTemp = expandirArray(especialidadesTemp);
                        }

                        // Cria e guarda o objeto
                        Especialidade esp = new Especialidade(partes[0], partes[1]);
                        especialidadesTemp[index++] = esp;

                    } else {
                        System.out.println("Linha inválida: " + linha);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o ficheiro: " + e.getMessage());
        }

        // --- VALIDAÇÃO DE SEGURANÇA ---
        if (file.exists() && file.length() > 0 && index == 0) {
            System.out.println("[AVISO] O separador '" + separador
                    + "' parece incorreto no ficheiro de Especialidades. Nenhum registo carregado.");
            return new Especialidade[0];
        }

        // --- TRIMMING (CORTAR O EXCESSO) ---
        // Agora criamos um array do tamanho exato (index) e copiamos os dados
        Especialidade[] especialidadesFinais = new Especialidade[index];
        System.arraycopy(especialidadesTemp, 0, especialidadesFinais, 0, index);

        return especialidadesFinais;
    }

    /**
     * Expande o tamanho do array para o dobro do original.
     */
    private Especialidade[] expandirArray(Especialidade[] original) {
        // Cria um novo array com o dobro do tamanho
        Especialidade[] novoArray = new Especialidade[original.length * 2];

        // Copia tudo do velho para o novo
        System.arraycopy(original, 0, novoArray, 0, original.length);

        return novoArray;
    }

    /**
     * GRAVAR: Recebe o vetor e escreve no ficheiro
     */
    public void gravarFicheiro(Especialidade[] lista) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_FICHEIRO, false))) {

            for (int i = 0; i < lista.length; i++) {
                if (lista[i] != null) {
                    String linha = lista[i].getCodigo() + separador + lista[i].getNome();
                    writer.write(linha);
                    writer.newLine();
                }
            }
            System.out.println("Guardado com sucesso!");

        } catch (IOException e) {
            System.out.println("Erro ao gravar: " + e.getMessage());
        }
    }
}