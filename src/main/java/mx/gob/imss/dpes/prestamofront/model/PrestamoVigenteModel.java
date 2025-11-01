/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 * 
 * @author cesar.leon
 */
public class PrestamoVigenteModel extends BaseModel{
    @Getter @Setter private String folio;
    
    @Getter @Setter private String descripcionEntidadFinanciera;
    @Getter @Setter private Double cat;
    @Getter @Setter private String montoSolicitado;
    @Getter @Setter private String descuentoMensual;
    @Getter @Setter private String mensualidadesDescontadas;
    
    @Getter @Setter private String descripcionPlazo;
    @Getter @Setter private String condicionOferta;
    @Getter @Setter private String idSolicitud;
    @Getter @Setter private String nombreComercialEF;
    @Getter @Setter private String clabe;
    @Getter @Setter private String correoAdminEF;
    @Getter @Setter private String numFolioSolicitud;
    
}
