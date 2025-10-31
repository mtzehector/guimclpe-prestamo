/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.CorreoSinAdjuntos;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.CorreoClient;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class CorreoService extends ServiceDefinition<RequestPrestamo, RequestPrestamo> {
    
    @Inject
    @RestClient
    private CorreoClient correoClient;
    
    @Inject
    private Config config;
    
    @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
        log.log(Level.INFO,">>>INICIA STEP ENVIAR CORREO AVISO SOLICITUD: {0}", request.getPayload());
        
        String folio = request.getPayload().getSolicitud().getNumFolioSolicitud();
        String entidadFinanciera = request.getPayload().getPrestamo().getOferta().getEntidadFinanciera().getRazonSocial();
        String plantilla;
        
        CorreoSinAdjuntos correo = new CorreoSinAdjuntos();
        
        
        
        if(request.getPayload().getFlatMonto() == 0){
            plantilla = String.format(config.getValue("plantillaCancelacionXMonto", String.class),
                           folio, entidadFinanciera);
        }else if(request.getPayload().getFlatFecha() == 0){
            plantilla = String.format(config.getValue("plantillaCancelacionXFecha", String.class),
                           folio, entidadFinanciera);
        }else{
            return new Message<>(request.getPayload());
        }
        
        try {
            correo.setCuerpoCorreo(new String(plantilla.getBytes(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CorreoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        correo.setAsunto("Folio Cancelado");
        ArrayList<String> correos = new ArrayList<>();
        correos.add(request.getPayload().getPersona().getCorreoElectronico());
        correo.setCorreoPara(correos);
        
        Response response = correoClient.enviaCorreo(correo);
                
        if(response.getStatus() == 200 || response.getStatus() == 204) {
            log.log(Level.INFO, ">>>>>>SE ENVIO CORREO DE AVISO={0}", response.getStatus());
            return request;
        }

           return new Message<>(request.getPayload()); 
        }
    
    
}
