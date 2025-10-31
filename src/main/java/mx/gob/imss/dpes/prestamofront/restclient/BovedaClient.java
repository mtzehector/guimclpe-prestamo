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
import mx.gob.imss.dpes.prestamofront.model.BovedaRequest;
import mx.gob.imss.dpes.prestamofront.model.Documento;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author edgar.arenas
 */
@RegisterRestClient
@Path("/boveda")
public interface BovedaClient {
    
    @POST
    @Path("/load")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Consumo de Boved",
            description = "uso de la boveda")
    public Response load(RequestPrestamo request);
    
    @POST
    @Path("/loadBovedaV3")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Consumo de Boved",
            description = "uso de la boveda")
    public Response loadBovedaV3(RequestPrestamo request);
    
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(BovedaRequest request);
}
