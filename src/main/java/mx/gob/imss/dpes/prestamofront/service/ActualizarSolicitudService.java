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
import mx.gob.imss.dpes.common.enums.TipoCronTareaEnum;
import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.bitacora.model.CronTarea;
import mx.gob.imss.dpes.interfaces.bitacora.model.TareaAccion;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.solicitud.model.EstadoSolicitud;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.prestamofront.exception.PrestamoException;
import mx.gob.imss.dpes.prestamofront.model.PrestamoEnRecuperacionRs;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.PrestamoClient;
import mx.gob.imss.dpes.prestamofront.restclient.SolicitudClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.prestamofront.restclient.EntidadFinancieraBackClient;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.prestamofront.restclient.CronTareaClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ActualizarSolicitudService extends ServiceDefinition<RequestPrestamo, RequestPrestamo> {

    @Inject
    @RestClient
    private SolicitudClient service;
    
    @Inject
    @RestClient
    private PrestamoClient prestamoClient;
    
    @Inject
    @RestClient
    private EntidadFinancieraBackClient entidadFinancieraClient;
    
    @Inject
    @RestClient
    private CronTareaClient cronTareaClient;

    @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
        log.log(Level.INFO, ">>>prestamoFront|ActualizarSolicitudService|execute", request.getPayload());

        Solicitud solicitud = new Solicitud();
        solicitud.setId(request.getPayload().getPrestamo().getSolicitud());
        boolean completo = true;
        
        log.log(Level.INFO, "Se obtiene EF de la Solicitud JFBA");
        Response response = entidadFinancieraClient.getEntidadFinanciera(request.getPayload().getSolicitud().getCveEntidadFinanciera());
        EntidadFinanciera entidadFinancieras = response.readEntity(EntidadFinanciera.class);
        String efSipre = entidadFinancieras.getCveEntidadFinancieraSipre();
        log.log(Level.INFO, "la cveSipre es ", efSipre);
        Response respuesta = prestamoClient.consultaPrestamosPorSolicitud(solicitud.getId());
        if (respuesta.getStatus() == 200) {
            PrestamoEnRecuperacionRs rs = respuesta.readEntity(PrestamoEnRecuperacionRs.class);
            for (PrestamoRecuperacion pr : rs.getPrestamosEnRecuperacion()){
                if (pr.getMontoActualizado() == 0 && !pr.getNumEntidadFinanciera().equals(efSipre) ){
                    completo = false;
                    break;
                }
            }
        }
        if (completo){
            if (request.getPayload().getSolicitud().getCveOrigenSolicitud().getId() == 4) {//flujo prestamo promotor
                solicitud.setEstadoSolicitud(TipoEstadoSolicitudEnum.POR_AUTORIZAR);
                EstadoSolicitud edo = new EstadoSolicitud();
                edo.setId(TipoEstadoSolicitudEnum.POR_AUTORIZAR.toValue());
                edo.setDesEstadoSolicitud(TipoEstadoSolicitudEnum.POR_AUTORIZAR.getDescripcion());
                solicitud.setCveEstadoSolicitud(edo);

            } else if (request.getPayload().getSolicitud().getCveOrigenSolicitud().getId() == 1) {
                solicitud.setEstadoSolicitud(TipoEstadoSolicitudEnum.POR_ASIGNAR_PROMOTOR);
                EstadoSolicitud edo = new EstadoSolicitud();
                edo.setId(TipoEstadoSolicitudEnum.POR_ASIGNAR_PROMOTOR.toValue());
                edo.setDesEstadoSolicitud(TipoEstadoSolicitudEnum.POR_ASIGNAR_PROMOTOR.getDescripcion());
                solicitud.setCveEstadoSolicitud(edo);
            }
            
            Response load = service.updateEstado(solicitud);
            if (load.getStatus() == 200) {
                Solicitud solicitudOut = load.readEntity(Solicitud.class);
                request.getPayload().setSolicitud(solicitudOut);
                log.log(Level.INFO, "Inicia el registro de la tarea de cancelacion de folios");
                registraTarea(request);
                log.log(Level.INFO, "Termina el registro de la tarea de cancelacion de folios");
                return new Message<>(request.getPayload());
            }
        }else{
            return new Message<>(request.getPayload());
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new PrestamoException(), null);
    }
    
    private void registraTarea(Message<RequestPrestamo> req){
        Long origenSolicitud = req.getPayload().getSolicitud().getCveOrigenSolicitud().getId();
        Long estadoSolicitud = req.getPayload().getSolicitud().getCveEstadoSolicitud().getId();
        
        if((origenSolicitud == 1L)&& (estadoSolicitud == TipoEstadoSolicitudEnum.POR_ASIGNAR_PROMOTOR.toValue())){
            log.log(Level.INFO, "Es de origen simulacion y el estado es por asignar promotor");
            CronTarea cronTarea = new CronTarea();
            cronTarea.setCveSolicitud(req.getPayload().getSolicitud().getId());
            TareaAccion tareaAccion = new TareaAccion();
            tareaAccion.setId(TipoCronTareaEnum.SIMULACION_COMPRA_CARTERA_MIXTO_MEJOR_OPCION.getTipo());
            cronTarea.setTareaAccion(tareaAccion);
            Response load = cronTareaClient.add(cronTarea);
            if (load.getStatus() == 200) {
                CronTarea cronTareaResponse = load.readEntity(CronTarea.class);
                log.log(Level.INFO, "   >>><<<ActualizarSolicitudService.fireCron cveTareaAccion=", cronTareaResponse);
            }
        }else if((origenSolicitud == 4L)&& (estadoSolicitud == TipoEstadoSolicitudEnum.POR_AUTORIZAR.toValue())){
            log.log(Level.INFO, "Es de origen promotor y el estado es por autorizar");
            CronTarea cronTarea = new CronTarea();
            cronTarea.setCveSolicitud(req.getPayload().getSolicitud().getId());
            TareaAccion tareaAccion = new TareaAccion();
            tareaAccion.setId(TipoCronTareaEnum.PROMOTOR_COMPRA_CARTERA_MIXTO_MEJOR_OPCION.toValue());
            cronTarea.setTareaAccion(tareaAccion);
            Response load = cronTareaClient.add(cronTarea);
            if (load.getStatus() == 200) {
                CronTarea cronTareaResponse = load.readEntity(CronTarea.class);
                log.log(Level.INFO, "   >>><<<ActualizarSolicitudService.fireCron cveTareaAccion=", cronTareaResponse);
            }
        }
    }
}
