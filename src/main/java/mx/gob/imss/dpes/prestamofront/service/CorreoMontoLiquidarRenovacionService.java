/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Correo;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.CorreoClient;
import mx.gob.imss.dpes.prestamofront.restclient.EntidadFinancieraBackClient;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import java.util.ArrayList;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;

/**
 *
 * @author juanf.barragan
 */
@Provider
public class CorreoMontoLiquidarRenovacionService extends ServiceDefinition<RequestPrestamo, RequestPrestamo> {
    
    @Inject
    @RestClient
    private CorreoClient correoClient;
    
    @Inject
    @RestClient
    private EntidadFinancieraBackClient entidadFinancieraClient;
    
    @Inject
    private Config config;
    
    @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
        
        Correo correo = new Correo();
        
        String entidadFinanciera = request.getPayload().getListPrestamoRecuperacion().getPrestamosEnRecuperacion().get(0).getNombreComercial();
        
        String nombre = request.getPayload().getPersona().getNombre();
        String pApellido = request.getPayload().getPersona().getPrimerApellido();
        String sApellido = request.getPayload().getPersona().getSegundoApellido();
        String pensionado = nombre+" "+pApellido+" "+sApellido;
        
        String nss = request.getPayload().getSolicitud().getNss();
        String curp= request.getPayload().getSolicitud().getCurp();
        
        String ef = entidadFinanciera;
        
        double monto =0;
        for(PrestamoRecuperacion pr: request.getPayload().getListPrestamoRecuperacion().getPrestamosEnRecuperacion()){
            monto += pr.getCanMontoSol();
        }
        String plantilla = String.format(config.getValue("plantillaMontoLiquidar", String.class),
                           ef,nss,curp,pensionado,entidadFinanciera,monto);
        
        try {
            correo.setCuerpoCorreo(new String(plantilla.getBytes(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CorreoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String correoAdmin;
        
        Response response = entidadFinancieraClient.load(request.getPayload().getListPrestamoRecuperacion().getPrestamosEnRecuperacion().get(0).getNumEntidadFinanciera());
        EntidadFinanciera entidadFinancieras = response.readEntity(EntidadFinanciera.class);
        correoAdmin = entidadFinancieras.getCorreoAdminEF();
        
        correo.setAsunto("Confirmación de Monto a Liquidar");
        ArrayList<String> correos = new ArrayList<>();
        correos.add(request.getPayload().getPersona().getCorreoElectronico());
        correos.add(correoAdmin);
        correo.setCorreoPara(correos);
        
        ArrayList<Adjunto> adjuntos = new ArrayList<Adjunto>();
        correo.setAdjuntos(adjuntos);
        
        Response respuesta = correoClient.enviaCorreo(correo);
                
        if(respuesta.getStatus() == 200 || respuesta.getStatus() == 204) {
            log.log(Level.INFO, ">>>>>>SE ENVIO CORREO DE CONFIRMACION DEL MONTO A LIQUIDAR={0}", respuesta.getStatus());
            return request;
        }

           return new Message<>(request.getPayload()); 
    }
    
    
}
