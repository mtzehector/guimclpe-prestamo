/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.prestamofront.exception.DocumentoException;
import mx.gob.imss.dpes.prestamofront.restclient.DocumentoClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class CrearDocumentoService extends ServiceDefinition<Reporte, Reporte> {

    @Inject
    @RestClient
    private DocumentoClient client;

    @Override
    public Message<Reporte> execute(Message<Reporte> request) throws BusinessException {
        //log.log(Level.INFO, ">>>prestamoFront|CrearDocumentoService|execute {0}", request.getPayload());
        log.log(Level.INFO, ">>>prestamoFront|CrearDocumentoService|execute");

        Documento documentoIn = new Documento();

        switch (request.getPayload().getFlagReporte()) {
            case 1:
                documentoIn.setTipoDocumento(TipoDocumentoEnum.REPORTE_CONCILIACION_XLSX);
                documentoIn.setDescTipoDocumento(TipoDocumentoEnum.REPORTE_CONCILIACION_XLSX.getDescripcion());
                break;
            case 2:
                documentoIn.setTipoDocumento(TipoDocumentoEnum.REPORTE_INCONSISTENCIAS);
                documentoIn.setDescTipoDocumento(TipoDocumentoEnum.REPORTE_INCONSISTENCIAS.getDescripcion());
                break;
            case 3:
                documentoIn.setTipoDocumento(TipoDocumentoEnum.REPORTE_DESCUENTOS_EMITIDOS_POR_EF);
                documentoIn.setDescTipoDocumento(TipoDocumentoEnum.REPORTE_DESCUENTOS_EMITIDOS_POR_EF.getDescripcion());
                break;
            case 4:
                documentoIn.setTipoDocumento(TipoDocumentoEnum.REPORTE_DESCUENTOS_EMITIDOS_POR_DEL);
                documentoIn.setDescTipoDocumento(TipoDocumentoEnum.REPORTE_DESCUENTOS_EMITIDOS_POR_DEL.getDescripcion());
                break;
            case 5:
                documentoIn.setTipoDocumento(TipoDocumentoEnum.REPORTE_INCONSISTENCIAS_PRESTAMOS);
                documentoIn.setDescTipoDocumento(TipoDocumentoEnum.REPORTE_INCONSISTENCIAS_PRESTAMOS.getDescripcion());
                break;
            case 6:
                documentoIn.setTipoDocumento(TipoDocumentoEnum.REPORTE_INCONSISTENCIAS_DESCUENTOS);
                documentoIn.setDescTipoDocumento(TipoDocumentoEnum.REPORTE_INCONSISTENCIAS_DESCUENTOS.getDescripcion());
                break;                
            case 7:
                documentoIn.setTipoDocumento(TipoDocumentoEnum.REPORTE_COMPRAS_CARTERA_EF);
                documentoIn.setDescTipoDocumento(TipoDocumentoEnum.REPORTE_COMPRAS_CARTERA_EF.getDescripcion());
                break;
        }

        documentoIn.setCveSolicitud(1L);
        documentoIn.setRefDocBoveda(request.getPayload().getBoveda().getRespuestaBoveda().getIdDocumento());

        log.log(Level.INFO, ">>>prestamoFront|CrearDocumentoService|execute createOrUpdate: {0}", documentoIn);

        Response load = client.createOrUpdate(documentoIn);
        if (load.getStatus() == 200) {
            Documento documento = load.readEntity(Documento.class);

            switch (request.getPayload().getFlagReporte()) {
                case 1:
                    request.getPayload().setDocConciliacion(documento);
                    break;
                case 2:
                    request.getPayload().setDocInconsistencias(documento);
                    break;
                case 3:
                    request.getPayload().setDocDescEmitByDel(documento);
                    break;
                case 4:
                    request.getPayload().setDocDescEmitByEF(documento);
                    break;
                case 5:
                    request.getPayload().setDocInconsPrestamos(documento);
                    break;
                case 6:
                    request.getPayload().setDocInconsDescuentos(documento);
                    break;          
                case 7:
                    request.getPayload().setDocComprasDeCarteraEF(documento);
                    break;
            }

            return request;
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new DocumentoException(), null);
    }
}
