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
import mx.gob.imss.dpes.common.enums.TipoReporteEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.interfaces.reportesMclp.model.ReporteRs;
import mx.gob.imss.dpes.prestamofront.exception.ReporteExcepcion;
import mx.gob.imss.dpes.prestamofront.restclient.ReporteClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ValidarGeneracionReporteService extends ServiceDefinition<Reporte, Reporte> {

    @Inject
    @RestClient
    private ReporteClient reporteClient;

    @Override
    public Message<Reporte> execute(Message<Reporte> request) throws BusinessException {
        //log.log(Level.INFO, ">>>prestamoFront|ValidarGeneracionReporteService|execute {0}", request.getPayload());
        log.log(Level.INFO, ">>>ValidarGeneracionReporteService JGV : {0}", request);
        Long tipoReporte = 0L;
        switch (request.getPayload().getTipoReporte().intValue()) {
            case 1:
                tipoReporte = TipoReporteEnum.REPORTE_CONCILIACION.getId();
                Response respuesta = reporteClient.validarReporte(request.getPayload().getAnioNominal().concat(request.getPayload().getMesNominal()), tipoReporte);
                if (respuesta.getStatus() == 200) {
                    ReporteRs response = respuesta.readEntity(ReporteRs.class);
                    //log.log(Level.INFO, ">>>prestamoFront|ValidarGeneracionReporteService|execute|response {0}", response);
                    log.log(Level.INFO, ">>>prestamoFront|ValidarGeneracionReporteService|execute|response JGV 1: {0}",response);
                    request.getPayload().setDocConciliacion(response.getReporteDocumentos().get(0).getDocumento());
                    request.getPayload().setDocInconsistencias(response.getReporteDocumentos().get(1).getDocumento());
                    request.getPayload().setFlagGenerarReporte(Boolean.FALSE);
                    request.getPayload().setReporteRs(response);
                    log.log(Level.INFO, ">>>prestamoFront|ValidarGeneracionReporteService|execute|request JGV 2: {0}",request);
                    return request;
                }else{
                    Response responseCorreos = reporteClient.getCorreos();
                    ReporteRs response = responseCorreos.readEntity(ReporteRs.class);
                    request.getPayload().setReporteRs(response);
                    request.getPayload().setFlagGenerarReporte(Boolean.TRUE); 
                    return request;
                }
            case 2:
                tipoReporte = TipoReporteEnum.REPORTE_CONCILIACION_EF.getId();
                 Response respuesta2 = reporteClient.validarReporte(request.getPayload().getAnioNominal().concat(request.getPayload().getMesNominal()), tipoReporte);
                if (respuesta2.getStatus() == 200) {
                    ReporteRs response = respuesta2.readEntity(ReporteRs.class);
                    //log.log(Level.INFO, ">>>prestamoFront|ValidarGeneracionReporteService|execute|response {0}", response);
                    log.log(Level.INFO, ">>>prestamoFront|ValidarGeneracionReporteService|execute|response JGV 1: {0}",response);
                    request.getPayload().setDocConciliacion(response.getReporteDocumentos().get(0).getDocumento());
                    request.getPayload().setDocInconsistencias(response.getReporteDocumentos().get(1).getDocumento());
                    request.getPayload().setFlagGenerarReporte(Boolean.FALSE);
                    request.getPayload().setReporteRs(response);
                    log.log(Level.INFO, ">>>prestamoFront|ValidarGeneracionReporteService|execute|request JGV 2: {0}",request);
                    return request;
                }else{
                    Response responseCorreos = reporteClient.getCorreos();
                    ReporteRs response = responseCorreos.readEntity(ReporteRs.class);
                    request.getPayload().setReporteRs(response);
                    request.getPayload().setFlagGenerarReporte(Boolean.TRUE); 
                    return request;
                }
            case 3:
                tipoReporte = TipoReporteEnum.REPORTE_DESCUENTOS_EMITIDOS.getId();
                Response respuesta3 = reporteClient.validarReporte(request.getPayload().getAnioNominal().concat(request.getPayload().getMesNominal()), tipoReporte);
                if (respuesta3.getStatus() == 200) {
                    ReporteRs response = respuesta3.readEntity(ReporteRs.class);
                    //log.log(Level.INFO, ">>>prestamoFront|ValidarGeneracionReporteService|execute|response {0}", response);
                    log.log(Level.INFO, ">>>prestamoFront|ValidarGeneracionReporteService|execute|response JGV 1: {0}",response);
                    request.getPayload().setDocConciliacion(response.getReporteDocumentos().get(0).getDocumento());
                    request.getPayload().setDocInconsistencias(response.getReporteDocumentos().get(1).getDocumento());
                    request.getPayload().setFlagGenerarReporte(Boolean.FALSE);
                    request.getPayload().setReporteRs(response);
                    log.log(Level.INFO, ">>>prestamoFront|ValidarGeneracionReporteService|execute|request JGV 2: {0}",request);
                    return request;
                }else{
                    Response responseCorreos = reporteClient.getCorreos();
                    ReporteRs response = responseCorreos.readEntity(ReporteRs.class);
                    request.getPayload().setReporteRs(response);
                    request.getPayload().setFlagGenerarReporte(Boolean.TRUE); 
                    return request;
                }
            case 4:
                        log.log(Level.INFO, ">>>CASE 4 JGV");

                tipoReporte = TipoReporteEnum.REPORTE_COMPRAS_CARTERA_EF.getId();
                Response respuesta4 = reporteClient.validarReporteEF(
                         request.getPayload().getAnioNominal().concat(request.getPayload().getMesNominal()),
                         tipoReporte,
                         Long.parseLong(request.getPayload().getCveEntidadFinanciera()) );
                 log.log(Level.INFO, ">>>CASE 4 JGV respuesta2: {0}",respuesta4);
                if (respuesta4.getStatus() == 200) {
                    ReporteRs response = respuesta4.readEntity(ReporteRs.class);
                    //log.log(Level.INFO, ">>>prestamoFront|ValidarGeneracionReporteService|execute|response {0}", response);
                    log.log(Level.INFO, ">>>prestamoFront|ValidarGeneracionReporteService|execute|response JGV 1: {0}",response);
                    request.getPayload().setDocComprasDeCarteraEF(response.getReporteDocumentos().get(0).getDocumento());
                    request.getPayload().setFlagGenerarReporte(Boolean.FALSE);
                    request.getPayload().setReporteRs(response);
                    log.log(Level.INFO, ">>>prestamoFront|ValidarGeneracionReporteService|execute|request JGV 2: {0}",request);
                    return request;
                }else{
                    request.getPayload().setFlagGenerarReporte(Boolean.TRUE); 
                    return request;
                }
            default:
                return response(null, ServiceStatusEnum.EXCEPCION, new ReporteExcepcion("Tipo de reporte inválido"), null);
        }

        
    }
}
