/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.reportes.model.PrestamoRecuperacionCompraCarteraEF;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.interfaces.reportes.model.ReporteCompraCarteraEF;
import mx.gob.imss.dpes.interfaces.reportesMclp.model.CompraCarteraEFRQ;
import mx.gob.imss.dpes.prestamofront.exception.ReporteExcepcion;
import mx.gob.imss.dpes.prestamofront.restclient.ReporteClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ConsultarPrestamosCompraCarteraEFService extends ServiceDefinition<Reporte, Reporte> {

    List<PrestamoRecuperacionCompraCarteraEF> prestamosCompraCarteraEF = new ArrayList<>();

    @Inject
    @RestClient
    ReporteClient client;

    @Override
    public Message<Reporte> execute(Message<Reporte> request) throws BusinessException {
        CompraCarteraEFRQ requestAux = new CompraCarteraEFRQ();
        requestAux.setCveEntidadFinanciera(request.getPayload().getCveEntidadFinanciera());
        requestAux.setPeriodoNomina(request.getPayload().getAnioNominal() + "" + request.getPayload().getMesNominal());
        Response response = client.compraCarteraEF(requestAux);
        if (response.getStatus() == 200) {
            prestamosCompraCarteraEF = response.readEntity(new GenericType<List<PrestamoRecuperacionCompraCarteraEF>>() {
            });
            ReporteCompraCarteraEF rccef = new ReporteCompraCarteraEF();

            rccef.setPrestamosCompraCarteraEF(prestamosCompraCarteraEF);

            request.getPayload().setReporteCompraCarteraEF(rccef);
            return request;
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new ReporteExcepcion("Tipo de reporte inválido"), null);
    }

    public PrestamoRecuperacionCompraCarteraEF poblar() {
        PrestamoRecuperacionCompraCarteraEF p = new PrestamoRecuperacionCompraCarteraEF();
        p.setCat(45.3);
        p.setCurp("XXXX123456XXXXX123");
        p.setDescuento(5678.23);
        p.setEmision(new Date());
        p.setFolio("00000001");
        p.setImporte(2345.23);
        p.setImporteLiquidado(2345234.23);
        p.setNombreComercial("Juanitos Bank");
        p.setNombreCompleto("Juanito Pirulero");
        p.setNss("1324123412");
        p.setNumDescuento(12);
        p.setNumeroDeProveedor("012");

        return p;

    }

}
