package com.sitron.persistence.entities.enums;

import com.sitron.persistence.entities.EstadoCaso;

/**
 *
 * @author jorge
 */
public enum EnumEstadoCaso {

    ESPERA_INFORME(new EstadoCaso("esperaInforme", "EN ESPERA INF",
            "Estado inicial de un caso cuando ha sido creado por el tecnico.", "FFFFFF", "5bc0de", true)),
    RAD_CON_OBSERVACION(new EstadoCaso("conObservacion", "CON OBSERVACION",
            "Con observación.", "FFFFFF", "f0ad4e", true)),
    
    REPETIR_RADIOGRAFIA(new EstadoCaso("repetir", "REPETIR",
            "Repetir Radiografía.", "FFFFFF", "d9534f", true)),
    
    EN_PROCESO(new EstadoCaso("enProceso", "EN PROCESO", "El Radiologo esta trabajando en el caso.", "FFFFFF", "f0ad4e", false)),
    
    INFORMADO(new EstadoCaso("informada", "INFORMADA", "Caso solucionado e informado. Respuesta enviada al tecnico/cliente y cerrado.", "FFFFFF", "5cb85c", false)),
    INVALIDO(new EstadoCaso("INVALIDA", "INVALIDA", "El caso no es valido.", "FFFFFF", "d9534f", false));

    private EstadoCaso estado;

    EnumEstadoCaso(EstadoCaso estado) {
        this.estado = estado;
    }

    public EstadoCaso getEstado() {
        return estado;
    }
}
