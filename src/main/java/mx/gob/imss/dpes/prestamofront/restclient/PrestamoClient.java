/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author eduardo.loyo
 */
@Path("/prestamo")
@RegisterRestClient
public interface PrestamoClient {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response load(Prestamo request);
    
    @POST
    @Path("/prestamoEnRecuperacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarMonto(PrestamoRecuperacion request);
    
    @GET
    @Path("/prestamosEnRecuperacion/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultaPrestamosPorSolicitud(@PathParam("id") Long id);
}
