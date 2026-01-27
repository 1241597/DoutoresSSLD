package com.example.lp1.Utils;

import com.example.lp1.DAL.EspecialidadeDAL;
import com.example.lp1.DAL.MedicoDAL;
import com.example.lp1.DAL.SintomaDAL;
import com.example.lp1.Model.Especialidade;
import com.example.lp1.Model.Medico;
import com.example.lp1.Model.Sintoma;

public class Utils {

    // --- VARIÁVEIS DE INSTÂNCIA PARA GUARDAR OS DADOS ---
    // Como não há Repositorio, os dados ficam guardados dentro deste objeto Utils
    private Especialidade[] especialidades;
    private Medico[] medicos;
    private Sintoma[] sintomas;

    public Utils() {
        // Inicializar com arrays vazios para evitar NullPointerException
        this.especialidades = new Especialidade[0];
        this.medicos = new Medico[0];
        this.sintomas = new Sintoma[0];
    }

    /**
     * Método principal que chama as DALs e processa as ligações
     */
    public void carregarFicheiros() {
        // 1. Carregar Configuração para saber o separador
        com.example.lp1.DAL.ConfiguracaoDAL configDal = new com.example.lp1.DAL.ConfiguracaoDAL();
        com.example.lp1.Model.Configuracao config = configDal.carregarConfiguracao();
        String separador = config.getSeparadorFicheiro();

        System.out.println("[SISTEMA] A usar separador: '" + separador + "'");

        // 2. Instanciar as DALs com o separador
        EspecialidadeDAL dalEsp = new EspecialidadeDAL(separador);
        MedicoDAL dalMed = new MedicoDAL(separador);
        SintomaDAL dalSint = new SintomaDAL(separador);

        // 3. Carregar os dados para as variáveis locais
        // Usamos try-catch individual para permitir que a app carregue o que conseguir
        try {
            this.especialidades = dalEsp.carregarEspecialidades();
        } catch (RuntimeException e) {
            System.out.println("[Utils] Erro a ler Especialidades: " + e.getMessage());
            this.especialidades = new com.example.lp1.Model.Especialidade[0];
        }

        try {
            this.medicos = dalMed.carregarMedicos();
        } catch (RuntimeException e) {
            System.out.println("[Utils] Erro a ler Médicos: " + e.getMessage());
            this.medicos = new com.example.lp1.Model.Medico[0];
        }

        try {
            this.sintomas = dalSint.carregarSintomas();
        } catch (RuntimeException e) {
            System.out.println("[Utils] Erro a ler Sintomas: " + e.getMessage());
            this.sintomas = new com.example.lp1.Model.Sintoma[0];
        }

        // 4. Ligar os objetos
        associarMedicosAEspecialidades();
        associarSintomasAEspecialidades();

        System.out.println("Ficheiros carregados e processados.");
    }

    // --- MÉTODOS DE ASSOCIAÇÃO (PRIVADOS) ---

    private void associarMedicosAEspecialidades() {
        // Se não houver especialidades carregadas, não vale a pena continuar
        if (this.especialidades == null || this.especialidades.length == 0)
            return;

        for (int i = 0; i < this.medicos.length; i++) {
            Medico m = this.medicos[i];
            if (m != null) {
                // Código que veio do ficheiro (ex: "CARD")
                String codProcurar = m.getEspecialidade().getCodigo();

                // Procurar esse código no array de especialidades
                for (int j = 0; j < this.especialidades.length; j++) {
                    if (this.especialidades[j] != null &&
                            this.especialidades[j].getCodigo().equalsIgnoreCase(codProcurar)) {

                        // Substituir o objeto incompleto pelo objeto completo (com Nome)
                        m.setEspecialidade(this.especialidades[j]);
                        break;
                    }
                }
            }
        }
    }

    private void associarSintomasAEspecialidades() {
        if (this.especialidades == null || this.especialidades.length == 0)
            return;

        for (int i = 0; i < this.sintomas.length; i++) {
            Sintoma s = this.sintomas[i];
            if (s != null) {
                Especialidade[] espsDoSintoma = s.getEspecialidades();

                // Percorrer as especialidades deste sintoma
                for (int k = 0; k < espsDoSintoma.length; k++) {
                    if (espsDoSintoma[k] != null) {
                        String codProcurar = espsDoSintoma[k].getCodigo();

                        // Procurar na lista geral
                        for (int j = 0; j < this.especialidades.length; j++) {
                            if (this.especialidades[j] != null &&
                                    this.especialidades[j].getCodigo().equalsIgnoreCase(codProcurar)) {

                                // Atualizar o array dentro do sintoma
                                espsDoSintoma[k] = this.especialidades[j];
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Imprime uma mensagem na consola.
     * Centraliza o uso de System.out.println.
     *
     * @param mensagem O texto a ser exibido.
     */
    public static void mostrarMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    // --- GETTERS PARA ACESSO AOS DADOS ---

    public Especialidade[] getEspecialidades() {
        return especialidades;
    }

    public Medico[] getMedicos() {
        return medicos;
    }

    public Sintoma[] getSintomas() {
        return sintomas;
    }
}