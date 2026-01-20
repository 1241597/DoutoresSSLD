package com.example.lp1.DAL;

import com.example.lp1.Model.Utente;
import com.example.lp1.Model.Sintoma;
import java.io.*;

/**
 * Data Access Layer para operações de leitura e escrita de Utentes.
 * Os utentes são persistidos em Ficheiros/utentes.txt no formato:
 * Nome;Sintoma1;Sintoma2;...
 *
 * Esta classe utiliza arrays em vez de colecções dinamicas (ArrayList).
 */
public class UtenteDAL {

    private static final String CAMINHO_FICHEIRO = "Ficheiros/utentes.txt";
    private static final String SEPARADOR = ";";

    /**
     * Carrega utentes do ficheiro.
     *
     * @param sintomasDisponiveis array de Sintoma disponíveis para mapear nomes para objetos
     * @return array de Utente carregados (pode ser array vazio)
     */
    public Utente[] carregarUtentes(Sintoma[] sintomasDisponiveis) {
        File f = new File(CAMINHO_FICHEIRO);
        if (!f.exists()) return new Utente[0];

        Utente[] lista = new Utente[0];
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(SEPARADOR);
                String nome = partes[0].trim();
                Sintoma[] sintomas = new Sintoma[0];
                if (partes.length > 1 && !partes[1].trim().isEmpty()) {
                    String[] nomes = partes[1].split(SEPARADOR);
                    Sintoma[] sel = new Sintoma[0];
                    for (String sn : nomes) {
                        String sTrim = sn.trim();
                        for (Sintoma sd : sintomasDisponiveis) {
                            if (sd.getNome().equalsIgnoreCase(sTrim)) {
                                sel = appendSintoma(sel, sd);
                                break;
                            }
                        }
                    }
                    sintomas = sel;
                }
                lista = appendUtente(lista, new Utente(nome, sintomas));
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler utentes: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Grava o array de utentes no ficheiro (sobrescreve).
     *
     * @param lista array de Utente a gravar
     */
    public void gravarUtentes(Utente[] lista) {
        File f = new File(CAMINHO_FICHEIRO);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, false))) {
            for (Utente u : lista) {
                if (u == null) continue;
                StringBuilder linha = new StringBuilder();
                linha.append(u.getNome()).append(SEPARADOR);
                if (u.getSintomas() != null && u.getSintomas().length > 0) {
                    boolean first = true;
                    for (Sintoma s : u.getSintomas()) {
                        if (s == null) continue;
                        if (!first) linha.append(SEPARADOR);
                        linha.append(s.getNome());
                        first = false;
                    }
                }
                bw.write(linha.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao gravar utentes: " + e.getMessage());
        }
    }

    // Helpers para arrays

    /**
     * Adiciona um Utente ao final de um array (cria novo array).
     *
     * @param arr array original
     * @param item Utente a adicionar
     * @return novo array com o item adicionado
     */
    private Utente[] appendUtente(Utente[] arr, Utente item) {
        Utente[] r = new Utente[arr.length + 1];
        System.arraycopy(arr, 0, r, 0, arr.length);
        r[arr.length] = item;
        return r;
    }

    /**
     * Adiciona um Sintoma ao final de um array (cria novo array).
     *
     * @param arr array original
     * @param item Sintoma a adicionar
     * @return novo array com o item adicionado
     */
    private Sintoma[] appendSintoma(Sintoma[] arr, Sintoma item) {
        Sintoma[] r = new Sintoma[arr.length + 1];
        System.arraycopy(arr, 0, r, 0, arr.length);
        r[arr.length] = item;
        return r;
    }
}
