/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.endpoint;

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
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.prestamofront.model.RequestSistrap;
import mx.gob.imss.dpes.prestamofront.service.SistrapGuardarService;
import mx.gob.imss.dpes.prestamofront.service.UpdateEstadoService;
import mx.gob.imss.dpes.prestamofront.service.UpdateEstadoSolicitudService;
import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 *
 * @author eduardo.loyo
 */
@Path("/guardadosistra")
@RequestScoped
public class PrestamoSistrapEndPoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel> {

    @Inject
    private SistrapGuardarService sistraGuardar;

    @Inject
    private UpdateEstadoService updateEstado;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Consume sistrap",
            description = "consume sistrap")
    public Response create(RequestSistrap request) throws BusinessException {

        ServiceDefinition[] steps = {sistraGuardar, updateEstado};
        Message<RequestSistrap> response = sistraGuardar.executeSteps(steps, new Message<>(request));

        return toResponse(response);
    }
}
