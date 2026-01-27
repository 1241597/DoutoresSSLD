package com.example.lp1.View;

import com.example.lp1.DAL.MedicoDAL;
import com.example.lp1.DAL.EspecialidadeDAL;
import com.example.lp1.Model.Medico;
import com.example.lp1.Model.Especialidade;
import java.util.Scanner;

/**
 * Interface de consola para gestão de Médicos.
 * Permite listar, criar, atualizar, eliminar e gravar médicos.
 * Valida horas de entrada/saída (0..23).
 */
public class MedicosView {

    private MedicoDAL medicoDAL = new MedicoDAL();
    private EspecialidadeDAL espDAL = new EspecialidadeDAL();
    private Scanner scanner = new Scanner(System.in);

    /**
     * Inicia menu de médicos.
     */
    public void iniciar() {
        Medico[] medicos = medicoDAL.carregarMedicos();
        Especialidade[] especialidades = espDAL.carregarEspecialidades();

        // tenta mapear códigos de especialidade para objetos carregados
        for (int i = 0; i < medicos.length; i++) {
            Especialidade e = medicos[i].getEspecialidade();
            if (e != null) {
                for (Especialidade ex : especialidades) {
                    if (ex.getCodigo().equalsIgnoreCase(e.getCodigo())) {
                        medicos[i].setEspecialidade(ex);
                        break;
                    }
                }
            }
        }

        int opcao = -1;
        do {
            System.out.println("\n--- GESTÃO DE MÉDICOS ---");
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
                case 1: listarMedicos(medicos); break;
                case 2: medicos = criarMedico(medicos, especialidades); break;
                case 3: medicos = atualizarMedico(medicos, especialidades); break;
                case 4: medicos = eliminarMedico(medicos); break;
                case 5:
                    medicoDAL.gravarMedicos(trimArray(medicos));
                    medicos = medicoDAL.carregarMedicos();
                    // remapeia após gravar
                    for (int i = 0; i < medicos.length; i++) {
                        Especialidade e2 = medicos[i].getEspecialidade();
                        if (e2 != null) {
                            for (Especialidade ex : especialidades) {
                                if (ex.getCodigo().equalsIgnoreCase(e2.getCodigo())) {
                                    medicos[i].setEspecialidade(ex);
                                    break;
                                }
                            }
                        }
                    }
                    System.out.println("Alterações gravadas.");
                    break;
                case 0: System.out.println("A voltar..."); break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    /**
     * Mostra a lista de médicos.
     *
     * @param medicos array de Medico
     */
    private void listarMedicos(Medico[] medicos) {
        if (medicos.length == 0) { System.out.println("Sem médicos registados."); return; }
        for (int i = 0; i < medicos.length; i++) System.out.println(i + ". " + medicos[i].toString());
    }

    /**
     * Cria novo médico com validação de horas (0..23).
     *
     * @param medicos array atual de Medico
     * @param especialidades array de Especialidade disponível
     * @return array com o novo médico adicionado
     */
    private Medico[] criarMedico(Medico[] medicos, Especialidade[] especialidades) {
        System.out.print("Nome: "); String nome = scanner.nextLine();
        if (especialidades.length == 0) { System.out.println("Não existem especialidades. Crie uma antes."); return medicos; }
        System.out.println("Escolha especialidade (índice):");
        for (int i = 0; i < especialidades.length; i++) System.out.println(i + ". " + especialidades[i].getCodigo() + " - " + especialidades[i].getNome());
        int idxEsp = lerIntComDefault(-1);
        if (idxEsp < 0 || idxEsp >= especialidades.length) { System.out.println("Especialidade inválida."); return medicos; }

        // Ler horas obrigatórias com validação 0..23
        double hEnt = lerHoraObrigatoriaEntre(0, 23, "Hora Entrada (0-23, ex: 8): ");
        double hSai = lerHoraObrigatoriaEntre(0, 23, "Hora Saída (0-23, ex: 17): ");

        System.out.print("Salário por hora: "); double salario = lerDoubleComDefault(10.0);
        Medico novo = new Medico(nome, especialidades[idxEsp], hEnt, hSai, salario);
        medicos = appendMedico(medicos, novo);
        System.out.println("Médico criado.");
        return medicos;
    }

    /**
     * Atualiza médico existente (enter para manter).
     *
     * @param medicos array de Medico
     * @param especialidades array de Especialidade
     * @return array atualizado
     */
    private Medico[] atualizarMedico(Medico[] medicos, Especialidade[] especialidades) {
        listarMedicos(medicos);
        if (medicos.length == 0) return medicos;

        System.out.print("Índice do médico a atualizar: ");
        int idx = lerIntComDefault(-1);
        if (idx < 0 || idx >= medicos.length) {
            System.out.println("Índice inválido.");
            return medicos;
        }

        Medico m = medicos[idx];
        System.out.println("Enter para manter valor atual.");

        System.out.print("Nome (" + m.getNome() + "): ");
        String nome = scanner.nextLine();
        if (!nome.isEmpty()) m.setNome(nome);

        System.out.println("Especialidades:");
        for (int i = 0; i < especialidades.length; i++) {
            System.out.println(i + ". " + especialidades[i].getCodigo() + " - " + especialidades[i].getNome());
        }
        System.out.print("Índice especialidade atual (" + (m.getEspecialidade() != null ? m.getEspecialidade().getCodigo() : "nenhuma") + "): ");
        String inEsp = scanner.nextLine();
        if (!inEsp.isEmpty()) {
            try {
                int ne = Integer.parseInt(inEsp);
                if (ne >= 0 && ne < especialidades.length) {
                    m.setEspecialidade(especialidades[ne]);
                }
            } catch (NumberFormatException ignored) {}
        }

        // Hora entrada: enter para manter ou pedir valor válido entre 0 e 23
        m.setHoraEntrada(lerHoraComEnterParaManter(m.getHoraEntrada(), "Hora Entrada (" + m.getHoraEntrada() + "): "));

        // Hora saída: enter para manter ou pedir valor válido entre 0 e 23
        m.setHoraSaida(lerHoraComEnterParaManter(m.getHoraSaida(), "Hora Saída (" + m.getHoraSaida() + "): "));

        System.out.print("Salário (" + m.getSalarioHora() + "): ");
        String sal = scanner.nextLine();
        if (!sal.isEmpty()) m.setSalarioHora(Double.parseDouble(sal));

        medicos[idx] = m;
        System.out.println("Médico atualizado.");
        return medicos;
    }

    /**
     * Elimina médico por índice.
     *
     * @param medicos array de Medico
     * @return array sem o médico eliminado
     */
    private Medico[] eliminarMedico(Medico[] medicos) {
        listarMedicos(medicos);
        if (medicos.length == 0) return medicos;
        System.out.print("Índice a eliminar: "); int idx = lerIntComDefault(-1);
        if (idx < 0 || idx >= medicos.length) { System.out.println("Índice inválido."); return medicos; }
        Medico[] r = new Medico[medicos.length - 1];
        int j = 0;
        for (int i = 0; i < medicos.length; i++) if (i != idx) r[j++] = medicos[i];
        System.out.println("Eliminado.");
        return r;
    }

    private Medico[] appendMedico(Medico[] arr, Medico item) {
        Medico[] r = new Medico[arr.length + 1];
        System.arraycopy(arr, 0, r, 0, arr.length);
        r[arr.length] = item;
        return r;
    }

    private Medico[] trimArray(Medico[] arr) {
        int count = 0;
        for (Medico m : arr) if (m != null) count++;
        Medico[] r = new Medico[count];
        int j = 0;
        for (Medico m : arr) if (m != null) r[j++] = m;
        return r;
    }

    private int lerIntComDefault(int def) {
        try { String s = scanner.nextLine(); return s.isEmpty() ? def : Integer.parseInt(s); } catch (Exception e) { return def; }
    }

    private double lerDoubleComDefault(double def) {
        try { String s = scanner.nextLine(); return s.isEmpty() ? def : Double.parseDouble(s); } catch (Exception e) { return def; }
    }

    // --- Novos helpers para validar horas ---
    /**
     * Lê hora obrigatória entre um intervalo, com prompt.
     *
     * @param min valor mínimo (inclusive)
     * @param max valor máximo (inclusive)
     * @param prompt mensagem a mostrar ao usuário
     * @return valor da hora lida
     */
    private double lerHoraObrigatoriaEntre(double min, double max, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine();
            try {
                double v = Double.parseDouble(s);
                if (v >= min && v <= max) return v;
                System.out.println("Valor inválido. Deve estar entre " + (int)min + " e " + (int)max + ".");
            } catch (Exception e) {
                System.out.println("Entrada inválida. Insira um número (ex: 8).");
            }
        }
    }

    /**
     * Lê hora com opção de manter valor atual (pressionar Enter).
     *
     * @param atual valor atual da hora
     * @param prompt mensagem a mostrar ao usuário
     * @return novo valor da hora
     */
    private double lerHoraComEnterParaManter(double atual, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine();
            if (s.isEmpty()) return atual;
            try {
                double v = Double.parseDouble(s);
                if (v >= 0 && v <= 23) return v;
                System.out.println("Valor inválido. Deve estar entre 0 e 23.");
            } catch (Exception e) {
                System.out.println("Entrada inválida. Insira um número ou pressione Enter para manter.");
            }
        }
    }
}
