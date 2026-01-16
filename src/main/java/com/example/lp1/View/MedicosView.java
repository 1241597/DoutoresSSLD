package com.example.lp1.View;

import com.example.lp1.DAL.MedicoDAL;
import com.example.lp1.DAL.EspecialidadeDAL;
import com.example.lp1.Model.Medico;
import com.example.lp1.Model.Especialidade;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MedicosView {

    private MedicoDAL medicoDAL = new MedicoDAL();
    private EspecialidadeDAL espDAL = new EspecialidadeDAL();
    private Scanner scanner = new Scanner(System.in);

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
            System.out.println("1. Listar médicos");
            System.out.println("2. Criar médico");
            System.out.println("3. Atualizar médico");
            System.out.println("4. Eliminar médico");
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

    private void listarMedicos(Medico[] medicos) {
        if (medicos.length == 0) { System.out.println("Sem médicos registados."); return; }
        for (int i = 0; i < medicos.length; i++) System.out.println(i + ". " + medicos[i].toString());
    }

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
        medicos = appendToArray(medicos, novo);
        System.out.println("Médico criado.");
        return medicos;
    }

    private Medico[] atualizarMedico(Medico[] medicos, Especialidade[] especialidades) {
        // ...existing code up to leitura de nome/especialidade...
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

    private Medico[] eliminarMedico(Medico[] medicos) {
        listarMedicos(medicos);
        if (medicos.length == 0) return medicos;
        System.out.print("Índice a eliminar: "); int idx = lerIntComDefault(-1);
        if (idx < 0 || idx >= medicos.length) { System.out.println("Índice inválido."); return medicos; }
        List<Medico> l = new ArrayList<>();
        for (int i = 0; i < medicos.length; i++) if (i != idx) l.add(medicos[i]);
        System.out.println("Eliminado.");
        return l.toArray(new Medico[l.size()]);
    }

    private Medico[] appendToArray(Medico[] arr, Medico item) {
        List<Medico> l = new ArrayList<>();
        for (Medico m : arr) l.add(m);
        l.add(item);
        return l.toArray(new Medico[l.size()]);
    }

    private Medico[] trimArray(Medico[] arr) {
        List<Medico> l = new ArrayList<>();
        for (Medico m : arr) if (m != null) l.add(m);
        return l.toArray(new Medico[l.size()]);
    }

    private int lerIntComDefault(int def) {
        try { String s = scanner.nextLine(); return s.isEmpty() ? def : Integer.parseInt(s); } catch (Exception e) { return def; }
    }

    private double lerDoubleComDefault(double def) {
        try { String s = scanner.nextLine(); return s.isEmpty() ? def : Double.parseDouble(s); } catch (Exception e) { return def; }
    }

    // --- Novos helpers para validar horas ---
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
