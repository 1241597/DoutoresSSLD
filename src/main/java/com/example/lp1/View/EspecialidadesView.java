package com.example.lp1.View;

import com.example.lp1.DAL.EspecialidadeDAL;
import com.example.lp1.Model.Especialidade;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EspecialidadesView {

    private EspecialidadeDAL dal = new EspecialidadeDAL();
    private Scanner scanner = new Scanner(System.in);

    public void iniciar() {
        Especialidade[] esps = dal.carregarEspecialidades();
        int opcao = -1;
        do {
            System.out.println("\n--- GESTÃO DE ESPECIALIDADES ---");
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
                case 1: listar(esps); break;
                case 2: esps = criar(esps); break;
                case 3: esps = atualizar(esps); break;
                case 4: esps = eliminar(esps); break;
                case 5:
                    dal.gravarFicheiro(trimArray(esps));
                    esps = dal.carregarEspecialidades();
                    System.out.println("Gravado.");
                    break;
                case 0: System.out.println("A voltar..."); break;
                default: System.out.println("Inválido.");
            }
        } while (opcao != 0);
    }

    private void listar(Especialidade[] esps) {
        if (esps.length == 0) { System.out.println("Sem especialidades."); return; }
        for (int i = 0; i < esps.length; i++) System.out.println(i + ". " + esps[i].getCodigo() + " - " + esps[i].getNome());
    }

    private Especialidade[] criar(Especialidade[] esps) {
        System.out.print("Código: "); String codigo = scanner.nextLine();
        System.out.print("Nome: "); String nome = scanner.nextLine();
        Especialidade e = new Especialidade(codigo, nome);
        List<Especialidade> l = new ArrayList<>();
        for (Especialidade ex : esps) l.add(ex);
        l.add(e);
        System.out.println("Criado.");
        return l.toArray(new Especialidade[l.size()]);
    }

    private Especialidade[] atualizar(Especialidade[] esps) {
        listar(esps);
        if (esps.length == 0) return esps;
        System.out.print("Índice a atualizar: ");
        int idx; try { idx = Integer.parseInt(scanner.nextLine()); } catch (Exception e) { System.out.println("Índice inválido."); return esps; }
        if (idx < 0 || idx >= esps.length) { System.out.println("Índice inválido."); return esps; }
        Especialidade ex = esps[idx];
        System.out.print("Novo código (" + ex.getCodigo() + "): "); String c = scanner.nextLine(); if (!c.isEmpty()) ex.setCodigo(c);
        System.out.print("Novo nome (" + ex.getNome() + "): "); String n = scanner.nextLine(); if (!n.isEmpty()) ex.setNome(n);
        esps[idx] = ex;
        System.out.println("Atualizado.");
        return esps;
    }

    private Especialidade[] eliminar(Especialidade[] esps) {
        listar(esps);
        if (esps.length == 0) return esps;
        System.out.print("Índice a eliminar: ");
        int idx; try { idx = Integer.parseInt(scanner.nextLine()); } catch (Exception e) { System.out.println("Índice inválido."); return esps; }
        List<Especialidade> l = new ArrayList<>();
        for (int i = 0; i < esps.length; i++) if (i != idx) l.add(esps[i]);
        System.out.println("Eliminado.");
        return l.toArray(new Especialidade[l.size()]);
    }

    private Especialidade[] trimArray(Especialidade[] arr) {
        List<Especialidade> l = new ArrayList<>();
        for (Especialidade e : arr) if (e != null) l.add(e);
        return l.toArray(new Especialidade[l.size()]);
    }
}

