/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.prestamofront.model.Prestamo;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/webresources/prestamopersistence")
@RegisterRestClient
public interface PrestamoBackClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Prestamo prestamo);

}
