package com.example.lp1.View;

import com.example.lp1.DAL.EspecialidadeDAL;
import com.example.lp1.Model.Especialidade;
import java.util.Scanner;

/**
 * Interface de consola para gestão de especialidades.
 * Permite listar, criar, atualizar, eliminar e gravar especialidades.
 * Utiliza arrays em memória e grava via EspecialidadeDAL.
 */
public class EspecialidadesView {

    // Carregar configuração para saber o separador
    private com.example.lp1.DAL.ConfiguracaoDAL configDal = new com.example.lp1.DAL.ConfiguracaoDAL();
    private String separator = configDal.carregarConfiguracao().getSeparadorFicheiro();

    private EspecialidadeDAL dal = new EspecialidadeDAL(separator);
    private Scanner scanner = new Scanner(System.in);

    /**
     * Inicia o menu de gestão de especialidades.
     */
    private boolean gravacaoBloqueada = false;

    /**
     * Inicia o menu de gestão de especialidades.
     */
    public void iniciar() {
        Especialidade[] esps;
        try {
            esps = dal.carregarEspecialidades();
            if (esps == null) {
                esps = new Especialidade[0];
                gravacaoBloqueada = true;
            } else {
                gravacaoBloqueada = false;
            }
        } catch (RuntimeException e) {
            esps = new Especialidade[0];
            gravacaoBloqueada = true;
        }
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
            try {
                opcao = in.isEmpty() ? -1 : Integer.parseInt(in);
            } catch (Exception e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    listar(esps);
                    break;
                case 2:
                    esps = criar(esps);
                    break;
                case 3:
                    esps = atualizar(esps);
                    break;
                case 4:
                    esps = eliminar(esps);
                    break;
                case 5:
                    if (gravacaoBloqueada) {
                        System.out.println("[AVISO] Gravação bloqueada devido a erro de formato.");
                    } else {
                        dal.gravarFicheiro(trimArray(esps));
                        esps = dal.carregarEspecialidades();
                        System.out.println("Gravado.");
                    }
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
     * Lista especialidades no ecrã.
     *
     * @param esps array de Especialidade
     */
    private void listar(Especialidade[] esps) {
        if (esps.length == 0) {
            System.out.println("Sem especialidades.");
            return;
        }
        for (int i = 0; i < esps.length; i++)
            System.out.println(i + ". " + esps[i].getCodigo() + " - " + esps[i].getNome());
    }

    /**
     * Cria uma nova especialidade a partir da entrada do utilizador.
     *
     * @param esps array atual de Especialidade
     * @return novo array com a especialidade adicionada
     */
    private Especialidade[] criar(Especialidade[] esps) {
        System.out.print("Código: ");
        String codigo = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        Especialidade e = new Especialidade(codigo, nome);
        return appendEspecialidade(esps, e);
    }

    /**
     * Atualiza uma especialidade existente.
     *
     * @param esps array de Especialidade
     * @return array atualizado
     */
    private Especialidade[] atualizar(Especialidade[] esps) {
        listar(esps);
        if (esps.length == 0)
            return esps;
        System.out.print("Índice a atualizar: ");
        int idx;
        try {
            idx = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Índice inválido.");
            return esps;
        }
        if (idx < 0 || idx >= esps.length) {
            System.out.println("Índice inválido.");
            return esps;
        }
        Especialidade ex = esps[idx];
        System.out.print("Novo código (" + ex.getCodigo() + "): ");
        String c = scanner.nextLine();
        if (!c.isEmpty())
            ex.setCodigo(c);
        System.out.print("Novo nome (" + ex.getNome() + "): ");
        String n = scanner.nextLine();
        if (!n.isEmpty())
            ex.setNome(n);
        esps[idx] = ex;
        System.out.println("Atualizado.");
        return esps;
    }

    /**
     * Elimina uma especialidade por índice.
     *
     * @param esps array de Especialidade
     * @return array após remoção
     */
    private Especialidade[] eliminar(Especialidade[] esps) {
        listar(esps);
        if (esps.length == 0)
            return esps;
        System.out.print("Índice a eliminar: ");
        int idx;
        try {
            idx = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Índice inválido.");
            return esps;
        }
        return removeEspecialidadeAt(esps, idx);
    }

    private Especialidade[] trimArray(Especialidade[] arr) {
        int count = 0;
        for (Especialidade e : arr)
            if (e != null)
                count++;
        Especialidade[] r = new Especialidade[count];
        int j = 0;
        for (Especialidade e : arr)
            if (e != null)
                r[j++] = e;
        return r;
    }

    // Helpers
    private Especialidade[] appendEspecialidade(Especialidade[] arr, Especialidade item) {
        Especialidade[] r = new Especialidade[arr.length + 1];
        System.arraycopy(arr, 0, r, 0, arr.length);
        r[arr.length] = item;
        return r;
    }

    private Especialidade[] removeEspecialidadeAt(Especialidade[] arr, int idx) {
        if (idx < 0 || idx >= arr.length)
            return arr;
        Especialidade[] r = new Especialidade[arr.length - 1];
        int j = 0;
        for (int i = 0; i < arr.length; i++) {
            if (i == idx)
                continue;
            r[j++] = arr[i];
        }
        System.out.println("Eliminado.");
        return r;
    }
}
