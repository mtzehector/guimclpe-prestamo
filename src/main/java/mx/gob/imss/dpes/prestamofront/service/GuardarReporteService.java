/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.reportesMclp.model.ReporteRs;
import mx.gob.imss.dpes.prestamofront.assembler.GuardarReporteAssembler;
import mx.gob.imss.dpes.prestamofront.restclient.ReporteClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class GuardarReporteService extends ServiceDefinition<Reporte, Reporte> {

    @Inject
    @RestClient
    private ReporteClient reporteClient;

    @Override
    public Message<Reporte> execute(Message<Reporte> request) throws BusinessException {
        log.log(Level.INFO, ">>>prestamoFront|GuardarReporteService|execute JGV: {0}", request.getPayload());

        GuardarReporteAssembler assembler = new GuardarReporteAssembler();

        Response response = reporteClient.guardarReporte(assembler.assemble(request.getPayload()));

        if (response.getStatus() == 200) {
            ReporteRs r = response.readEntity(ReporteRs.class);
            log.log(Level.INFO, ">>>prestamoFront|GuardarReporteService|execute reportesMclp JGV: {0}", r);
            if (request.getPayload().getTipoReporte() == 1 || request.getPayload().getTipoReporte() == 3) {

                //log.log(Level.INFO, ">>>prestamoFront|GuardarReporteService|execute reportesMclp: {0}", r);
                r.setCorreos(request.getPayload().getReporteRs().getCorreos());
                request.getPayload().getBitacoraInterfaz().setCveReporte(r.getReporte().getId());

            }

            request.getPayload().setReporteRs(r);
            return request;
        }
        return response(null, ServiceStatusEnum.EXCEPCION, null, null);

    }

}
