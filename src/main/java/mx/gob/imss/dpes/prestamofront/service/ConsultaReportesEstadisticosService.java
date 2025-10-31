/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.reportes.model.PrestamoAutorizadoDetalleDelegacion;
import mx.gob.imss.dpes.interfaces.reportes.model.PrestamoAutorizadoDetallePorEF;
import mx.gob.imss.dpes.interfaces.reportes.model.PrestamoAutorizadoPorEF;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.interfaces.reportes.model.ReportePrestamosAutorizadosDetalleDelegacion;
import mx.gob.imss.dpes.interfaces.reportes.model.ReportePrestamosAutorizadosDetallePorEF;
import mx.gob.imss.dpes.interfaces.reportes.model.ReportePrestamosAutorizadosPorEF;
import mx.gob.imss.dpes.prestamofront.exception.ReporteExcepcion;
import mx.gob.imss.dpes.prestamofront.model.PrestamosAutorizadosEFRQ;
import mx.gob.imss.dpes.prestamofront.restclient.ReporteClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juan.garfias
 */
@Provider
public class ConsultaReportesEstadisticosService extends ServiceDefinition<Reporte, Reporte> {

    private List<PrestamoAutorizadoPorEF> lstPrestamoAutorizadoPorEF = new ArrayList<>();
    private List<PrestamoAutorizadoDetallePorEF> lstPrestamosAutorizadosDetalleEF = new ArrayList<>();
    private List<PrestamoAutorizadoDetalleDelegacion> lstPrestamoAutorizadoDetalleDelegacion = new ArrayList<>();
    
    

    private ReportePrestamosAutorizadosPorEF rp1 = new ReportePrestamosAutorizadosPorEF();
    private ReportePrestamosAutorizadosDetallePorEF rp2 = new ReportePrestamosAutorizadosDetallePorEF();
    private ReportePrestamosAutorizadosDetalleDelegacion rp3 = new ReportePrestamosAutorizadosDetalleDelegacion();

    @Inject
    @RestClient
    ReporteClient client;

    @Override
    public Message<Reporte> execute(Message<Reporte> request) throws BusinessException {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date1 = simpleDateFormat.format(request.getPayload().getFechaDesde());
        String date2 = simpleDateFormat.format(request.getPayload().getFechaHasta());

        Response response = null;

        switch (request.getPayload().getTipoReporte().intValue()) {
            case 5:

                response = client.prestamosAutorizadosEF(new PrestamosAutorizadosEFRQ(
                        date1, date2
                ));

                lstPrestamoAutorizadoPorEF = response.readEntity(
                        new GenericType<List<PrestamoAutorizadoPorEF>>() {
                }
                );

                rp1.setRows(lstPrestamoAutorizadoPorEF);
                request.getPayload().setReportePrestamosAutorizadosPorEF(rp1);
                return request;
            case 6:

                response = client.prestamosAutorizadosDetalleEF(new PrestamosAutorizadosEFRQ(
                        date1, date2
                ));

                lstPrestamosAutorizadosDetalleEF = response.readEntity(
                        new GenericType<List<PrestamoAutorizadoDetallePorEF>>() {
                }
                );

                rp2.setRows(lstPrestamosAutorizadosDetalleEF);
                request.getPayload().setReportePrestamosAutorizadosDetallePorEF(rp2);
                return request;
            case 7:
                  response = client.prestamosAutorizadosDetalleDelegacion(new PrestamosAutorizadosEFRQ(
                        date1, date2
                ));

                lstPrestamoAutorizadoDetalleDelegacion = response.readEntity(
                        new GenericType<List<PrestamoAutorizadoDetalleDelegacion>>() {
                }
                );

                rp3.setRows(lstPrestamoAutorizadoDetalleDelegacion);
                request.getPayload().setReportePrestamosAutorizadosDetalleDelegacion(rp3);
                return request;
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new ReporteExcepcion("Tipo de reporte inválido"), null);

    }

}
