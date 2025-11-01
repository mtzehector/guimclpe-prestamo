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
 * @author juan.garfias
 */
@Provider
public class PrestamosEnRecuperacionAssambler extends BaseAssembler<PrestamoVigenteComposite, PrestamoVigenteModel> {

    @Override
    public PrestamoVigenteModel assemble(PrestamoVigenteComposite source) {
        log.log(Level.INFO, "JUAN LOG ========= |> PrestamosEnRecuperacionAssambler de la consulta : {0}", source);

        if (source.getPrestamoRecuperado() != null) {

            PrestamoVigenteModel prestamoVigente = new PrestamoVigenteModel();

            prestamoVigente.setMontoSolicitado(source.getPrestamoRecuperado().getImpRealPrestamo().toString());
            prestamoVigente.setDescuentoMensual(source.getPrestamoRecuperado().getImpMensual().toString());
            prestamoVigente.setCat(source.getPrestamoRecuperado().getCatPrestamo());
            prestamoVigente.setMensualidadesDescontadas(source.getPrestamoRecuperado().getMesesRecuperados().toString());
            prestamoVigente.setIdSolicitud(source.getPrestamoRecuperado().getIdSolPrestamo());
            prestamoVigente.setDescripcionEntidadFinanciera(source.getPrestamoRecuperado().getInsFinanciera());
            //prestamoVigente.setNombreComercialEF(source.getPrestamoRecuperado().getNombreComercialEF());
            prestamoVigente.setNombreComercialEF(source.getPrestamoRecuperado().getNomFinanciera());
            prestamoVigente.setClabe(source.getPrestamoRecuperado().getClabe());
            prestamoVigente.setCorreoAdminEF(source.getPrestamoRecuperado().getNomEmail());
            Integer iAux =  source.getPrestamoRecuperado().getNumMeses().intValue();
            prestamoVigente.setDescripcionPlazo( iAux.toString() );
            prestamoVigente.setNumFolioSolicitud(source.getPrestamoRecuperado().getNumFolioSolicitud());
            

            return prestamoVigente;
        } else {
            return null;
        }
    }
}

