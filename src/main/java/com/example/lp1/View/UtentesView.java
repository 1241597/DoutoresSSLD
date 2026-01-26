package com.example.lp1.View;

import com.example.lp1.DAL.UtenteDAL;
import com.example.lp1.DAL.SintomaDAL;
import com.example.lp1.Model.Utente;
import com.example.lp1.Model.Sintoma;
import java.util.Scanner;

/**
 * Interface de consola para gestão de Utentes.
 * Operações: listar, criar, atualizar, eliminar e gravar.
 * Utiliza arrays e o UtenteDAL para persistência.
 */
public class UtentesView {

    private UtenteDAL dal = new UtenteDAL();
    private SintomaDAL sintomaDAL = new SintomaDAL();
    private Scanner scanner = new Scanner(System.in);

    /**
     * Inicia menu de utentes.
     */
    public void iniciar() {
        Sintoma[] sintomasDisponiveis = sintomaDAL.carregarSintomas();
        Utente[] utentes = dal.carregarUtentes(sintomasDisponiveis);

        int opcao = -1;
        do {
            System.out.println("\n--- GESTÃO DE UTENTES ---");
            System.out.println("1. Listar");
            System.out.println("2. Criar");
            System.out.println("3. Atualizar");
            System.out.println("4. Eliminar");
            System.out.println("5. Gravar alterações");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            String in = scanner.nextLine();
            try {
                opcao = in.isEmpty() ? -1 : Integer.parseInt(in);
            } catch (Exception e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    listar(utentes);
                    break;
                case 2:
                    utentes = criar(utentes, sintomasDisponiveis);
                    break;
                case 3:
                    utentes = atualizar(utentes, sintomasDisponiveis);
                    break;
                case 4:
                    utentes = eliminar(utentes);
                    break;
                case 5:
                    dal.gravarUtentes(trimArray(utentes));
                    utentes = dal.carregarUtentes(sintomasDisponiveis);
                    System.out.println("Gravado.");
                    break;
                case 0:
                    System.out.println("A voltar...");
                    break;
                default:
                    System.out.println("Inválido.");
            }
        } while (opcao != 0);
    }

    /**
     * Lista utentes.
     *
     * @param u array de Utente
     */
    private void listar(Utente[] u) {
        if (u.length == 0) {
            System.out.println("Sem utentes.");
            return;
        }
        for (int i = 0; i < u.length; i++)
            System.out.println(i + ". " + u[i].toString());
    }

    /**
     * Cria novo utente a partir da entrada do utilizador.
     *
     * @param utentes             array atual de Utente
     * @param sintomasDisponiveis array de Sintoma disponível
     * @return array com o utente adicionado
     */
    private Utente[] criar(Utente[] utentes, Sintoma[] sintomasDisponiveis) {
        System.out.print("Nome utente: ");
        String nome = scanner.nextLine();
        System.out.println("Escolha sintomas (índices separados por vírgula), ou vazio para nenhum:");
        for (int i = 0; i < sintomasDisponiveis.length; i++)
            System.out.println(i + ". " + sintomasDisponiveis[i].getNome());
        String sel = scanner.nextLine();
        Sintoma[] selSintomas;
        if (sel.trim().isEmpty()) {
            selSintomas = new Sintoma[0];
        } else {
            String[] parts = sel.split(",");
            Sintoma[] list = new Sintoma[0];
            for (String p : parts) {
                try {
                    int idx = Integer.parseInt(p.trim());
                    if (idx >= 0 && idx < sintomasDisponiveis.length)
                        list = appendSintoma(list, sintomasDisponiveis[idx]);
                } catch (Exception ignored) {
                }
            }
            selSintomas = list;
        }
        Utente novo = new Utente(nome, selSintomas);
        utentes = appendUtente(utentes, novo);

        // Guardar o novo utente no ficheiro
        dal.adicionarUtente(novo);

        System.out.println("Criado.");
        return utentes;
    }

    /**
     * Atualiza utente existente.
     *
     * @param utentes             array de Utente
     * @param sintomasDisponiveis array de Sintoma
     * @return array atualizado
     */
    private Utente[] atualizar(Utente[] utentes, Sintoma[] sintomasDisponiveis) {
        listar(utentes);
        if (utentes.length == 0)
            return utentes;
        System.out.print("Índice a atualizar: ");
        int idx = lerIntComDefault(-1);
        if (idx < 0 || idx >= utentes.length)
            return utentes;
        Utente u = utentes[idx];
        System.out.print("Novo nome (" + u.getNome() + "): ");
        String n = scanner.nextLine();
        if (!n.isEmpty())
            u.setNome(n);
        System.out.println("Atualizar sintomas? (enter para manter)");
        for (int i = 0; i < sintomasDisponiveis.length; i++)
            System.out.println(i + ". " + sintomasDisponiveis[i].getNome());
        String s = scanner.nextLine();
        if (!s.isEmpty()) {
            String[] parts = s.split(",");
            Sintoma[] list = new Sintoma[0];
            for (String p : parts) {
                try {
                    int id = Integer.parseInt(p.trim());
                    if (id >= 0 && id < sintomasDisponiveis.length)
                        list = appendSintoma(list, sintomasDisponiveis[id]);
                } catch (Exception ignored) {
                }
            }
            u.setSintomas(list);
        }
        utentes[idx] = u;
        System.out.println("Atualizado.");
        return utentes;
    }

    /**
     * Elimina utente por índice.
     *
     * @param utentes array de Utente
     * @return array após remoção
     */
    private Utente[] eliminar(Utente[] utentes) {
        listar(utentes);
        if (utentes.length == 0)
            return utentes;
        System.out.print("Índice a eliminar: ");
        int idx = lerIntComDefault(-1);
        if (idx < 0 || idx >= utentes.length)
            return utentes;
        Utente[] r = new Utente[utentes.length - 1];
        int j = 0;
        for (int i = 0; i < utentes.length; i++)
            if (i != idx)
                r[j++] = utentes[i];
        System.out.println("Eliminado.");
        return r;
    }

    private Utente[] trimArray(Utente[] arr) {
        int count = 0;
        for (Utente u : arr)
            if (u != null)
                count++;
        Utente[] r = new Utente[count];
        int j = 0;
        for (Utente u : arr)
            if (u != null)
                r[j++] = u;
        return r;
    }

    // Helpers
    private Utente[] appendUtente(Utente[] arr, Utente item) {
        Utente[] r = new Utente[arr.length + 1];
        System.arraycopy(arr, 0, r, 0, arr.length);
        r[arr.length] = item;
        return r;
    }

    private Sintoma[] appendSintoma(Sintoma[] arr, Sintoma item) {
        Sintoma[] r = new Sintoma[arr.length + 1];
        System.arraycopy(arr, 0, r, 0, arr.length);
        r[arr.length] = item;
        return r;
    }

    private int lerIntComDefault(int def) {
        try {
            String s = scanner.nextLine();
            return s.isEmpty() ? def : Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }
}
