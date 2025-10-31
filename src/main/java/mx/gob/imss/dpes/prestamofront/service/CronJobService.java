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
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.bitacora.model.CronTarea;
import mx.gob.imss.dpes.interfaces.bitacora.model.TareaAccion;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.CronTareaClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author gabriel.rios
 */
@Provider
public class CronJobService extends ServiceDefinition<RequestPrestamo, RequestPrestamo> {

    @Inject
    @RestClient
    private CronTareaClient cronTareaClient;

    @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
        log.log(Level.INFO, ">>>prestamoFront CronJobService request.getPayload()= {0}", request.getPayload());

            CronTarea cronTarea = new CronTarea();
            cronTarea.setCveSolicitud(request.getPayload().getSolicitud().getId());
            TareaAccion tareaAccion= new TareaAccion();
            tareaAccion.setId(TipoCronTareaEnum.SIMULACION_RENOVACION.getTipo());
            cronTarea.setTareaAccion(tareaAccion);
            Response load = cronTareaClient.add(cronTarea);
            if (load.getStatus() == 200) {
                        CronTarea cronTareaResponse = load.readEntity(CronTarea.class);
                        request.getPayload().setCrontareaResponse(cronTareaResponse);
                        log.log(Level.INFO, "   >>><<<CronJobService cronTareaResponse= {0}", cronTareaResponse);

                    }
            return new Message<>(request.getPayload());
        
    }
}
