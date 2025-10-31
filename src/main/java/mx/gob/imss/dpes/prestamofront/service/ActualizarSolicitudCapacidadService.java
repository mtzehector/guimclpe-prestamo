/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.prestamofront.exception.PrestamoException;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.SolicitudClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ActualizarSolicitudCapacidadService extends ServiceDefinition<RequestPrestamo, RequestPrestamo>{
    
 @Inject
    @RestClient
    private SolicitudClient service;
    
     @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
        log.log(Level.INFO, "Actualizar Estado", request.getPayload());
       
        Solicitud solicitud = new Solicitud();
        solicitud.setId(request.getPayload().getSolicitud().getId());
        solicitud.setEstadoSolicitud(TipoEstadoSolicitudEnum.INICIADO); 
       
        
        log.log(Level.INFO, "Request hacia SolicitudBack: {0}", solicitud);
        Response load = service.updateEstado(solicitud);
        if (load.getStatus() == 200) {
            Solicitud solicitudOut = load.readEntity(Solicitud.class);
            request.getPayload().setSolicitud(solicitudOut);
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new PrestamoException(), null);
    }
}
