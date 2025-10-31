/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.interfaces.reportesMclp.model.CompraCarteraEFRQ;
import mx.gob.imss.dpes.interfaces.reportesMclp.model.Reporte;
import mx.gob.imss.dpes.interfaces.reportesMclp.model.ReporteRq;
import mx.gob.imss.dpes.prestamofront.model.PrestamosAutorizadosEFRQ;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author edgar.arenas
 */
@Path("/reporte")
@RegisterRestClient
public interface ReporteClient {

    @GET
    @Path("/{periodoNomina}/{tipoReporte}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validarReporte(@PathParam("periodoNomina") String periodoNomina, @PathParam("tipoReporte") Long tipoReporte);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarReporte(ReporteRq request);

    @PUT
    @Path("/updateEstado")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEstadoReporte(Reporte request);

    @GET
    @Path("/correos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCorreos();

    @POST
    @Path("/compraCarteraEF")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response compraCarteraEF(CompraCarteraEFRQ request);

    @GET
    @Path("/{periodoNomina}/{tipoReporte}/{cveEF}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validarReporteEF(
            @PathParam("periodoNomina") String periodoNomina,
            @PathParam("tipoReporte") Long tipoReporte,
            @PathParam("cveEF") Long cveEntidadFinanciera);

    @POST
    @Path("/prestamosAutorizadosEF")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response prestamosAutorizadosEF(PrestamosAutorizadosEFRQ request);

    @POST
    @Path("/prestamosAutorizadosDetalleEF")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response prestamosAutorizadosDetalleEF(PrestamosAutorizadosEFRQ request);

    @POST
    @Path("/prestamosAutorizadosDetalleDelegacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response prestamosAutorizadosDetalleDelegacion(PrestamosAutorizadosEFRQ request);

}
