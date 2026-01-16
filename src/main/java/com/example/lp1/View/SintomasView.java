package com.example.lp1.View;

import com.example.lp1.DAL.SintomaDAL;
import com.example.lp1.DAL.EspecialidadeDAL;
import com.example.lp1.Model.Sintoma;
import com.example.lp1.Model.Especialidade;
import com.example.lp1.Utils.Enums.nivelUrgencia;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SintomasView {

    private SintomaDAL dal = new SintomaDAL();
    private EspecialidadeDAL espDAL = new EspecialidadeDAL();
    private Scanner scanner = new Scanner(System.in);

    public void iniciar() {
        Sintoma[] sintomas = dal.carregarSintomas();
        Especialidade[] esps = espDAL.carregarEspecialidades();
        int opcao = -1;
        do {
            System.out.println("\n--- GESTÃO DE SINTOMAS ---");
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
                case 1: listar(sintomas); break;
                case 2: sintomas = criar(sintomas, esps); break;
                case 3: sintomas = atualizar(sintomas, esps); break;
                case 4: sintomas = eliminar(sintomas); break;
                case 5:
                    dal.gravarSintomas(trimArray(sintomas));
                    sintomas = dal.carregarSintomas();
                    System.out.println("Gravado.");
                    break;
                case 0: System.out.println("A voltar..."); break;
                default: System.out.println("Inválido.");
            }
        } while (opcao != 0);
    }

    private void listar(Sintoma[] s) {
        if (s.length == 0) { System.out.println("Sem sintomas."); return; }
        for (int i = 0; i < s.length; i++) System.out.println(i + ". " + s[i].toString());
    }

    private Sintoma[] criar(Sintoma[] sintomas, Especialidade[] esps) {
        System.out.print("Nome do sintoma: "); String nome = scanner.nextLine();
        System.out.println("Nível de urgência:");
        nivelUrgencia[] vals = nivelUrgencia.values();
        for (int i = 0; i < vals.length; i++) System.out.println(i + ". " + vals[i].name());
        int nv = lerIntComDefault(0); if (nv < 0 || nv >= vals.length) nv = 0;
        nivelUrgencia urg = vals[nv];

        System.out.println("Escolha especialidades (índices separados por vírgula), ou vazio:");
        for (int i = 0; i < esps.length; i++) System.out.println(i + ". " + esps[i].getCodigo() + " - " + esps[i].getNome());
        String line = scanner.nextLine();
        Especialidade[] escolha;
        if (line.trim().isEmpty()) {
            escolha = new Especialidade[0];
        } else {
            String[] parts = line.split(",");
            List<Especialidade> sel = new ArrayList<>();
            for (String p : parts) {
                try { int idx = Integer.parseInt(p.trim()); if (idx >= 0 && idx < esps.length) sel.add(esps[idx]); } catch (Exception ignored) {}
            }
            escolha = sel.toArray(new Especialidade[sel.size()]);
        }

        Sintoma novo = new Sintoma(nome, urg, escolha);
        List<Sintoma> l = new ArrayList<>();
        for (Sintoma si : sintomas) l.add(si);
        l.add(novo);
        System.out.println("Criado.");
        return l.toArray(new Sintoma[l.size()]);
    }

    private Sintoma[] atualizar(Sintoma[] sintomas, Especialidade[] esps) {
        listar(sintomas);
        if (sintomas.length == 0) return sintomas;
        System.out.print("Índice a atualizar: "); int idx = lerIntComDefault(-1);
        if (idx < 0 || idx >= sintomas.length) return sintomas;
        Sintoma s = sintomas[idx];
        System.out.print("Novo nome (" + s.getNome() + "): "); String n = scanner.nextLine(); if (!n.isEmpty()) s.setNome(n);
        System.out.println("Nível atual: " + s.getUrgencia().name());
        System.out.println("Escolha novo nível (enter para manter):");
        nivelUrgencia[] vals = nivelUrgencia.values();
        for (int i = 0; i < vals.length; i++) System.out.println(i + ". " + vals[i].name());
        String nv = scanner.nextLine();
        if (!nv.isEmpty()) {
            try { int v = Integer.parseInt(nv); if (v >= 0 && v < vals.length) s.setUrgencia(vals[v]); } catch (Exception ignored) {}
        }
        System.out.println("Atualizar especialidades (enter para manter):");
        for (int i = 0; i < esps.length; i++) System.out.println(i + ". " + esps[i].getCodigo());
        String line = scanner.nextLine();
        if (!line.isEmpty()) {
            String[] parts = line.split(",");
            List<Especialidade> sel = new ArrayList<>();
            for (String p : parts) {
                try { int id = Integer.parseInt(p.trim()); if (id >= 0 && id < esps.length) sel.add(esps[id]); } catch (Exception ignored) {}
            }
            s.setEspecialidades(sel.toArray(new Especialidade[sel.size()]));
        }
        sintomas[idx] = s;
        System.out.println("Atualizado.");
        return sintomas;
    }

    private Sintoma[] eliminar(Sintoma[] sintomas) {
        listar(sintomas);
        if (sintomas.length == 0) return sintomas;
        System.out.print("Índice a eliminar: "); int idx = lerIntComDefault(-1);
        if (idx < 0 || idx >= sintomas.length) return sintomas;
        List<Sintoma> l = new ArrayList<>();
        for (int i = 0; i < sintomas.length; i++) if (i != idx) l.add(sintomas[i]);
        System.out.println("Eliminado.");
        return l.toArray(new Sintoma[l.size()]);
    }

    private Sintoma[] trimArray(Sintoma[] arr) {
        List<Sintoma> l = new ArrayList<>();
        for (Sintoma s : arr) if (s != null) l.add(s);
        return l.toArray(new Sintoma[l.size()]);
    }

    private int lerIntComDefault(int def) {
        try { String s = scanner.nextLine(); return s.isEmpty() ? def : Integer.parseInt(s); } catch (Exception e) { return def; }
    }
}

