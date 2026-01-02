package com.example.lp1;

import com.example.lp1.Model.medico;
import com.example.lp1.Utils.Utils;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Utils utils = new Utils();
        utils.carregarFicheiros();
        
        System.out.println("-----Simulador Dia a Dia-----");
        System.out.println("1 - Avançar Hora");
        System.out.println("2 - Inserir utente");
        System.out.println("3 - Chamar utente");
        System.out.println("4 - Listar utentes");
        System.out.println("5 - Listar médicos");
    }
}