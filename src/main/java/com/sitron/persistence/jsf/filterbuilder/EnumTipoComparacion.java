package com.sitron.persistence.jsf.filterbuilder;


/**
 *
 * @author jorge
 */
public enum EnumTipoComparacion
{
    /**
     * EQUALS
     */
     
    EQ(new TipoComparacion("Es", "EQ", "=", "Igual: Verdadero, si el contenido del operando1 es igual al contenido del operando2.")),
    /**
     * NOT EQUALS "Es Distinto", "NE", "<>", "No Igual: Verdadero, si el contenido del operando1 no es igual al contenido del operando2."
     */
    NE(new TipoComparacion("No es", "NE", "<>", "No Igual: Verdadero, si el contenido del operando1 no es igual al contenido del operando2.")),
    /**
     * "Es Menor", "LT", "<", "Menor Que: Verdadero, si el contenido del operando1 es menor al contenido del operando2."
     */
    LT(new TipoComparacion("Es Menor", "LT", "<", "Menor Que: Verdadero, si el contenido del operando1 es menor al contenido del operando2.")),
    /**
     * "Es Mayor", "GT", ">", "Mayor Que: Verdadero, si el contenido del operando1 es mayor al contenido del operando2."
     */
    GT(new TipoComparacion("Es Mayor", "GT", ">", "Mayor Que: Verdadero, si el contenido del operando1 es mayor al contenido del operando2.")),
    /**
     * "Es Menor o Igual", "LE", "<=", "Menor o Igual: Verdadero, si el contenido del operando1 es menor o igual al contenido del operando2."
     */
    LE(new TipoComparacion("Es Menor o Igual", "LE", "<=", "Menor o Igual: Verdadero, si el contenido del operando1 es menor o igual al contenido del operando2.")),
    /**
     * "Es Mayor o Igual", "GE", ">=", "Mayor o Igual: Verdadero, si el contenido del operando1 es mayor o igual al contenido del operando2."
     */
    GE(new TipoComparacion("Es Mayor o Igual", "GE", ">=", "Mayor o Igual: Verdadero, si el contenido del operando1 es mayor o igual al contenido del operando2.")),
    /**
     * "Contiene", "CO", "CO", "Verdadero si contiene la secuencia de caracteres dada"
     */
    CO(new TipoComparacion("Contiene", "CO", "CO", "Verdadero si contiene la secuencia de caracteres dada")),   
 
    /**
     * "Sub Conjunto", "SC", "SC", "Verdadero si el valor del campo esta dentro del subconjunto pasado como parametro."
     */
    SC(new TipoComparacion("Es Cualquiera de", "SC", "SC", "Verdadero si el universo contiene la lista subconjunto pasado como parametro.")),
    /**
     * "Cambie A", "CT", "Change To", "Verdadero si el valor del campo cambio, y cambio al valor especificado."
     */
    CT(new TipoComparacion("Cambie A", "CT", "CT", "Verdadero si el valor del campo cambio, y cambio al valor especificado.")),
    /**
     * "Contiene Valores", "IM", "IM", "Verdadero si la lista a comparar contiene todas los valores separados por coma."
     */
    IM(new TipoComparacion("Contiene todas", "IM", "IM", "Verdadero si la lista a comparar contiene todas los valores separados por coma.")),
    /**
     * "Entre Fechas", "BW", "BW", "BW: Verdadero, si la fecha a comparar esta entre la fecha operando1 y la fecha operando2."
     */
    BW(new TipoComparacion("Entre Fechas", "BW", "BW", "BW: Verdadero, si la fecha a comparar esta entre la fecha operando1 y la fecha operando2."));

    private TipoComparacion comparacion;

    EnumTipoComparacion(TipoComparacion comparacion)
    {
        this.comparacion = comparacion;
    }

    public TipoComparacion getTipoComparacion()
    {
        return comparacion;
    }
}
