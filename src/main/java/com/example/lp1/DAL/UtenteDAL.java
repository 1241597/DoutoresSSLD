package com.example.lp1.DAL;

import com.example.lp1.Model.Utente;
import com.example.lp1.Model.Sintoma;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteDAL {

    private static final String CAMINHO_FICHEIRO = "Ficheiros/utentes.txt";
    private static final String SEPARADOR = ";";

    public Utente[] carregarUtentes(Sintoma[] sintomasDisponiveis) {
        File f = new File(CAMINHO_FICHEIRO);
        if (!f.exists()) return new Utente[0];

        List<Utente> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(SEPARADOR);
                String nome = partes[0].trim();
                Sintoma[] sintomas = new Sintoma[0];
                if (partes.length > 1 && !partes[1].trim().isEmpty()) {
                    String[] nomes = partes[1].split(SEPARADOR);
                    List<Sintoma> sel = new ArrayList<>();
                    for (String sn : nomes) {
                        String sTrim = sn.trim();
                        for (Sintoma sd : sintomasDisponiveis) {
                            if (sd.getNome().equalsIgnoreCase(sTrim)) {
                                sel.add(sd);
                                break;
                            }
                        }
                    }
                    sintomas = sel.toArray(new Sintoma[sel.size()]);
                }
                lista.add(new Utente(nome, sintomas));
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler utentes: " + e.getMessage());
        }
        return lista.toArray(new Utente[lista.size()]);
    }

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
}

