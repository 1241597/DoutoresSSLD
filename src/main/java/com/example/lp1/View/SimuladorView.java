package com.example.lp1.View;

import com.example.lp1.BLL.AtribuicaoBLL;
import com.example.lp1.BLL.ConfiguracaoSimuladorBLL;
import com.example.lp1.BLL.NotificacaoBLL;
import com.example.lp1.BLL.SimuladorBLL;
import com.example.lp1.BLL.TriagemBLL;
import com.example.lp1.Model.Especialidade;
import com.example.lp1.Model.Medico;
import com.example.lp1.Model.Sintoma;
import com.example.lp1.Model.Utente;
import com.example.lp1.Utils.Enums.StatusUtente;
import com.example.lp1.Utils.Enums.nivelUrgencia;
import com.example.lp1.Utils.Utils;

import java.util.Scanner;

/**
 * Interface principal do simulador de urgência hospitalar
 */
public class SimuladorView {
    private SimuladorBLL simulador;
    private TriagemBLL triagemBLL;
    private AtribuicaoBLL atribuicaoBLL;
    private NotificacaoBLL notificacaoBLL;
    private ConfiguracaoSimuladorBLL configuracaoBLL;

    private Utente[] utentes;
    private int numUtentes;
    private Medico[] medicos;
    private Sintoma[] sintomas;
    private Especialidade[] especialidades;

    private Scanner scanner;

    public SimuladorView() {
        this.simulador = new SimuladorBLL();
        this.triagemBLL = new TriagemBLL();
        this.atribuicaoBLL = new AtribuicaoBLL();
        this.notificacaoBLL = new NotificacaoBLL();
        this.configuracaoBLL = new ConfiguracaoSimuladorBLL();

        this.utentes = new Utente[10];
        this.numUtentes = 0;
        this.scanner = new Scanner(System.in);

        // Carregar dados do sistema
        carregarDados();
    }

    private void carregarDados() {
        Utils utils = new Utils();
        utils.carregarFicheiros();

        this.medicos = utils.getMedicos();
        this.sintomas = utils.getSintomas();
        this.especialidades = utils.getEspecialidades();

        System.out.println("[SIMULADOR] Dados carregados: " +
                medicos.length + " médicos, " +
                sintomas.length + " sintomas, " +
                especialidades.length + " especialidades");
    }

    public void iniciar() {
        int opcao = -1;

        do {
            mostrarCabecalho();
            mostrarMenu();

            try {
                String input = scanner.nextLine();
                opcao = input.isEmpty() ? -1 : Integer.parseInt(input);
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    registarUtente();
                    break;
                case 2:
                    chamarParaTriagem();
                    break;
                case 3:
                    atribuirUtenteAMedico();
                    break;
                case 4:
                    avancarTempo();
                    break;
                case 5:
                    verSalaEspera();
                    break;
                case 6:
                    verEstadoMedicos();
                    break;
                case 7:
                    verNotificacoes();
                    break;
                case 0:
                    System.out.println(">> A voltar ao menu principal...");
                    break;
                default:
                    System.out.println("❌ Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void mostrarCabecalho() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   SIMULADOR DE URGÊNCIA HOSPITALAR        ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.printf("⏰ Hora Atual: %d/24", simulador.getHoraAtual());

        int notificacoesPendentes = notificacaoBLL.contarNotificacoes();
        if (notificacoesPendentes > 0) {
            System.out.printf(" | 🔔 %d notificações", notificacoesPendentes);
        }
        System.out.println("\n");
    }

    private void mostrarMenu() {
        System.out.println("=== MENU PRINCIPAL ===");
        System.out.println("1. 👤 Registar novo utente");
        System.out.println("2. 🏥 Chamar utente para triagem");
        System.out.println("3. 👨‍⚕️ Atribuir utente a médico");
        System.out.println("4. ⏩ Avançar tempo");
        System.out.println("5. 🪑 Ver sala de espera");
        System.out.println("6. 👨‍⚕️ Ver estado dos médicos");
        System.out.println("7. 🔔 Ver notificações");
        System.out.println("0. ↩️  Voltar");
        System.out.print("\nEscolha: ");
    }

    private void registarUtente() {
        System.out.println("\n=== REGISTAR NOVO UTENTE ===");
        System.out.print("Nome do utente: ");
        String nome = scanner.nextLine();

        if (nome.isEmpty()) {
            System.out.println("❌ Nome não pode estar vazio.");
            return;
        }

        Utente novoUtente = new Utente(nome, simulador.getHoraAtual());

        // Expandir array se necessário
        if (numUtentes >= utentes.length) {
            Utente[] novoArray = new Utente[utentes.length * 2];
            for (int i = 0; i < utentes.length; i++) {
                novoArray[i] = utentes[i];
            }
            utentes = novoArray;
        }
        utentes[numUtentes++] = novoUtente;

        System.out.println("✅ Utente " + nome + " registado com sucesso!");
        System.out.println("   Hora de chegada: " + simulador.getHoraAtual());
        System.out.println("   Status: " + novoUtente.getStatus().getDescricao());
    }

    private void chamarParaTriagem() {
        System.out.println("\n=== CHAMAR UTENTE PARA TRIAGEM ===");

        // Listar utentes aguardando triagem
        Utente[] aguardandoTriagem = new Utente[numUtentes];
        int countTriagem = 0;
        for (int i = 0; i < numUtentes; i++) {
            if (utentes[i].getStatus() == StatusUtente.AGUARDANDO_TRIAGEM) {
                aguardandoTriagem[countTriagem++] = utentes[i];
            }
        }

        if (countTriagem == 0) {
            System.out.println("ℹ️ Não há utentes aguardando triagem.");
            return;
        }

        System.out.println("Utentes na fila:");
        for (int i = 0; i < countTriagem; i++) {
            Utente u = aguardandoTriagem[i];
            System.out.printf("%d. %s (chegou na hora %d)\n",
                    i + 1, u.getNome(), u.getHoraChegada());
        }

        System.out.print("\nSelecione o utente (0 para cancelar): ");
        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            if (escolha == 0)
                return;

            if (escolha < 1 || escolha > countTriagem) {
                System.out.println("❌ Seleção inválida.");
                return;
            }

            Utente selecionado = aguardandoTriagem[escolha - 1];
            realizarTriagem(selecionado);

        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
        }
    }

    private void realizarTriagem(Utente utente) {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║          TRIAGEM - " + utente.getNome());
        System.out.println("╚════════════════════════════════════════════╝");

        utente.setStatus(StatusUtente.EM_TRIAGEM);
        Sintoma[] sintomasSelecionados = new Sintoma[20];
        int numSintomas = 0;

        boolean continuar = true;
        while (continuar) {
            System.out.println("\n--- Sintomas selecionados: " + numSintomas + " ---");

            System.out.println("\n1. 🔍 Pesquisar e adicionar sintoma");
            System.out.println("2. ➕ Criar novo sintoma");
            System.out.println("3. ✅ Concluir triagem");
            System.out.print("Escolha: ");

            try {
                int opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        numSintomas = pesquisarEAdicionarSintoma(sintomasSelecionados, numSintomas);
                        break;
                    case 2:
                        numSintomas = criarNovoSintoma(sintomasSelecionados, numSintomas);
                        break;
                    case 3:
                        continuar = false;
                        break;
                    default:
                        System.out.println("❌ Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida.");
            }
        }

        // Finalizar triagem
        if (numSintomas == 0) {
            System.out.println("⚠️ Nenhum sintoma selecionado. Triagem cancelada.");
            utente.setStatus(StatusUtente.AGUARDANDO_TRIAGEM);
            return;
        }

        Sintoma[] arrayStr = new Sintoma[numSintomas];
        for (int i = 0; i < numSintomas; i++) {
            arrayStr[i] = sintomasSelecionados[i];
        }
        utente.setSintomas(arrayStr);

        // Calcular urgência
        nivelUrgencia urgencia = triagemBLL.calcularNivelUrgencia(arrayStr);
        utente.setNivelUrgenciaCalculado(urgencia);

        // Calcular especialidade
        Especialidade especialidade = triagemBLL.calcularEspecialidade(arrayStr);
        utente.setEspecialidadeCalculada(especialidade);

        utente.setHoraTriagem(simulador.getHoraAtual());
        utente.setStatus(StatusUtente.AGUARDANDO_MEDICO);

        // Mostrar resultados
        System.out.println("\n✅ TRIAGEM CONCLUÍDA");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("Utente: " + utente.getNome());
        System.out.println("Sintomas: " + numSintomas);
        System.out.println("Nível de Urgência: " + urgencia);
        System.out.println(
                "Especialidade: " + (especialidade != null ? especialidade.getCodigo() + " - " + especialidade.getNome()
                        : "Não determinada"));

        // Sugestão estatística
        String sugestao = triagemBLL.sugerirEspecialidadePorEstatistica(arrayStr, especialidades);
        if (sugestao != null) {
            System.out.println("💡 " + sugestao);
        }

        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    private int pesquisarEAdicionarSintoma(Sintoma[] sintomasSelecionados, int numSintomas) {
        System.out.print("\n🔍 Digite o nome do sintoma (ou parte): ");
        String busca = scanner.nextLine().toLowerCase();

        if (busca.isEmpty()) {
            System.out.println("❌ Busca vaz ia.");
            return numSintomas;
        }

        // Busca fuzzy: encontrar sintomas que contenham a string
        Sintoma[] encontrados = new Sintoma[sintomas.length];
        int countEncontrados = 0;
        for (Sintoma s : sintomas) {
            if (s != null && s.getNome().toLowerCase().contains(busca)) {
                encontrados[countEncontrados++] = s;
            }
        }

        if (countEncontrados == 0) {
            System.out.println("❌ Nenhum sintoma encontrado com '" + busca + "'");
            System.out.println("💡 Dica: Tente criar um novo sintoma (opção 2)");
            return numSintomas;
        }

        System.out.println("\n📋 Sintomas encontrados:");
        for (int i = 0; i < countEncontrados; i++) {
            Sintoma s = encontrados[i];
            System.out.printf("%d. %s [%s]", i + 1, s.getNome(), s.getUrgencia());

            if (s.getEspecialidades() != null && s.getEspecialidades().length > 0) {
                System.out.print(" - Especialidades: ");
                for (int j = 0; j < s.getEspecialidades().length; j++) {
                    if (s.getEspecialidades()[j] != null) {
                        System.out.print(s.getEspecialidades()[j].getCodigo());
                        if (j < s.getEspecialidades().length - 1)
                            System.out.print(", ");
                    }
                }
            }
            System.out.println();
        }

        System.out.print("\nSelecione o sintoma (0 para cancelar): ");
        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            if (escolha == 0)
                return numSintomas;

            if (escolha < 1 || escolha > countEncontrados) {
                System.out.println("❌ Seleção inválida.");
                return numSintomas;
            }

            Sintoma selecionado = encontrados[escolha - 1];
            sintomasSelecionados[numSintomas++] = selecionado;
            System.out.println("✅ Sintoma '" + selecionado.getNome() + "' adicionado!");
            return numSintomas;

        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
            return numSintomas;
        }
    }

    private int criarNovoSintoma(Sintoma[] sintomasSelecionados, int numSintomas) {
        System.out.println("\n=== CRIAR NOVO SINTOMA ===");
        System.out.print("Nome do sintoma: ");
        String nome = scanner.nextLine();

        if (nome.isEmpty()) {
            System.out.println("❌ Nome não pode estar vazio.");
            return numSintomas;
        }

        System.out.println("Nível de urgência:");
        System.out.println("1. VERDE (leve)");
        System.out.println("2. LARANJA (moderada)");
        System.out.println("3. VERMELHA (urgente)");
        System.out.print("Escolha: ");

        nivelUrgencia urgencia;
        try {
            int nivelEscolhido = Integer.parseInt(scanner.nextLine());
            urgencia = nivelUrgencia.fromNivelUrgencia(nivelEscolhido);
        } catch (Exception e) {
            System.out.println("❌ Nível inválido. Usando VERDE por padrão.");
            urgencia = nivelUrgencia.VERDE;
        }

        // Selecionar especialidades
        System.out.println("\nEspecialidades disponíveis:");
        for (int i = 0; i < especialidades.length; i++) {
            System.out.printf("%d. %s - %s\n",
                    i + 1,
                    especialidades[i].getCodigo(),
                    especialidades[i].getNome());
        }

        System.out.print("Selecione especialidade (0 para nenhuma): ");
        Especialidade[] esps = new Especialidade[0];

        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            if (escolha > 0 && escolha <= especialidades.length) {
                esps = new Especialidade[] { especialidades[escolha - 1] };
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Nenhuma especialidade selecionada.");
        }

        Sintoma novoSintoma = new Sintoma(nome, urgencia, esps);
        sintomasSelecionados[numSintomas++] = novoSintoma;

        System.out.println("✅ Novo sintoma '" + nome + "' criado e adicionado!");
        return numSintomas;
    }

    private void atribuirUtenteAMedico() {
        System.out.println("\n=== ATRIBUIR UTENTE A MÉDICO ===");

        // Listar utentes aguardando médico
        Utente[] aguardandoMedico = new Utente[numUtentes];
        int countAguardando = 0;
        for (int i = 0; i < numUtentes; i++) {
            if (utentes[i].getStatus() == StatusUtente.AGUARDANDO_MEDICO) {
                aguardandoMedico[countAguardando++] = utentes[i];
            }
        }

        if (countAguardando == 0) {
            System.out.println("ℹ️ Não há utentes aguardando médico.");
            return;
        }

        System.out.println("Utentes aguardando:");
        for (int i = 0; i < countAguardando; i++) {
            Utente u = aguardandoMedico[i];
            System.out.printf("%d. %s - Urgência: %s - Especialidade: %s\n",
                    i + 1,
                    u.getNome(),
                    u.getNivelUrgenciaCalculado(),
                    u.getEspecialidadeCalculada() != null ? u.getEspecialidadeCalculada().getCodigo() : "N/D");
        }

        System.out.print("\nSelecione o utente (0 para cancelar): ");
        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            if (escolha == 0)
                return;

            if (escolha < 1 || escolha > countAguardando) {
                System.out.println("❌ Seleção inválida.");
                return;
            }

            Utente selecionado = aguardandoMedico[escolha - 1];

            System.out.println("\n1. Atribuição automática");
            System.out.println("2. Atribuição manual");
            System.out.print("Escolha: ");

            int tipoAtribuicao = Integer.parseInt(scanner.nextLine());

            if (tipoAtribuicao == 1) {
                atribuicaoAutomatica(selecionado);
            } else if (tipoAtribuicao == 2) {
                atribuicaoManual(selecionado);
            } else {
                System.out.println("❌ Opção inválida.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
        }
    }

    private void atribuicaoAutomatica(Utente utente) {
        if (utente.getEspecialidadeCalculada() == null) {
            System.out.println("❌ Não é possível atribuição automática.");
            System.out.println("   Especialidade não determinada. Use atribuição manual.");
            return;
        }

        // Calcular duração baseada na urgência
        int duracao = configuracaoBLL.getDuracaoPorUrgencia(utente.getNivelUrgenciaCalculado());

        boolean sucesso = atribuicaoBLL.atribuirAutomaticamente(
                utente, medicos, simulador.getHoraAtual(), duracao);

        if (sucesso) {
            System.out.println("✅ Utente atribuído automaticamente!");
            // Encontrar o médico que ficou com o utente
            for (Medico m : medicos) {
                if (m != null && m.getUtenteAtual() == utente) {
                    System.out.println("   Médico: Dr(a). " + m.getNome());
                    System.out.println("   Especialidade: " + m.getEspecialidade().getCodigo());
                    System.out.println("   Duração prevista: " + duracao + " unidades de tempo");
                    System.out.println("   Fim previsto: hora " + m.getHoraFimPrevista());
                    break;
                }
            }
        } else {
            System.out.println("❌ Não há médicos disponíveis para a especialidade " +
                    utente.getEspecialidadeCalculada().getCodigo());
            System.out.println("   Utente permanece aguardando.");
        }
    }

    private void atribuicaoManual(Utente utente) {
        Medico[] disponiveis = atribuicaoBLL.listarMedicosDisponiveis(
                medicos, simulador.getHoraAtual());

        if (disponiveis.length == 0) {
            System.out.println("❌ Não há médicos disponíveis no momento.");
            return;
        }

        System.out.println("\nMédicos disponíveis:");
        for (int i = 0; i < disponiveis.length; i++) {
            Medico m = disponiveis[i];
            System.out.printf("%d. Dr(a). %s - %s (Turno: %.0f-%.0fh)\n",
                    i + 1,
                    m.getNome(),
                    m.getEspecialidade().getCodigo(),
                    m.getHoraEntrada(),
                    m.getHoraSaida());
        }

        System.out.print("\nSelecione o médico (0 para cancelar): ");
        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            if (escolha == 0)
                return;

            if (escolha < 1 || escolha > disponiveis.length) {
                System.out.println("❌ Seleção inválida.");
                return;
            }

            Medico medico = disponiveis[escolha - 1];

            // Calcular duração baseada na urgência
            int duracao = configuracaoBLL.getDuracaoPorUrgencia(utente.getNivelUrgenciaCalculado());

            boolean sucesso = atribuicaoBLL.atribuirManualmente(
                    utente, medico, simulador.getHoraAtual(), duracao);

            if (sucesso) {
                System.out.println("✅ Utente atribuído ao Dr(a). " + medico.getNome() + "!");
                System.out.println("   Duração prevista: " + duracao + " unidades de tempo");
                System.out.println("   Fim previsto: hora " + medico.getHoraFimPrevista());
            } else {
                System.out.println("❌ Erro na atribuição.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
        }
    }

    private void avancarTempo() {
        int horaAnterior = simulador.getHoraAtual();
        simulador.avancarTempo();
        int horaAtual = simulador.getHoraAtual();

        System.out.println("\n⏩ TEMPO AVANÇADO");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.printf("De: %d/24 → Para: %d/24\n", horaAnterior, horaAtual);

        if (horaAtual == 1 && horaAnterior == 24) {
            System.out.println("🌅 Novo dia iniciado!");
        }

        // VERIFICAR E FINALIZAR ATENDIMENTOS AUTOMATICAMENTE
        String[] finalizados = atribuicaoBLL.verificarEFinalizarAtendimentos(medicos, horaAtual);
        if (finalizados.length > 0) {
            System.out.println("\n✅ ATENDIMENTOS FINALIZADOS AUTOMATICAMENTE:");
            for (String finalizado : finalizados) {
                System.out.println("  - " + finalizado);
                notificacaoBLL.adicionarNotificacao("✅ Atendimento finalizado: " + finalizado);
            }
        }

        // Atualizar disponibilidade dos médicos
        atribuicaoBLL.atualizarDisponibilidade(medicos, horaAtual);

        // Verificar eventos e gerar notificações
        Utente[] arrayUtentes = new Utente[numUtentes];
        for (int i = 0; i < numUtentes; i++) {
            arrayUtentes[i] = utentes[i];
        }
        notificacaoBLL.verificarEventosTemporais(medicos, arrayUtentes, horaAtual);

        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Mostrar notificações imediatamente
        String[] notificacoes = notificacaoBLL.obterNotificacoes();
        if (notificacoes.length > 0) {
            System.out.println("\n🔔 NOTIFICAÇÕES:");
            for (String not : notificacoes) {
                System.out.println("  " + not);
            }
        }
    }

    private void verSalaEspera() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║            SALA DE ESPERA                  ║");
        System.out.println("╚════════════════════════════════════════════╝");

        if (numUtentes == 0) {
            System.out.println("ℹ️ Não há utentes registados.");
            return;
        }

        System.out.printf("%-20s %-15s %-12s %-20s %s\n",
                "Nome", "Urgência", "Especialidade", "Status", "Hora Chegada");
        System.out.println("─".repeat(85));

        for (int i = 0; i < numUtentes; i++) {
            Utente u = utentes[i];
            String urgencia = u.getNivelUrgenciaCalculado() != null ? u.getNivelUrgenciaCalculado().toString() : "N/A";
            String especialidade = u.getEspecialidadeCalculada() != null ? u.getEspecialidadeCalculada().getCodigo()
                    : "N/A";

            System.out.printf("%-20s %-15s %-12s %-20s %d\n",
                    u.getNome(),
                    urgencia,
                    especialidade,
                    u.getStatus().getDescricao(),
                    u.getHoraChegada());
        }

        System.out.println("\nTotal: " + numUtentes + " utentes");
    }

    private void verEstadoMedicos() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║         ESTADO DOS MÉDICOS                 ║");
        System.out.println("╚════════════════════════════════════════════╝");

        if (medicos == null || medicos.length == 0) {
            System.out.println("ℹ️ Não há médicos registados.");
            return;
        }

        System.out.printf("%-20s %-12s %-10s %-12s %s\n",
                "Nome", "Especialidade", "Turno", "Disponível", "Atendendo");
        System.out.println("─".repeat(80));

        for (Medico m : medicos) {
            if (m != null) {
                String turno = String.format("%.0f-%.0fh",
                        m.getHoraEntrada(), m.getHoraSaida());
                String disponivel = m.isDisponivel() ? "✅ Sim" : "❌ Não";
                String atendendo = m.getUtenteAtual() != null ? m.getUtenteAtual().getNome() : "-";

                // Destacar se médico está fora do horário mas atendendo
                if (m.getUtenteAtual() != null &&
                        !m.isDisponivelNaHora(simulador.getHoraAtual())) {
                    atendendo += " ⚠️";
                }

                System.out.printf("%-20s %-12s %-10s %-12s %s\n",
                        m.getNome(),
                        m.getEspecialidade().getCodigo(),
                        turno,
                        disponivel,
                        atendendo);
            }
        }

        System.out.println("\nHora atual: " + simulador.getHoraAtual() + "/24");
    }

    private void verNotificacoes() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║           NOTIFICAÇÕES                     ║");
        System.out.println("╚════════════════════════════════════════════╝");

        String[] notificacoes = notificacaoBLL.obterELimparNotificacoes();

        if (notificacoes.length == 0) {
            System.out.println("ℹ️ Não há notificações pendentes.");
            return;
        }

        for (int i = 0; i < notificacoes.length; i++) {
            System.out.println((i + 1) + ". " + notificacoes[i]);
        }

        System.out.println("\n✅ Notificações limpas.");
    }
}
