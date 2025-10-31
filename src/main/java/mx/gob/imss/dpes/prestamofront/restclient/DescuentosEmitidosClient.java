/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.interfaces.sipre.model.ConsultaDescuentosEmitidos;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author edgar.arenas
 */
@Path("/descuentosEmitidos")
@RegisterRestClient
public interface DescuentosEmitidosClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response load(ConsultaDescuentosEmitidos request);

    @Path("/consultaPrestamosconDescuento")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultaPrestamosconDescuento(ConsultaDescuentosEmitidos request);

    @Path("/consultaPrestamosEnCursoPorMovimiento")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultaPrestamosEnCursoPorMovimiento(ConsultaDescuentosEmitidos request);
}
