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
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.solicitud.model.SolicitudEstado;
import mx.gob.imss.dpes.prestamofront.exception.AutorizarPrestamoException;
import mx.gob.imss.dpes.prestamofront.exception.OperacionesSISTRAPException;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
//import mx.gob.imss.dpes.prestamofront.restclient.DocumentoClient;
import mx.gob.imss.dpes.prestamofront.service.ActualizaEstadoSolicitudService;
import mx.gob.imss.dpes.prestamofront.service.ActualizarDocumentosReinstalacionService;
import mx.gob.imss.dpes.prestamofront.service.OperacionesSISTRAPService;
import mx.gob.imss.dpes.prestamofront.service.CorreoCompraCarteraService;
import mx.gob.imss.dpes.prestamofront.service.CreateEventService;
import mx.gob.imss.dpes.prestamofront.service.ObtainCEPByteFilesService;
//import mx.gob.imss.dpes.prestamofront.service.PrestamoSIPREService;
import mx.gob.imss.dpes.prestamofront.service.UpdateDocumentsService;
import mx.gob.imss.dpes.prestamofront.service.UpdateEstadoSolicitudService;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/autorizarPrestamo")
@RequestScoped
public class AutorizarPrestamoEndPoint extends BaseGUIEndPoint<BaseModel, BaseModel, RequestPrestamo> {

    @Inject
    //@RestClient
    private UpdateEstadoSolicitudService solicitudService;

    //@Inject
    //@RestClient
    //private PrestamoSIPREService prestamoSIPREService;

    @Inject
    //@RestClient
    private OperacionesSISTRAPService operacionesSISTRAP;

    @Inject
    //@RestClient
    private UpdateDocumentsService updateDocumentsService;

    @Inject
    CreateEventService createEventService;

    @Inject
    CorreoCompraCarteraService correoCompraCarteraService;

    @Inject
    ObtainCEPByteFilesService obtainCEPByteFilesService;
    @Inject
    private ActualizaEstadoSolicitudService actualizaEstadoService;

    @Inject
    private ActualizarDocumentosReinstalacionService documentosReinstalacionService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Actualiza el estado a Autorizado",
            description = "Actualiza el estado a Autorizado\"")
    @Override
    public Response update(RequestPrestamo request) throws BusinessException {
        log.log(Level.INFO, ">>>AutorizarPrestamoEndPoint request={0}", request);

        ServiceDefinition[] steps = {
            solicitudService,
            updateDocumentsService,
            obtainCEPByteFilesService,
            correoCompraCarteraService,
            //prestamoSIPREService
        };
        Message<RequestPrestamo> response = solicitudService.executeSteps(steps, new Message<>(request));

//        if (!Message.isException(response)) {
//            IdentityBaseModel<Long> model = new IdentityBaseModel<>();
//            model.setId(response.getPayload().getSolicitud().getId());
//            createEventService.execute(new Message<>(model));
//        }
        return toResponse(response);
    }
 /*
    @POST
    @Path("/operaciones")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Actualiza el estado a Autorizado",
            description = "Actualiza el estado a Autorizado\"")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOperaciones(RequestPrestamo request) throws BusinessException {
        log.log(Level.INFO, ">>>AutorizarPrestamoEndPoint updateOperaciones request={0}", request);

        ServiceDefinition[] steps = {
            prestamoSIPREService,
            solicitudService
        };
        Message<RequestPrestamo> response = solicitudService.executeSteps(steps, new Message<>(request));
        return toResponse(response);
    }
*/
    
	@POST
	@Path("/operaciones")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Operacion de una solicitud en SIPRE", description = "Operacion de Solicitud y Sincronizacion a SISTRAP\"")
	@Produces(MediaType.APPLICATION_JSON)
	public Response operacionesSISTRAP(RequestPrestamo request) throws BusinessException {
		try {
			if (!(request != null && request.getSolicitud() != null))
				return toResponse(new Message<>(null, ServiceStatusEnum.EXCEPCION,
						new OperacionesSISTRAPException(OperacionesSISTRAPException.ERROR),
						null));

			ServiceDefinition[] steps = { operacionesSISTRAP };
			Message<RequestPrestamo> response = operacionesSISTRAP.executeSteps(steps, new Message<>(request));
			return toResponse(response);
		} catch (BusinessException e) {
			log.log(Level.SEVERE, "ERROR AutorizarPrestamoEndPoint.operacionesSISTRAP datos[" + request + "]", e);
			return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
		} catch (Exception e) {
			log.log(Level.SEVERE, "ERROR AutorizarPrestamoEndPoint.operacionesSISTRAP datos[" + request + "]", e);
			return toResponse(new Message(null,
                    ServiceStatusEnum.EXCEPCION,
                    new OperacionesSISTRAPException(OperacionesSISTRAPException.ERROR_DESCONOCIDO),
                    null)
            );
		}
	}
    
    @POST
    @Path("/reinstalacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarEstadoSolicitud(SolicitudEstado solicitudEstado) throws BusinessException {
        try {
            if (solicitudEstado.getIdSolicitud() == null || solicitudEstado.getIdEstado() == null) {
                return toResponse(new Message<>(
                        null,
                        ServiceStatusEnum.EXCEPCION,
                        new AutorizarPrestamoException(AutorizarPrestamoException.MENSAJE_DE_SOLICITUD_INCORRECTO),
                        null
                ));
            }
            ServiceDefinition[] steps ={actualizaEstadoService, documentosReinstalacionService};
            Message<SolicitudEstado> respuesta = actualizaEstadoService.executeSteps(steps, new Message<>(solicitudEstado));
            return Response.ok(respuesta.getPayload().getSolicitud()).build();
        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR AutorizarPrestamoEndPoint.actualizarEstadoSolicitud() ", e);
            return toResponse(new Message(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    null,
                    null
            ));
        }
    }
}
