/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.endpoint;

import java.util.List;
import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;

import org.eclipse.microprofile.openapi.annotations.Operation;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.prestamofront.assembler.PrestamoVigenteCompositeAssembler;
import mx.gob.imss.dpes.prestamofront.assembler.PrestamosEnRecuperacionAssambler;
import mx.gob.imss.dpes.prestamofront.model.Pensionado;
import mx.gob.imss.dpes.prestamofront.model.PrestamoVigenteModel;
import mx.gob.imss.dpes.prestamofront.model.PrestamosVigentes;
import mx.gob.imss.dpes.prestamofront.model.PrestamosVigentesComposite;
import mx.gob.imss.dpes.prestamofront.service.ConsultaPrestamosEnRecuperacionSistrapService;
import mx.gob.imss.dpes.prestamofront.service.ConsultarCondicionOfertaService;
import mx.gob.imss.dpes.prestamofront.service.ConsultarPeriodoNominaService;
import mx.gob.imss.dpes.prestamofront.service.ConsultarPrestamoSistrapService;
import mx.gob.imss.dpes.prestamofront.service.ConsultarPrestamosVigentesService;
import mx.gob.imss.dpes.prestamofront.service.ConsultarSolicitudesVigentesService;

/**
 *
 * @author cesar.leon
 */
@Path("/prestamosVigentes")
@RequestScoped
public class PrestamosVigentesEndPoint extends BaseGUIEndPoint<PrestamosVigentes, Pensionado, PrestamosVigentes> {

    @Inject
    ConsultarPrestamosVigentesService consultarPrestamosVigentesService;

    @Inject
    ConsultarPrestamoSistrapService consultarPrestamoSistrapService;

    @Inject
    ConsultarSolicitudesVigentesService consultarSolicitudesVigentesService;

    @Inject
    ConsultarCondicionOfertaService consultarCondicionOfertaService;

    @Inject
    private PrestamoVigenteCompositeAssembler prestamoVigenteCompositeAssembler;

    @Inject
    private ConsultarPeriodoNominaService consultarPeriodoNominaService;

    @Inject
    private ConsultaPrestamosEnRecuperacionSistrapService consultaPrestamosEnRecuperacionSistrapService;

    @Inject
    private PrestamosEnRecuperacionAssambler enRecuperacionAssambler;

    @GET
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener los prestamos vigentes de un pensionado",
            description = "Obtener los prestamos vigentes de un pensionado")
    public Response load(Pensionado pensionado) throws BusinessException {

        log.log(Level.INFO, ">>>PrestamosVigentesEndPoint Consulta de prestamos: {0}", pensionado);

        PrestamosVigentesComposite prestamosVigentesComposite = new PrestamosVigentesComposite();
        prestamosVigentesComposite.setPensionado(pensionado);

        log.log(Level.INFO, ">>>PrestamosVigentesEndPoint Consulta de prestamos: {0}", prestamosVigentesComposite);

        ServiceDefinition[] steps = {
            consultarSolicitudesVigentesService,
            consultarPrestamosVigentesService,
            consultarCondicionOfertaService,
            consultarPeriodoNominaService,
            consultarPrestamoSistrapService,
            consultaPrestamosEnRecuperacionSistrapService
        };
        Message<PrestamosVigentesComposite> response
                = consultarSolicitudesVigentesService.executeSteps(steps, new Message<>(prestamosVigentesComposite));

        //log.log(Level.INFO, ">>>PrestamosVigentesEndPoint JUAN LOG ========= |> Despues de la consulta : {0}", response);
        List<PrestamoVigenteModel> prestamosVigentes
                = prestamoVigenteCompositeAssembler.assembleList(response.getPayload().getPrestamosVigentesComposite());

        List<PrestamoVigenteModel> prestamosEnRecuperacion
                = enRecuperacionAssambler.assembleList(response.getPayload().getPrestamosVigentesComposite()
                );

        //log.log(Level.INFO, ">>>PrestamosVigentesEndPoint JUAN LOG ========= |> Lista final : {0}", prestamosEnRecuperacion);
        return Response.ok(prestamosEnRecuperacion).build();
    }

}
