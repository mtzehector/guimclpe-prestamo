/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.assembler;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoRequest;
import mx.gob.imss.dpes.prestamofront.model.RequestSistrap;
import java.text.SimpleDateFormat;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class SistrapAssembler extends BaseAssembler<RequestSistrap, RegistroPrestamoRequest> {
    /**
     * {
   "numFolio":"001327871",
   "idInstFinanciera":"064",
    "nss":"01674894447",
    "grupoFamiliar":"01",
   "curp":"MABL490924HDFRNZ08",
   "nombre":"PRUEBA",
   "apellidoPaterno":"APELLIDO PATERNO",
   "apellidoMaterno":"APELLIDO MATERNO",
   "clabe":"72905008463878823",
   "impPrestamo":10000.00,
   "numPlazo":"6",
   "fecAlta":"2019-11-29",
   "impMensual":2000,
   "nominaPrimerDescuento":"",
   "numTasaActual":3.42,
   "catPrestamo":51.38,
   "impRealPrestamo":15000,
   "imgCartaInstruccion":"hola"
}
     */

    @Override
    public RegistroPrestamoRequest assemble(RequestSistrap source) {
        RegistroPrestamoRequest request = new RegistroPrestamoRequest();
        request.setNominaPrimerDescuento(new SimpleDateFormat("dd/MM/yyy")
            .format(source.getPrestamo().getPrimerDescuento()));
        request.setGrupoFamiliar(source.getPensionado().getGrupoFamiliar());
        request.setNss(source.getPensionado().getNss());
        request.setCurp(source.getPensionado().getCurp());
        request.setApellidoMaterno(source.getPensionado().getSegundoApellido());
        request.setApellidoPaterno(source.getPensionado().getPrimerApellido());
        request.setNombre(source.getPensionado().getNombre());
        request.setClabe(source.getPrestamo().getRefCuentaClabe());
        request.setNumFolio(source.getSolicitud().getNumFolioSolicitud());
        request.setImpPrestamo(source.getPrestamo().getImpMontoSol());

        return request;
    }
}
