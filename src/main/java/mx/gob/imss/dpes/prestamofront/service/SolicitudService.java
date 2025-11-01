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
import mx.gob.imss.dpes.interfaces.solicitud.model.EstadoSolicitud;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.prestamofront.exception.PrestamoException;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.SolicitudClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class SolicitudService extends ServiceDefinition<RequestPrestamo, RequestPrestamo> {

    @Inject
    @RestClient
    private SolicitudClient service;

    @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
        log.log(Level.INFO, "########## Step 2 ##########");
        //se inserta solicitud
//        log.log(Level.INFO, "Request solicitud: {0}", request);
        Solicitud solicitud = request.getPayload().getSolicitud();
        solicitud.setId(request.getPayload().getPrestamo().getSolicitud());
        solicitud.setEstadoSolicitud(TipoEstadoSolicitudEnum.PENDIENTE_CARGA_COMPROBANTE);
        solicitud.setCveEstadoSolicitud(new EstadoSolicitud(
                TipoEstadoSolicitudEnum.PENDIENTE_CARGA_COMPROBANTE.getTipo())
        );
//        log.log(Level.INFO, "Request hacia SolicitudBack: {0}", solicitud);
        Response load = service.updateEstado(solicitud);
        if (load.getStatus() == 200) {
            Solicitud solicitudOut = load.readEntity(Solicitud.class);
            request.getPayload().setSolicitud(solicitudOut);
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new PrestamoException(), null);
    }

}
