package mx.gob.imss.dpes.prestamofront.service;

import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.solicitud.model.EstadoSolicitud;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.interfaces.solicitud.model.SolicitudEstado;
import mx.gob.imss.dpes.prestamofront.exception.AutorizarPrestamoException;
import mx.gob.imss.dpes.prestamofront.restclient.SolicitudClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class ActualizaEstadoSolicitudService extends ServiceDefinition<SolicitudEstado, SolicitudEstado> {

    @Inject
    @RestClient
    private SolicitudClient service;

    @Override
    public Message<SolicitudEstado> execute(Message<SolicitudEstado> request) throws BusinessException {
        try{
            Solicitud solicitud = new Solicitud();
            solicitud.setId(request.getPayload().getIdSolicitud());
            solicitud.setEstadoSolicitud(TipoEstadoSolicitudEnum.AUTORIZADO);
            EstadoSolicitud estado = new EstadoSolicitud();
            estado.setId(TipoEstadoSolicitudEnum.AUTORIZADO.getTipo());
            estado.setDesEstadoSolicitud(TipoEstadoSolicitudEnum.AUTORIZADO.getDescripcion());
            solicitud.setCveEstadoSolicitud(estado);
            Response response = service.updateEstado(solicitud);
            if (response != null && response.getStatus() == 200) {
                Solicitud solicitudRespuesta = response.readEntity(Solicitud.class);
                request.getPayload().setSolicitud(solicitudRespuesta);
                return request;
            }
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR ActualizaEstadoSolicitudService.execute() = {0}", e);
        }
        return response(
                null,
                ServiceStatusEnum.EXCEPCION,
                new AutorizarPrestamoException(AutorizarPrestamoException.ERROR_AL_AUTORIZAR_REINSTALACION),
                null
        );
    }
}
