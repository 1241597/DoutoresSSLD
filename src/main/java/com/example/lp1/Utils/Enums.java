package com.example.lp1.Utils;

public class Enums {
    public enum nivelUrgencia {
        VERDE(1), LARANJA(2), VERMELHA(3);

        private final int nivelUrgencia;

        nivelUrgencia(int nivelUrgencia) {
            this.nivelUrgencia = nivelUrgencia;
        }

        public int getNivelUrgencia() {
            return nivelUrgencia;
        }

        public static nivelUrgencia fromNivelUrgencia(int nivelUrgencia) {
            for (nivelUrgencia tipo : Enums.nivelUrgencia.values()) {
                if (tipo.getNivelUrgencia() == nivelUrgencia) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("Código inválido: " + nivelUrgencia);
        }
    }
}
