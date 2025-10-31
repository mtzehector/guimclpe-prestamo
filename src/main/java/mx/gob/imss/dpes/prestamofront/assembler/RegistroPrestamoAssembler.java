/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.assembler;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoRequest;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;

/**
 *
 * @author antonio
 */
@Provider
public class RegistroPrestamoAssembler
        extends BaseAssembler<RequestPrestamo, RegistroPrestamoRequest> {

    public static final String PENSIONADO = "Pensionado";

    
    @Override
    public RegistroPrestamoRequest assemble(RequestPrestamo source) {
        RegistroPrestamoRequest registroPrestamo = new RegistroPrestamoRequest();
        log.log(Level.INFO, ">>> simulacionFront RegistroPrestamoAssembler source={0}", source);
        log.log(Level.INFO, ">>> simulacionFront RegistroPrestamoAssembler source.getPrestamo().getNumPeriodoNomina()={0}", source.getPrestamo().getPrimerDescuento());
        registroPrestamo.setNumFolio(source.getSolicitud().getNumFolioSolicitud());
        //TODO: Deal with lack of cveEntidadFinancieraSIPRE(syncro)
        String idSipre = source.getPrestamo().getOferta().getEntidadFinanciera().getCveEntidadFinancieraSipre();
        Date fecPrimerDescuento = source.getPrestamo().getPrimerDescuento();
        int numPeriodoNomina = source.getPrestamo().getNumPeriodoNomina()!=null?source.getPrestamo().getNumPeriodoNomina():0;
        //registroPrestamo.setIdInstFinanciera("");
        registroPrestamo.setNumProveedor(idSipre);
        registroPrestamo.setNss(source.getSolicitud().getNss());
        registroPrestamo.setGrupoFamiliar(source.getSolicitud().getGrupoFamiliar());
        registroPrestamo.setCurp(source.getSolicitud().getCurp());
        //TODO: fill info
        registroPrestamo.setNombre(source.getPersona().getNombre());
        registroPrestamo.setApellidoPaterno(source.getPersona().getPrimerApellido());
        registroPrestamo.setApellidoMaterno(source.getPersona().getSegundoApellido());
        registroPrestamo.setClabe(source.getPrestamo().getRefCuentaClabe());
        registroPrestamo.setImpPrestamo(source.getPrestamo().getImpTotalPagar());
        registroPrestamo.setImpMensual(source.getPrestamo().getImpDescNomina());
        registroPrestamo.setImgCartaInstruccion("S");
        int numPlazo = 0;
        if (source.getPrestamo().getOferta().getPlazo() != null) {
            
                    if (source.getPrestamo().getOferta().getPlazo().getNumPlazo() != null) {

            numPlazo = source.getPrestamo().getOferta().getPlazo().getNumPlazo();
        }}
        if (numPlazo > 0) {
            registroPrestamo.setNumPlazo(Integer.toString(numPlazo));
        }
        Timestamp timestamp = new Timestamp(new Date().getTime());
        //TODO: try timestamp
        registroPrestamo.setFecAlta("");
        log.log(Level.INFO, ">>> simulacionFront RegistroPrestamoAssembler timestamp="+timestamp.toString());
        //registroPrestamo.setFecAlta(timestamp.toString());
        Date primerDescuento = source.getPrestamo().getPrimerDescuento();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String nominaPrimerDescuento = sdf.format(primerDescuento.getTime());
        registroPrestamo.setNominaPrimerDescuento(nominaPrimerDescuento);
        registroPrestamo.setCatPrestamo(source.getPrestamo().getOferta().getCat());
        registroPrestamo.setImpRealPrestamo(source.getPrestamo().getMonto());
        SimpleDateFormat sdfini = new SimpleDateFormat("yyyy-MM-dd");
        registroPrestamo.setFecInicioPrestamo(sdfini.format(source.getSolicitud().getAltaRegistro()));
        registroPrestamo.setNumContrato(source.getSolicitud().getNumFolioSolicitud());

        return registroPrestamo;
    }

    
}
