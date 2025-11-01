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
import mx.gob.imss.dpes.prestamofront.exception.PrestamoException;
import mx.gob.imss.dpes.prestamofront.model.Documentacion;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.ObtenerDocumentosClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class UpdateAutorizacionDocumentsService extends ServiceDefinition<RequestPrestamo, RequestPrestamo> {

    @Inject
    @RestClient
    private ObtenerDocumentosClient obtenerDocumentos;

     @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
        log.log(Level.INFO, "########## Step 3 ##########");
        log.log(Level.INFO, "########## UpdateAutorizacionDocumentsService new updateDocument Obteniendo Contrato ##########");
//        log.log(Level.INFO, ">>>ObtenerContratoService new updateDocument Obteniendo Contrato: {0}", request.getPayload());

        if (request.getPayload().getDocumentacion() != null) {
            Long cveSolictud = request.getPayload().getSolicitud().getId();
            this.updateDocument(request.getPayload().getDocumentacion().getIdContrato(),cveSolictud , TipoDocumentoEnum.CONTRATO);
            this.updateDocument(request.getPayload().getDocumentacion().getIdCartaInstruccion(),cveSolictud , TipoDocumentoEnum.CARTA_INSTRUCCION);
            this.updateDocument(request.getPayload().getDocumentacion().getIdIdentificacionOficial(),cveSolictud , TipoDocumentoEnum.IDENTIFICACION_OFICIAL);
            return new Message<>(request.getPayload());

        }
        return response(null, ServiceStatusEnum.EXCEPCION, new PrestamoException(), null);

    }

    protected void updateDocument(Long id, Long cveSolicitud, TipoDocumentoEnum tipoDocumento) throws BusinessException {
        Documento documento = new Documento();
        documento.setId(id);
        documento.setTipoDocumento(tipoDocumento);
        documento.setCveSolicitud(cveSolicitud);
        Response response = obtenerDocumentos.cleanAndUpdateSolicitud(documento);
        if (response.getStatus() != 200) {
            log.log(Level.SEVERE, ">>>ERROR ObtenerContratoService, error al actualizar documento=" + documento);
            throw new PrestamoException(PrestamoException.DOCTOS_UPDATE_ERROR);
        }
    }
}
