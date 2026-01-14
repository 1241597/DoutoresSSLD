package com.example.lp1.View;

import com.example.lp1.DAL.UtenteDAL;
import com.example.lp1.DAL.SintomaDAL;
import com.example.lp1.Model.Utente;
import com.example.lp1.Model.Sintoma;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UtentesView {

    private UtenteDAL dal = new UtenteDAL();
    private SintomaDAL sintomaDAL = new SintomaDAL();
    private Scanner scanner = new Scanner(System.in);

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
            try { opcao = in.isEmpty() ? -1 : Integer.parseInt(in); } catch (Exception e) { opcao = -1; }

            switch (opcao) {
                case 1: listar(utentes); break;
                case 2: utentes = criar(utentes, sintomasDisponiveis); break;
                case 3: utentes = atualizar(utentes, sintomasDisponiveis); break;
                case 4: utentes = eliminar(utentes); break;
                case 5:
                    dal.gravarUtentes(trimArray(utentes));
                    utentes = dal.carregarUtentes(sintomasDisponiveis);
                    System.out.println("Gravado.");
                    break;
                case 0: System.out.println("A voltar..."); break;
                default: System.out.println("Inválido.");
            }
        } while (opcao != 0);
    }

    private void listar(Utente[] u) {
        if (u.length == 0) { System.out.println("Sem utentes."); return; }
        for (int i = 0; i < u.length; i++) System.out.println(i + ". " + u[i].toString());
    }

    private Utente[] criar(Utente[] utentes, Sintoma[] sintomasDisponiveis) {
        System.out.print("Nome utente: "); String nome = scanner.nextLine();
        System.out.println("Escolha sintomas (índices separados por vírgula), ou vazio para nenhum:");
        for (int i = 0; i < sintomasDisponiveis.length; i++) System.out.println(i + ". " + sintomasDisponiveis[i].getNome());
        String sel = scanner.nextLine();
        Sintoma[] selSintomas;
        if (sel.trim().isEmpty()) {
            selSintomas = new Sintoma[0];
        } else {
            String[] parts = sel.split(",");
            List<Sintoma> list = new ArrayList<>();
            for (String p : parts) {
                try { int idx = Integer.parseInt(p.trim()); if (idx >= 0 && idx < sintomasDisponiveis.length) list.add(sintomasDisponiveis[idx]); } catch (Exception ignored) {}
            }
            selSintomas = list.toArray(new Sintoma[list.size()]);
        }
        Utente novo = new Utente(nome, selSintomas);
        List<Utente> l = new ArrayList<>();
        for (Utente ut : utentes) l.add(ut);
        l.add(novo);
        System.out.println("Criado.");
        return l.toArray(new Utente[l.size()]);
    }

    private Utente[] atualizar(Utente[] utentes, Sintoma[] sintomasDisponiveis) {
        listar(utentes);
        if (utentes.length == 0) return utentes;
        System.out.print("Índice a atualizar: "); int idx = lerIntComDefault(-1);
        if (idx < 0 || idx >= utentes.length) return utentes;
        Utente u = utentes[idx];
        System.out.print("Novo nome (" + u.getNome() + "): "); String n = scanner.nextLine(); if (!n.isEmpty()) u.setNome(n);
        System.out.println("Atualizar sintomas? (enter para manter)");
        for (int i = 0; i < sintomasDisponiveis.length; i++) System.out.println(i + ". " + sintomasDisponiveis[i].getNome());
        String s = scanner.nextLine();
        if (!s.isEmpty()) {
            String[] parts = s.split(",");
            List<Sintoma> list = new ArrayList<>();
            for (String p : parts) {
                try { int id = Integer.parseInt(p.trim()); if (id >= 0 && id < sintomasDisponiveis.length) list.add(sintomasDisponiveis[id]); } catch (Exception ignored) {}
            }
            u.setSintomas(list.toArray(new Sintoma[list.size()]));
        }
        utentes[idx] = u;
        System.out.println("Atualizado.");
        return utentes;
    }

    private Utente[] eliminar(Utente[] utentes) {
        listar(utentes);
        if (utentes.length == 0) return utentes;
        System.out.print("Índice a eliminar: "); int idx = lerIntComDefault(-1);
        if (idx < 0 || idx >= utentes.length) return utentes;
        List<Utente> l = new ArrayList<>();
        for (int i = 0; i < utentes.length; i++) if (i != idx) l.add(utentes[i]);
        System.out.println("Eliminado.");
        return l.toArray(new Utente[l.size()]);
    }

    private Utente[] trimArray(Utente[] arr) {
        List<Utente> l = new ArrayList<>();
        for (Utente u : arr) if (u != null) l.add(u);
        return l.toArray(new Utente[l.size()]);
    }

    private int lerIntComDefault(int def) {
        try { String s = scanner.nextLine(); return s.isEmpty() ? def : Integer.parseInt(s); } catch (Exception e) { return def; }
    }
}

