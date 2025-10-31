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
import mx.gob.imss.dpes.prestamofront.model.AdjuntoAutorizacion;
import mx.gob.imss.dpes.prestamofront.model.CorreoAutorizacion;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.CorreoAutorizacionAdjuntosClient;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class CorreoAutorizarService extends ServiceDefinition<RequestPrestamo, RequestPrestamo> {

    @Inject
    @RestClient
    private CorreoAutorizacionAdjuntosClient correoClient;

    @Inject
    private Config config;

    @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
        log.log(Level.INFO, ">>>INICIA STEP ENVIAR CORREO AUTORIZAR PRESTAMO");

        String entidadFinanciera = request.getPayload().getPersonaEf().getEntidadFinanciera().getNombreComercial();
        String numFolio = request.getPayload().getSolicitud().getNumFolioSolicitud();
        String plantilla = String.format(config.getValue("plantillaAutorizar", String.class),
                entidadFinanciera, numFolio);

        //Correo correo = new Correo();
        CorreoAutorizacion correo = new CorreoAutorizacion();

        try {
            correo.setCuerpoCorreo(new String(plantilla.getBytes(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CorreoService.class.getName()).log(Level.SEVERE, null, ex);
        }

        correo.setAsunto("Préstamo Autorizado");

        ArrayList<String> correos = new ArrayList<>();
        correos.add(request.getPayload().getPersona().getCorreoElectronico());
        correo.setCorreoPara(correos);

        //ArrayList<Adjunto> adjuntos = new ArrayList<Adjunto>();
        //Adjunto adjunto1 = new Adjunto();
        ArrayList<AdjuntoAutorizacion> adjuntos = new ArrayList<AdjuntoAutorizacion>();

        AdjuntoAutorizacion adjunto1 = new AdjuntoAutorizacion();
        adjunto1.setNombreAdjunto("Contrato.pdf");
        Base64 codec1 = new Base64();
        String encoded = codec1.encodeBase64String(request.getPayload().getDocumento().getContrato());
        adjunto1.setAdjuntoBase64(encoded);
        //adjunto1.setAdjuntoBase64(request.getPayload().getDocumento().getContrato());
        adjuntos.add(adjunto1);

        //Adjunto adjunto2 = new Adjunto();
        AdjuntoAutorizacion adjunto2 = new AdjuntoAutorizacion();
        adjunto2.setNombreAdjunto("CartaLibranza.pdf");
        Base64 codec2 = new Base64();
        String encoded2 = codec2.encodeBase64String(request.getPayload().getDocumento().getCarta());
        adjunto2.setAdjuntoBase64(encoded2);
        //adjunto2.setAdjuntoBase64(request.getPayload().getDocumento().getCarta());
        adjuntos.add(adjunto2);
        
        AdjuntoAutorizacion adjunto3 = new AdjuntoAutorizacion();
        adjunto3.setNombreAdjunto("IdentificacionOficial.pdf");
        Base64 codec3 = new Base64();
        String encoded3 = codec3.encodeBase64String(request.getPayload().getDocumento().getIdentificacion());
        adjunto3.setAdjuntoBase64(encoded3);
        adjuntos.add(adjunto3);

        correo.setAdjuntos(adjuntos);
        correo.setRemitente("noreply.prestamos@imss.gob.mx");
        log.log(Level.INFO, ">>>antes de enviar a servicios digitales: {0}", correo);

        Response response = null;
        try {
            response = correoClient.enviaCorreoAutorizar(correo);
        } catch (Exception e) {
            log.log(Level.INFO, ">>>ERROR AL EVIARSE CORREO AUTORIZACION: {0}");
            e.printStackTrace();
        }

        if (response.getStatus() == 200 || response.getStatus() == 204) {
            log.log(Level.INFO, ">>>>>>SE ENVIO CORREO AUTORIZAR PRESTAMO={0}", response.getStatus());
            return request;
        }

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
