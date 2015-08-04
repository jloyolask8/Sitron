/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.entities.enums;

import com.sitron.persistence.entities.Priority;


/**
 *
 * @author jorge
 */
public enum EnumPrioridad {

    BAJA(new Priority("BAJA", "BAJA", "Prioridad Baja: No hay apuro por la solucion al problema.", 72)),
    MEDIA(new Priority("MEDIA", "MEDIA", "Prioridad Media: Necesito una solucion pero puedo esperar en pro de la calidad del servicio.", 48)),
    ALTA(new Priority("ALTA", "ALTA", "Prioridad alta: El problema es importante pero no crítico.", 24)),
    MAXIMA(new Priority("MAXIMA", "MAXIMA", "Prioridad Maxima: El problema es crítico.", 4));
    
    private Priority prioridad;

    private EnumPrioridad(Priority prioridad) {
        this.prioridad = prioridad;
    }

    public Priority getPrioridad() {
        return prioridad;
    }
}
