/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Oferta;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import mx.gob.imss.dpes.interfaces.sipre.model.PrestamoRecuperado;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.prestamofront.model.mclpe.PeriodoNominaMclpeResponse;
import mx.gob.imss.dpes.prestamofront.model.sistrap.PrestamoSistrap;
/**
 *
 * @author antonio
 */
public class PrestamoVigenteComposite extends BaseModel{
    @Getter @Setter private Solicitud solicitudVigenteMclpeResponse;
    @Getter @Setter private Prestamo prestamoMclpeResponse;
    @Getter @Setter private Oferta condicionOfertaMclpeResponse;
    @Getter @Setter private PeriodoNominaMclpeResponse periodoNominaMclpeResponse;
    @Getter @Setter private PrestamoSistrap prestamoSistrap;
    @Getter @Setter private PrestamoRecuperado prestamoRecuperado;
}
