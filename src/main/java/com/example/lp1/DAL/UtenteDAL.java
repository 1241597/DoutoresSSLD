package com.example.lp1.DAL;

import com.example.lp1.Model.Utente;
import com.example.lp1.Model.Sintoma;
import com.example.lp1.Model.Especialidade;
import com.example.lp1.Utils.Enums.nivelUrgencia;
import com.example.lp1.Utils.Enums.StatusUtente;
import java.io.*;

/**
 * Data Access Layer para operações de leitura e escrita de Utentes.
 * Os utentes são persistidos em Ficheiros/utentes.txt no formato:
 * Nome|Sintoma1,Sintoma2,...|NivelUrgencia|CodigoEspecialidade|DiaChegada|HoraChegada|HoraTriagem|HoraUltimaEscalacao|Status
 *
 */
public class UtenteDAL {

    private static final String CAMINHO_FICHEIRO = "Ficheiros/utentes.txt";
    private static final String SEPARADOR_CAMPOS = "|"; // Separa os campos principais
    private static final String SEPARADOR_SINTOMAS = ","; // Separa os sintomas dentro do campo sintomas

    /**
     * Formata um utente numa string para ser guardada no ficheiro.
     * Formato:
     * Nome|Sintoma1,Sintoma2,...|NivelUrgencia|CodigoEspecialidade|DiaChegada|HoraChegada|HoraTriagem|HoraUltimaEscalacao|Status
     *
     * @param u Utente a formatar
     * @return String formatada
     */
    private String formatarUtente(Utente u) {
        StringBuilder linha = new StringBuilder();

        // Campo 1: Nome
        linha.append(u.getNome() != null ? u.getNome() : "").append(SEPARADOR_CAMPOS);

        // Campo 2: Sintomas (separados por vírgula)
        if (u.getSintomas() != null && u.getSintomas().length > 0) {
            for (int i = 0; i < u.getSintomas().length; i++) {
                if (u.getSintomas()[i] != null) {
                    linha.append(u.getSintomas()[i].getNome());
                    if (i < u.getSintomas().length - 1) {
                        linha.append(SEPARADOR_SINTOMAS);
                    }
                }
            }
        }
        linha.append(SEPARADOR_CAMPOS);

        // Campo 3: Nível de Urgência
        linha.append(u.getNivelUrgenciaCalculado() != null ? u.getNivelUrgenciaCalculado().toString() : "")
                .append(SEPARADOR_CAMPOS);

        // Campo 4: Código da Especialidade
        linha.append(u.getEspecialidadeCalculada() != null ? u.getEspecialidadeCalculada().getCodigo() : "")
                .append(SEPARADOR_CAMPOS);

        // Campo 5: Dia de Chegada
        linha.append(u.getDiaChegada()).append(SEPARADOR_CAMPOS);

        // Campo 6: Hora de Chegada
        linha.append(u.getHoraChegada()).append(SEPARADOR_CAMPOS);

        // Campo 7: Hora de Triagem
        linha.append(u.getHoraTriagem()).append(SEPARADOR_CAMPOS);

        // Campo 8: Hora Última Escalação
        linha.append(u.getHoraUltimaEscalacao()).append(SEPARADOR_CAMPOS);

        // Campo 9: Status
        linha.append(u.getStatus() != null ? u.getStatus().toString() : "");

        return linha.toString();
    }

    /**
     * Carrega utentes do ficheiro (versão simplificada sem especialidades).
     *
     * @param sintomasDisponiveis array de Sintoma disponíveis para mapear nomes
     * @return array de Utente carregados (pode ser array vazio)
     */
    public Utente[] carregarUtentes(Sintoma[] sintomasDisponiveis) {
        return carregarUtentes(sintomasDisponiveis, new Especialidade[0]);
    }

    /**
     * Carrega utentes do ficheiro com todas as informações.
     *
     * @param sintomasDisponiveis       array de Sintoma disponíveis
     * @param especialidadesDisponiveis array de Especialidade disponíveis
     * @return array de Utente carregados (pode ser array vazio)
     */
    public Utente[] carregarUtentes(Sintoma[] sintomasDisponiveis, Especialidade[] especialidadesDisponiveis) {
        File f = new File(CAMINHO_FICHEIRO);
        if (!f.exists())
            return new Utente[0];

        Utente[] lista = new Utente[0];
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty())
                    continue;

                // Formato:
                // Nome|Sintoma1,Sintoma2|NivelUrgencia|CodigoEsp|DiaChegada|HoraChegada|HoraTriagem|HoraEscalacao|Status
                String[] campos = linha.split("\\|", -1); // -1 mantém campos vazios

                if (campos.length < 9) {
                    System.out.println("Linha inválida ignorada: " + linha);
                    continue;
                }

                // Campo 0: Nome
                String nome = campos[0].trim();

                // Campo 1: Sintomas
                Sintoma[] sintomas = new Sintoma[0];
                if (!campos[1].trim().isEmpty()) {
                    String[] nomesSintomas = campos[1].split(SEPARADOR_SINTOMAS);
                    for (String nomeSintoma : nomesSintomas) {
                        String sTrim = nomeSintoma.trim();
                        for (Sintoma sd : sintomasDisponiveis) {
                            if (sd.getNome().equalsIgnoreCase(sTrim)) {
                                sintomas = appendSintoma(sintomas, sd);
                                break;
                            }
                        }
                    }
                }

                // Criar utente
                Utente utente = new Utente(nome, sintomas);

                // Campo 2: Nível de Urgência
                if (!campos[2].trim().isEmpty()) {
                    try {
                        utente.setNivelUrgenciaCalculado(nivelUrgencia.valueOf(campos[2].trim()));
                    } catch (IllegalArgumentException e) {
                        // Ignorar se valor inválido
                    }
                }

                // Campo 3: Especialidade
                if (!campos[3].trim().isEmpty() && especialidadesDisponiveis != null) {
                    String codigoEsp = campos[3].trim();
                    for (Especialidade esp : especialidadesDisponiveis) {
                        if (esp != null && esp.getCodigo().equalsIgnoreCase(codigoEsp)) {
                            utente.setEspecialidadeCalculada(esp);
                            break;
                        }
                    }
                }

                // Campo 4: Dia de Chegada
                try {
                    utente.setDiaChegada(Integer.parseInt(campos[4].trim()));
                } catch (NumberFormatException e) {
                    utente.setDiaChegada(1);
                }

                // Campo 5: Hora de Chegada
                try {
                    utente.setHoraChegada(Integer.parseInt(campos[5].trim()));
                } catch (NumberFormatException e) {
                    utente.setHoraChegada(0);
                }

                // Campo 6: Hora de Triagem
                try {
                    utente.setHoraTriagem(Integer.parseInt(campos[6].trim()));
                } catch (NumberFormatException e) {
                    utente.setHoraTriagem(0);
                }

                // Campo 7: Hora Última Escalação
                try {
                    utente.setHoraUltimaEscalacao(Integer.parseInt(campos[7].trim()));
                } catch (NumberFormatException e) {
                    utente.setHoraUltimaEscalacao(0);
                }

                // Campo 8: Status
                if (!campos[8].trim().isEmpty()) {
                    try {
                        utente.setStatus(StatusUtente.valueOf(campos[8].trim()));
                    } catch (IllegalArgumentException e) {
                        utente.setStatus(StatusUtente.AGUARDANDO_TRIAGEM);
                    }
                }

                lista = appendUtente(lista, utente);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler utentes: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Grava o array de utentes no ficheiro (sobrescreve).
     *
     * @param lista array de Utente a gravar
     */
    public void gravarUtentes(Utente[] lista) {
        File f = new File(CAMINHO_FICHEIRO);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, false))) {
            for (Utente u : lista) {
                if (u == null)
                    continue;
                bw.write(formatarUtente(u));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao gravar utentes: " + e.getMessage());
        }
    }

    /**
     * Adiciona um novo utente ao ficheiro (anexa ao final).
     *
     * @param utente Utente a adicionar
     */
    public void adicionarUtente(Utente utente) {
        if (utente == null)
            return;

        File f = new File(CAMINHO_FICHEIRO);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, true))) {
            bw.write(formatarUtente(utente));
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao adicionar utente: " + e.getMessage());
        }
    }

    /**
     * Limpa todo o conteúdo do ficheiro de utentes.
     * Usado para resetar o sistema no início do simulador.
     */
    public void limparFicheiro() {
        File f = new File(CAMINHO_FICHEIRO);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, false))) {
            // Ficheiro vazio - apenas abre e fecha para limpar
        } catch (IOException e) {
            System.out.println("Erro ao limpar ficheiro de utentes: " + e.getMessage());
        }
    }

    // Helpers para arrays

    /**
     * Adiciona um Utente ao final de um array (cria novo array).
     *
     * @param arr  array original
     * @param item Utente a adicionar
     * @return novo array com o item adicionado
     */
    private Utente[] appendUtente(Utente[] arr, Utente item) {
        Utente[] r = new Utente[arr.length + 1];
        System.arraycopy(arr, 0, r, 0, arr.length);
        r[arr.length] = item;
        return r;
    }

    /**
     * Adiciona um Sintoma ao final de um array (cria novo array).
     *
     * @param arr  array original
     * @param item Sintoma a adicionar
     * @return novo array com o item adicionado
     */
    private Sintoma[] appendSintoma(Sintoma[] arr, Sintoma item) {
        Sintoma[] r = new Sintoma[arr.length + 1];
        System.arraycopy(arr, 0, r, 0, arr.length);
        r[arr.length] = item;
        return r;
    }
}
