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
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.prestamofront.model.PrestamosEnRecuperacionRequest;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.service.ActualizarSolicitudCapacidadService;
import mx.gob.imss.dpes.prestamofront.service.ActualizarSolicitudService;
import mx.gob.imss.dpes.prestamofront.service.ConsultarPrestamosRecuperacionBackService;
import mx.gob.imss.dpes.prestamofront.service.CorreoAutorizarService;
import mx.gob.imss.dpes.prestamofront.service.CorreoService;
import mx.gob.imss.dpes.prestamofront.service.CronJobAutorizarService;
import mx.gob.imss.dpes.prestamofront.service.CronJobService;
import mx.gob.imss.dpes.prestamofront.service.ObtenerAmortizacionService;
import mx.gob.imss.dpes.prestamofront.service.ObtenerCartaInstruccionService;
import mx.gob.imss.dpes.prestamofront.service.ObtenerIdentificacionService;
import mx.gob.imss.dpes.prestamofront.service.ObtenerContratoService;
import mx.gob.imss.dpes.prestamofront.service.PrestamoRecuperacionCapacidad;
import mx.gob.imss.dpes.prestamofront.service.PrestamoRecuperacionService;
import mx.gob.imss.dpes.prestamofront.service.PrestamoService;
import mx.gob.imss.dpes.prestamofront.service.SolicitudService;
import mx.gob.imss.dpes.prestamofront.service.UpdateAutorizacionDocumentsService;
import mx.gob.imss.dpes.prestamofront.service.ValidacionService;
import mx.gob.imss.dpes.prestamofront.service.reporte.ReporteConciliacionXLSService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import mx.gob.imss.dpes.prestamofront.service.CorreoMontoLiquidarService;
import mx.gob.imss.dpes.prestamofront.service.CorreoMontoLiquidarRenovacionService;

/**
 *
 * @author eduardo.loyo
 */
@Path("/prestamo")
@RequestScoped
public class PrestamoEndPoint extends BaseGUIEndPoint<RequestPrestamo, RequestPrestamo, RequestPrestamo> {

    @Inject
    private PrestamoService prestamo;
    @Inject
    private SolicitudService solicitud;
    @Inject
    private ValidacionService validacion;
    @Inject
    private PrestamoRecuperacionService prestamoRecuperacion;
    @Inject
    private ActualizarSolicitudService actualizarSolicitud;
    @Inject
    private CorreoService envioCorreo;
    @Inject
    private PrestamoRecuperacionCapacidad prestamoRecuperacionCap;
    @Inject
    private ActualizarSolicitudCapacidadService actualizarSolicitudCap;
    @Inject
    private CorreoAutorizarService envioCorreoAutorizar;
    @Inject
    private ObtenerContratoService obtenerContrato;
    @Inject
    private ObtenerCartaInstruccionService obtenerCartaInstruccion;
    @Inject
    private ObtenerAmortizacionService obtenerAmortizacion;
    @Inject
    private UpdateAutorizacionDocumentsService updateAutorizacionDocumentsService;

    @Inject
    private ReporteConciliacionXLSService reporteTest;

    @Inject
    CronJobService cronJobService;
    @Inject
    CronJobAutorizarService cronJobAutorizarService;

    @Inject
    private ConsultarPrestamosRecuperacionBackService consulPrestRecBackSer;
    
    @Inject
    private CorreoMontoLiquidarRenovacionService correoMontoLiquidarRenovacionService;
    
    @Inject
    private CorreoMontoLiquidarService correoMontoLiquidarService;
    
    @Inject
    private ObtenerIdentificacionService obtenerIdentificacionService;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Consume sistrap",
            description = "consume sistrap")
    @Override
    public Response create(RequestPrestamo request) throws BusinessException {
    	log.log(Level.INFO, "########## prestamoFront PrestamoEndPoint /prestamo Inicia autorizar prestamo ##########");

        ServiceDefinition[] steps = {
            prestamo,
            solicitud,
            updateAutorizacionDocumentsService,
            obtenerContrato,
            obtenerCartaInstruccion,
            obtenerIdentificacionService,
            cronJobAutorizarService,
            envioCorreoAutorizar
        };

        Message<RequestPrestamo> response = prestamo.executeSteps(steps, new Message<>(request));
        //response.getHeader().getNotices().add(new Notice("hola"));
//        log.log(Level.INFO, "LAS NOTICES SON : {0}", response.getHeader().getNotices());
//        Date dateInQuestion = response.getPayload().getBitacora().getAltaRegistro();
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(dateInQuestion);
//        cal.add(Calendar.HOUR, 72);
//        Date futureDate = cal.getTime();
//        log.log(Level.INFO,"FECHAS CURRENT: {0}, FUTURO: {1}", new Object[]{futureDate, new Date()});
//        if (futureDate.before(new Date())) {
//            // Then more than 72 hours have passed since the date in question
//            log.log(Level.INFO,"ENTRO");
//            response.getHeader().addNotice(new Notice("El comprobante electrónico de pago no se adjuntó dentro de las 72 horas establecidas."));
//        }
        // response.getHeader().addNotice(new Notice("Pasaron 72 Horas"));
        return toResponse(response);
    }

    @POST
    @Path("/registroMonto")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registrarMontoLiquidar(RequestPrestamo request) throws BusinessException {
        log.log(Level.INFO, ">>>prestamoFront|PrestamoEndPoint|registrarMontoLiquidar {0}", request);
        ServiceDefinition[] steps = {prestamoRecuperacion, actualizarSolicitud,correoMontoLiquidarService};
        Message<RequestPrestamo> response = actualizarSolicitud.executeSteps(steps, new Message<>(request));
        return toResponse(response);
    }
    
    @POST
    @Path("/registroMontoRenovacion")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registrarMontoLiquidarRenovacion(RequestPrestamo request) throws BusinessException {
        log.log(Level.INFO, ">>>prestamoFront|PrestamoEndPoint|registrarMontoLiquidar {0}", request);
        ServiceDefinition[] steps = {prestamoRecuperacion ,correoMontoLiquidarRenovacionService};
        Message<RequestPrestamo> response = prestamoRecuperacion.executeSteps(steps, new Message<>(request));
        return toResponse(response);
    }

    @POST
    @Path("/registroMontoCapacidad")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registrarMontoLiquidarCapacidad(RequestPrestamo request) throws BusinessException {
        log.log(Level.INFO, "registrarMontoLiquidarCapacidad", request);
        ServiceDefinition[] steps = {prestamoRecuperacionCap, actualizarSolicitudCap};
        Message<RequestPrestamo> response = prestamoRecuperacion.executeSteps(steps, new Message<>(request));
        return toResponse(response);
    }

    @Path("/prestamosEnRecuperacion")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultaPrestamosPorSolicitud(PrestamosEnRecuperacionRequest request) throws BusinessException {

        log.log(Level.INFO, "Obteniendo prestamos recuperacion de bd {0}", request);

        ServiceDefinition[] steps = {consulPrestRecBackSer};
        Message<PrestamosEnRecuperacionRequest> response = consulPrestRecBackSer.executeSteps(steps, new Message<>(request));

        return toResponse(response);

    }

    @Path("/reporteTest")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reporteTest(Reporte request) throws BusinessException {

        log.log(Level.INFO, "Obteniendo prestamos recuperacion de bd {0}", request);

        ServiceDefinition[] steps = {reporteTest};
        Message<Reporte> response = reporteTest.executeSteps(steps, new Message<>(request));

        return toResponse(response);

    }
}
