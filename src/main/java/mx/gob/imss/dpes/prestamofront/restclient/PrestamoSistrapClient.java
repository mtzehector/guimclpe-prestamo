/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.restclient;

/**
 *
 * @author antonio
 */
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.interfaces.sipre.model.ConsultaPrestamosEnRecuperacionRQ;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoRQ;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoRequest;
import mx.gob.imss.dpes.prestamofront.model.sistrap.PrestamoSistrapRequest;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/webresources")
@RegisterRestClient
public interface PrestamoSistrapClient{

    @Path("/prestamo")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response load(PrestamoSistrapRequest request);

    
    @Path("/prestamosEnRecuperacion")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultaPrestamosEnRecuperacion(ConsultaPrestamosEnRecuperacionRQ request);
    
    @POST
    @Path("/prestamo/registroPrestamo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RegistroPrestamoRQ save(RegistroPrestamoRequest request);
    
}
