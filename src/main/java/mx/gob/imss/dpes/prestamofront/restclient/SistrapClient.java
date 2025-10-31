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
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoRequest;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author eduardo.loyo
 */
@Path("/prestamoSistrap")
@RegisterRestClient
public interface SistrapClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(RegistroPrestamoRequest request);
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response valida(RegistroPrestamoRequest request);

}
