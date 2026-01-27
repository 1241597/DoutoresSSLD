package com.example.lp1.View;

import com.example.lp1.DAL.SintomaDAL;
import com.example.lp1.DAL.EspecialidadeDAL;
import com.example.lp1.Model.Sintoma;
import com.example.lp1.Model.Especialidade;
import com.example.lp1.Utils.Enums.nivelUrgencia;
import java.util.Scanner;

/**
 * Interface de consola para gestão de sintomas.
 * Operações: listar, criar, atualizar, eliminar e gravar.
 */
public class SintomasView {

    // Carregar configuração para saber o separador
    private com.example.lp1.DAL.ConfiguracaoDAL configDal = new com.example.lp1.DAL.ConfiguracaoDAL();
    private String separator = configDal.carregarConfiguracao().getSeparadorFicheiro();

    private SintomaDAL dal = new SintomaDAL(separator);
    private EspecialidadeDAL espDAL = new EspecialidadeDAL(separator);
    private Scanner scanner = new Scanner(System.in);

    /**
     * Inicia menu de sintomas.
     */
    private boolean gravacaoBloqueada = false;

    /**
     * Inicia menu de sintomas.
     */
    public void iniciar() {
        Sintoma[] sintomas;
        try {
            sintomas = dal.carregarSintomas();
            if (sintomas == null) {
                sintomas = new Sintoma[0];
                gravacaoBloqueada = true;
            } else {
                gravacaoBloqueada = false;
            }
        } catch (RuntimeException e) {
            sintomas = new Sintoma[0];
            gravacaoBloqueada = true;
        }
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
            try {
                opcao = in.isEmpty() ? -1 : Integer.parseInt(in);
            } catch (Exception e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    listar(sintomas);
                    break;
                case 2:
                    sintomas = criar(sintomas, esps);
                    break;
                case 3:
                    sintomas = atualizar(sintomas, esps);
                    break;
                case 4:
                    sintomas = eliminar(sintomas);
                    break;
                case 5:
                    if (gravacaoBloqueada) {
                        System.out.println("[AVISO] Gravação bloqueada devido a erro de formato.");
                    } else {
                        dal.gravarSintomas(trimArray(sintomas));
                        sintomas = dal.carregarSintomas();
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
     * Lista sintomas.
     *
     * @param s array de Sintoma
     */
    private void listar(Sintoma[] s) {
        if (s.length == 0) {
            System.out.println("Sem sintomas.");
            return;
        }
        for (int i = 0; i < s.length; i++)
            System.out.println(i + ". " + s[i].toString());
    }

    /**
     * Cria novo sintoma.
     *
     * @param sintomas array atual de Sintoma
     * @param esps     array de Especialidade disponível
     * @return array com o novo sintoma
     */
    private Sintoma[] criar(Sintoma[] sintomas, Especialidade[] esps) {
        System.out.print("Nome do sintoma: ");
        String nome = scanner.nextLine();
        System.out.println("Nível de urgência:");
        nivelUrgencia[] vals = nivelUrgencia.values();
        for (int i = 0; i < vals.length; i++)
            System.out.println(i + ". " + vals[i].name());
        int nv = lerIntComDefault(0);
        if (nv < 0 || nv >= vals.length)
            nv = 0;
        nivelUrgencia urg = vals[nv];

        System.out.println("Escolha especialidades (índices separados por vírgula), ou vazio:");
        for (int i = 0; i < esps.length; i++)
            System.out.println(i + ". " + esps[i].getCodigo() + " - " + esps[i].getNome());
        String line = scanner.nextLine();
        Especialidade[] escolha;
        if (line.trim().isEmpty()) {
            escolha = new Especialidade[0];
        } else {
            String[] parts = line.split(",");
            Especialidade[] sel = new Especialidade[0];
            for (String p : parts) {
                try {
                    int idx = Integer.parseInt(p.trim());
                    if (idx >= 0 && idx < esps.length)
                        sel = appendEspecialidade(sel, esps[idx]);
                } catch (Exception ignored) {
                }
            }
            escolha = sel;
        }

        Sintoma novo = new Sintoma(nome, urg, escolha);
        sintomas = appendSintoma(sintomas, novo);
        System.out.println("Criado.");
        return sintomas;
    }

    /**
     * Atualiza sintoma existente.
     *
     * @param sintomas array de Sintoma
     * @param esps     array de Especialidade
     * @return array atualizado
     */
    private Sintoma[] atualizar(Sintoma[] sintomas, Especialidade[] esps) {
        listar(sintomas);
        if (sintomas.length == 0)
            return sintomas;
        System.out.print("Índice a atualizar: ");
        int idx = lerIntComDefault(-1);
        if (idx < 0 || idx >= sintomas.length)
            return sintomas;
        Sintoma s = sintomas[idx];
        System.out.print("Novo nome (" + s.getNome() + "): ");
        String n = scanner.nextLine();
        if (!n.isEmpty())
            s.setNome(n);
        System.out.println("Nível atual: " + s.getUrgencia().name());
        System.out.println("Escolha novo nível (enter para manter):");
        nivelUrgencia[] vals = nivelUrgencia.values();
        for (int i = 0; i < vals.length; i++)
            System.out.println(i + ". " + vals[i].name());
        String nv = scanner.nextLine();
        if (!nv.isEmpty()) {
            try {
                int v = Integer.parseInt(nv);
                if (v >= 0 && v < vals.length)
                    s.setUrgencia(vals[v]);
            } catch (Exception ignored) {
            }
        }
        System.out.println("Atualizar especialidades (enter para manter):");
        for (int i = 0; i < esps.length; i++)
            System.out.println(i + ". " + esps[i].getCodigo());
        String line = scanner.nextLine();
        if (!line.isEmpty()) {
            String[] parts = line.split(",");
            Especialidade[] sel = new Especialidade[0];
            for (String p : parts) {
                try {
                    int id = Integer.parseInt(p.trim());
                    if (id >= 0 && id < esps.length)
                        sel = appendEspecialidade(sel, esps[id]);
                } catch (Exception ignored) {
                }
            }
            s.setEspecialidades(sel);
        }
        sintomas[idx] = s;
        System.out.println("Atualizado.");
        return sintomas;
    }

    /**
     * Elimina sintoma por índice.
     *
     * @param sintomas array de Sintoma
     * @return array após remoção
     */
    private Sintoma[] eliminar(Sintoma[] sintomas) {
        listar(sintomas);
        if (sintomas.length == 0)
            return sintomas;
        System.out.print("Índice a eliminar: ");
        int idx = lerIntComDefault(-1);
        if (idx < 0 || idx >= sintomas.length)
            return sintomas;
        Sintoma[] r = new Sintoma[sintomas.length - 1];
        int j = 0;
        for (int i = 0; i < sintomas.length; i++)
            if (i != idx)
                r[j++] = sintomas[i];
        System.out.println("Eliminado.");
        return r;
    }

    private Sintoma[] trimArray(Sintoma[] arr) {
        int count = 0;
        for (Sintoma s : arr)
            if (s != null)
                count++;
        Sintoma[] r = new Sintoma[count];
        int j = 0;
        for (Sintoma s : arr)
            if (s != null)
                r[j++] = s;
        return r;
    }

    // Helpers
    private Especialidade[] appendEspecialidade(Especialidade[] arr, Especialidade item) {
        Especialidade[] r = new Especialidade[arr.length + 1];
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
