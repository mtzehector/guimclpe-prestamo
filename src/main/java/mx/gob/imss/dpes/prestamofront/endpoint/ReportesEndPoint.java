/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.endpoint;

import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.enums.TipoReporteEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraInterfaz;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraReporte;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.interfaces.reportes.model.ReporteDescuentosEmitidos;
import mx.gob.imss.dpes.interfaces.reportesMclp.model.EstadoReporte;
import mx.gob.imss.dpes.interfaces.reportesMclp.model.SubTipoReporte;
import mx.gob.imss.dpes.interfaces.reportesMclp.model.TipoReporte;
import mx.gob.imss.dpes.interfaces.sipre.model.ConsultaDescuentoEmitidoResponse;
import mx.gob.imss.dpes.prestamofront.service.AutorizarReporteService;
import mx.gob.imss.dpes.prestamofront.service.BitacoraInterfazService;
import mx.gob.imss.dpes.prestamofront.service.BitacoraReporteService;
import mx.gob.imss.dpes.prestamofront.service.BovedaReportesService;
import mx.gob.imss.dpes.prestamofront.service.ConsultaReportesEstadisticosService;
import mx.gob.imss.dpes.prestamofront.service.ConsultarDescuentosEmitidosService;
import mx.gob.imss.dpes.prestamofront.service.ConsultarPrestamosCompraCarteraEFService;
import mx.gob.imss.dpes.prestamofront.service.CreateCorreoReporteConsiliacionService;
import mx.gob.imss.dpes.prestamofront.service.GuardarReporteService;
import mx.gob.imss.dpes.prestamofront.service.ResumenConciliacionService;
import mx.gob.imss.dpes.prestamofront.service.ValidarGeneracionReporteService;
import mx.gob.imss.dpes.prestamofront.service.reporte.ReporteConciliacionXLSService;

/**
 *
 * @author edgar.arenas
 */
@Path("/reporte")
@RequestScoped
public class ReportesEndPoint extends BaseGUIEndPoint<Reporte, Reporte, Reporte> {

    @Inject
    ConsultarDescuentosEmitidosService consultaSistrap;

    @Inject
    ResumenConciliacionService resumenConciliacion;

    @Inject
    ReporteConciliacionXLSService reporteXlsxService;

    @Inject
    BovedaReportesService bovedaService;

    @Inject
    ValidarGeneracionReporteService validarReporte;

    @Inject
    GuardarReporteService guardarReporte;

    @Inject
    BitacoraReporteService bitacoraService;

    @Inject
    BitacoraInterfazService bitacoraIService;

    @Inject
    AutorizarReporteService autorizarReporte;

    @Inject
    CreateCorreoReporteConsiliacionService correoService;

    @Inject
    ConsultarPrestamosCompraCarteraEFService cpccService;

    @Inject
    ConsultaReportesEstadisticosService consRepoEstService;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/conciliacion")
    public Response create(Reporte request) throws BusinessException {

        ServiceDefinition[] step = {validarReporte};
        Message<Reporte> validar
                = validarReporte.executeSteps(step, new Message<>(request));
        log.log(Level.INFO, "Correos validar JGV: 1 {0}", validar);

        if (validar.getPayload().getFlagGenerarReporte()) {

            request.setBitacoraReporte(new BitacoraReporte(null,
                    null,
                    request.getAnioNominal().concat(request.getMesNominal()),
                    new TipoReporte(request.getTipoReporte()),
                    new SubTipoReporte(1L), // Sub tipo - Prestamos Recuperados
                    new EstadoReporte(1L), // Estado Generado
                    TipoReporteEnum.forValue(request.getTipoReporte()).getDescripcion(),
                    request.getCurpUsuario()));

            ServiceDefinition[] steps = {
                consultaSistrap,
                resumenConciliacion,
                reporteXlsxService,
                bovedaService,
                guardarReporte,
                bitacoraService,
                bitacoraIService,
                correoService
            };

            Message<Reporte> response
                    = consultaSistrap.executeSteps(steps, validar);

            //log.log(Level.INFO, "Correos request JGV: 1 {0}", request);
            // Reduciendo datos al json del response.
            try {
                response.getPayload().setBitacoraInterfaz(new BitacoraInterfaz());
            } catch (Exception e) {
            }

            try {
                response.getPayload().getDescuentosResponse().getDescuentosEmitidos().clear();
            } catch (Exception e) {
            }
            try {
                response.getPayload().getDescuentosResponse().getDescuentosVoList().clear();
            } catch (Exception e) {
            }
            try {
                response.getPayload().getDescuentosResponse().getPrestamosEnCursoVoList().clear();
            } catch (Exception e) {
            }
            try {
                response.getPayload().setRepDesctsEmitds(new ReporteDescuentosEmitidos());
            } catch (Exception e) {
            }
            return toResponse(response);
        }

        return toResponse(validar);

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/conciliacionEF")
    public Response createConciliacionEF(Reporte request) throws BusinessException {
        ServiceDefinition[] steps = {consultaSistrap,
            resumenConciliacion,
            reporteXlsxService,
            bovedaService};
        Message<Reporte> response
                = consultaSistrap.executeSteps(steps, new Message<>(request));
        return toResponse(response);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/autorizarConciliacionImss")
    public Response autorizarConciliacionImss(Reporte request) throws BusinessException {
        ServiceDefinition[] steps = {autorizarReporte};

        Message<Reporte> response
                = autorizarReporte.executeSteps(steps, new Message<>(request));

        ServiceDefinition[] steps2 = {bitacoraService};

        response.getPayload().setBitacoraReporte(new BitacoraReporte(null,
                null,
                request.getReporteRs().getReporte().getPeriodoNomina(),
                request.getReporteRs().getReporte().getTipoReporte(),
                request.getReporteRs().getReporte().getSubTipoReporte(), // Sub tipo - Prestamos Recuperados
                request.getReporteRs().getReporte().getEstadoReporte(), // Estado Generado
                "Autorización reporte conciliación IMSS",
                request.getCurpUsuario()));

        Message<Reporte> response2
                = autorizarReporte.executeSteps(steps2, new Message<>(response.getPayload()));

        return toResponse(response2);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/comprasDeCarteraEF")
    public Response comprasDeCarteraEF(Reporte request) throws BusinessException {

        log.log(Level.INFO, "JGV Validar: 1 {0}", request);

        ServiceDefinition[] step = {validarReporte};
        Message<Reporte> validar
                = validarReporte.executeSteps(step, new Message<>(request));

        if (validar.getPayload().getFlagGenerarReporte()) {

            ServiceDefinition[] steps = {
                cpccService,
                reporteXlsxService,
                bovedaService,
                guardarReporte,
                bitacoraService};
            Message<Reporte> response
                    = cpccService.executeSteps(steps, new Message<>(request));

            log.log(Level.INFO, "comprasDeCarteraEF: {0}", response.getPayload());

            return toResponse(response);
        }
        return toResponse(validar);

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reportesEstadisticos")
    public Response reportesEstadisticos(Reporte request) throws BusinessException {

        log.log(Level.INFO, "JGV Validar: 1 {0}", request);

        ServiceDefinition[] steps = {
            consRepoEstService,
            reporteXlsxService};
        Message<Reporte> response
                = cpccService.executeSteps(steps, new Message<>(request));

        log.log(Level.INFO, "comprasDeCarteraEF: {0}", response.getPayload());

        return toResponse(response);

    }

}
