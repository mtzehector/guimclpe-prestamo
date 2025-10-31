/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.prestamofront.restclient.ReporteClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class AutorizarReporteService extends ServiceDefinition<Reporte, Reporte> {

    @Inject
    @RestClient
    private ReporteClient reporteClient;

    @Override
    public Message<Reporte> execute(Message<Reporte> request) throws BusinessException {
        log.log(Level.INFO, ">>>prestamoFront|AutorizarReporteService|execute {0}", request.getPayload());

        Response response = reporteClient.updateEstadoReporte(request.getPayload().getReporteRs().getReporte());
        if (response.getStatus() == 200) {
            mx.gob.imss.dpes.interfaces.reportesMclp.model.Reporte r = response.readEntity(mx.gob.imss.dpes.interfaces.reportesMclp.model.Reporte.class);

            log.log(Level.INFO, ">>>prestamoFront|AutorizarReporteService|execute reportesMclp: {0}", r);

            request.getPayload().getReporteRs().setReporte(r);

            return request;
        }
        return response(null, ServiceStatusEnum.EXCEPCION, null, null);

    }

}
