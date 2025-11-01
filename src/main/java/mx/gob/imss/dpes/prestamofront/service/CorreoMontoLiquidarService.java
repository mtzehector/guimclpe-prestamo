/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;
import mx.gob.imss.dpes.prestamofront.restclient.CorreoClient;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import javax.inject.Inject;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Correo;
import mx.gob.imss.dpes.prestamofront.restclient.EntidadFinancieraBackClient;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;

/**
 *
 * @author juanf.barragan
 */
@Provider
public class CorreoMontoLiquidarService extends ServiceDefinition<RequestPrestamo, RequestPrestamo> {
    
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
        log.log(Level.INFO,">>>INICIA STEP ENVIAR CORREO CONFIRMA MONTO LIQUIDAR: {0}", request.getPayload());
        
        Correo correo = new Correo();
        String entidadFinancieran = request.getPayload().getPrestamo().getOferta().getEntidadFinanciera().getNombreComercial();
        
        String nombre = request.getPayload().getPersona().getNombre();
        String pApellido = request.getPayload().getPersona().getPrimerApellido();
        String sApellido = request.getPayload().getPersona().getSegundoApellido();
        String pensionado = nombre+" "+pApellido+" "+sApellido;
        
        String nss = request.getPayload().getSolicitud().getNss();
        String curp= request.getPayload().getSolicitud().getCurp();
        
        String ef =request.getPayload().getListPrestamoRecuperacion().getPrestamosEnRecuperacion().get(0).getNombreComercial();
        double monto = 0;
        for (PrestamoRecuperacion pr : request.getPayload().getListPrestamoRecuperacion().getPrestamosEnRecuperacion()){
            monto += pr.getCanMontoSol();
        }
        //String monto = request.getPayload().getListPrestamoRecuperacion().getPrestamosEnRecuperacion().get(0).getCanMontoSol().toString();
        
        String plantilla = String.format(config.getValue("plantillaMontoLiquidar", String.class),
                           ef,nss,curp,pensionado,entidadFinancieran,monto);
        //Agregar los datos faltantes de la curp y eso;
        try {
            correo.setCuerpoCorreo(new String(plantilla.getBytes(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CorreoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        String adminEFC;
        String adminEFV;
        
        Response response = entidadFinancieraClient.getEntidadFinanciera(request.getPayload().getPrestamo().getOferta().getEntidadFinanciera().getId());
        EntidadFinanciera entidadFinanciera = response.readEntity(EntidadFinanciera.class);
        adminEFV = entidadFinanciera.getCorreoAdminEF();
        
        response = entidadFinancieraClient.load(request.getPayload().getListPrestamoRecuperacion().getPrestamosEnRecuperacion().get(0).getNumEntidadFinanciera());
         entidadFinanciera = response.readEntity(EntidadFinanciera.class);
        adminEFC =entidadFinanciera.getCorreoAdminEF();
        
        correo.setAsunto("Confirmación de Monto a Liquidar");
        ArrayList<String> correos = new ArrayList<>();
        correos.add(request.getPayload().getPersona().getCorreoElectronico());
        correos.add(adminEFC);
        correos.add(adminEFV);
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
