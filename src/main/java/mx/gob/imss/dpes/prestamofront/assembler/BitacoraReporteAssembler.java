/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.assembler;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.interfaces.reportesMclp.model.ReporteRq;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class BitacoraReporteAssembler extends BaseAssembler<Reporte, ReporteRq>{
    
    @Override
    public ReporteRq assemble(Reporte source) {
        return null;
    }
}
