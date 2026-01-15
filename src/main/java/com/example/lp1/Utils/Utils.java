package com.example.lp1.Utils;

import com.example.lp1.DAL.especialidadeDAL;
import com.example.lp1.DAL.medicoDAL;
import com.example.lp1.DAL.sintomaDAL;
import com.example.lp1.Model.especialidade;
import com.example.lp1.Model.medico;
import com.example.lp1.Model.sintoma;

public class Utils {

    // --- VARIÁVEIS DE INSTÂNCIA PARA GUARDAR OS DADOS ---
    // Como não há Repositorio, os dados ficam guardados dentro deste objeto Utils
    private especialidade[] especialidades;
    private medico[] medicos;
    private sintoma[] sintomas;

    public Utils() {
        // Inicializar com arrays vazios para evitar NullPointerException
        this.especialidades = new especialidade[0];
        this.medicos = new medico[0];
        this.sintomas = new sintoma[0];
    }

    /**
     * Método principal que chama as DALs e processa as ligações
     */
    public void carregarFicheiros() {
        // 1. Instanciar as DALs
        especialidadeDAL dalEsp = new especialidadeDAL();
        medicoDAL dalMed = new medicoDAL();
        sintomaDAL dalSint = new sintomaDAL();

        // 2. Carregar os dados para as variáveis locais
        this.especialidades = dalEsp.carregarEspecialidades();
        this.medicos = dalMed.carregarMedicos();
        this.sintomas = dalSint.carregarSintomas();

        // 3. Ligar os objetos (Substituir códigos por objetos reais)
        associarMedicosAEspecialidades();
        associarSintomasAEspecialidades();

        System.out.println("Ficheiros carregados e processados.");
    }

    // --- MÉTODOS DE ASSOCIAÇÃO (PRIVADOS) ---

    private void associarMedicosAEspecialidades() {
        // Se não houver especialidades carregadas, não vale a pena continuar
        if (this.especialidades == null || this.especialidades.length == 0) return;

        for (int i = 0; i < this.medicos.length; i++) {
            medico m = this.medicos[i];
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
        if (this.especialidades == null || this.especialidades.length == 0) return;

        for (int i = 0; i < this.sintomas.length; i++) {
            sintoma s = this.sintomas[i];
            if (s != null) {
                especialidade[] espsDoSintoma = s.getEspecialidades();

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

    public especialidade[] getEspecialidades() {
        return especialidades;
    }

    public medico[] getMedicos() {
        return medicos;
    }

    public sintoma[] getSintomas() {
        return sintomas;
    }
}