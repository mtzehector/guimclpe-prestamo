/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author eduardo.loyo
 */
@Data
public class Sistra extends BaseModel {

    private String numFolio;
    private String idInstFinanciera;
    private String nss;
    private String grupoFamiliar;
    private String curp;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String clabe;
    private Double impPrestamo;
    private String numPlazo;
    private String fecAlta;
    private Double impMensual;
    private String nominaPrimerDescuento;
    private Double numTasaActual;
    private Double catPrestamo;
    private Double impRealPrestamo;
    private String imgCartaInstruccion;
}
