/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.assembler;

import java.util.logging.Level;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.prestamofront.model.PrestamoVigenteComposite;
import mx.gob.imss.dpes.prestamofront.model.PrestamoVigenteModel;

/**
 *
 * @author Diego Velazquez
 */
@Provider
public class PrestamoVigenteCompositeAssembler extends BaseAssembler<PrestamoVigenteComposite, PrestamoVigenteModel> {

    @Override
    public PrestamoVigenteModel assemble(PrestamoVigenteComposite source) {
        log.log(Level.INFO, "JUAN LOG ========= |> PrestamoVigenteCompositeAssembler de la consulta : {0}", source);

        if (source.getSolicitudVigenteMclpeResponse() != null) {
            PrestamoVigenteModel prestamoVigente = new PrestamoVigenteModel();
            prestamoVigente.setFolio(source.getSolicitudVigenteMclpeResponse().getNumFolioSolicitud());
            prestamoVigente.setMontoSolicitado(source.getPrestamoMclpeResponse().getMonto().toString());
            prestamoVigente.setDescuentoMensual(source.getPrestamoMclpeResponse().getImpDescNomina().toString());
            prestamoVigente.setDescripcionEntidadFinanciera(
                    source.getCondicionOfertaMclpeResponse().getEntidadFinanciera().getNombreComercial());
            prestamoVigente.setCat(source.getCondicionOfertaMclpeResponse().getCat());
            prestamoVigente.setDescripcionPlazo(source.getCondicionOfertaMclpeResponse().getPlazo().getDescripcion());
            prestamoVigente.setMensualidadesDescontadas(source.getPrestamoSistrap().getTotalMensualidadesDescontadas());
            prestamoVigente.setIdSolicitud(source.getSolicitudVigenteMclpeResponse().getId().toString());
            prestamoVigente.setCondicionOferta(source.getPrestamoMclpeResponse().getIdOferta().toString());

            return prestamoVigente;
        } else {
            return null;
        }
    }

}
