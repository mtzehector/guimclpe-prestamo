package mx.gob.imss.dpes.prestamofront.assembler;


import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoRequest;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;

@Provider
public class RegistroPrestamoConvertidor {

	protected final Logger log = Logger.getLogger(getClass().getName());
    public static final String IMG_CARTA_INSTRUCCION = "S";
    
    public RegistroPrestamoRequest ensambladorRegistroPrestamo(RequestPrestamo datos) throws Exception {
    	
    	try {
    		RegistroPrestamoRequest registroPrestamo = new RegistroPrestamoRequest();

    		registroPrestamo.setNumFolio(datos.getSolicitud().getNumFolioSolicitud());
    		registroPrestamo.setNss(datos.getSolicitud().getNss());
    		registroPrestamo.setGrupoFamiliar(datos.getSolicitud().getGrupoFamiliar());
    		registroPrestamo.setCurp(datos.getSolicitud().getCurp());
    		registroPrestamo.setFecInicioPrestamo(new SimpleDateFormat("yyyy-MM-dd").format(datos.getSolicitud().getAltaRegistro()));
    		registroPrestamo.setNumContrato(datos.getSolicitud().getNumFolioSolicitud());

    		registroPrestamo.setNumProveedor(datos.getPrestamo().getOferta().getEntidadFinanciera().getCveEntidadFinancieraSipre());
    		registroPrestamo.setClabe(datos.getPrestamo().getRefCuentaClabe());
    		registroPrestamo.setImpPrestamo(datos.getPrestamo().getImpTotalPagar());
    		registroPrestamo.setImpMensual(datos.getPrestamo().getImpDescNomina());
    		registroPrestamo.setCatPrestamo(datos.getPrestamo().getOferta().getCat());
    		registroPrestamo.setImpRealPrestamo(datos.getPrestamo().getMonto());
    		registroPrestamo.setNominaPrimerDescuento(new SimpleDateFormat("yyyyMM").format(datos.getPrestamo().getPrimerDescuento().getTime()));
    		if (datos.getPrestamo().getOferta().getPlazo() != null && datos.getPrestamo().getOferta().getPlazo().getNumPlazo() != null) 
    			if (datos.getPrestamo().getOferta().getPlazo().getNumPlazo() > 0) 
    				registroPrestamo.setNumPlazo(Integer.toString(datos.getPrestamo().getOferta().getPlazo().getNumPlazo()));
    		
    		if (datos.getPersona().getNombre() == null) {
    			registroPrestamo.setApellidoMaterno("Materno");
	            registroPrestamo.setApellidoPaterno("Materno");
	            registroPrestamo.setNombre("Nombre");
	        } else {
	        	registroPrestamo.setNombre(datos.getPersona().getNombre());
	    		registroPrestamo.setApellidoPaterno(datos.getPersona().getPrimerApellido());
	    		registroPrestamo.setApellidoMaterno(datos.getPersona().getSegundoApellido());

	        }
    		
    		registroPrestamo.setImgCartaInstruccion(IMG_CARTA_INSTRUCCION);
    		registroPrestamo.setFecAlta("");

    		registroPrestamo.setIdSolPrestLiq1("");
    		registroPrestamo.setIdSolPrestLiq2("");
    		registroPrestamo.setIdSolPrestLiq3("");

            return registroPrestamo;
    	} catch (Exception e) {
			log.log(Level.SEVERE, "ERROR! PrestamoFront RegistroPrestamoConvertidor.ensambladorRegistroPrestamo() RequestPrestamo = [" + datos + "]", e);
			return null;
		}
    }
    
}
