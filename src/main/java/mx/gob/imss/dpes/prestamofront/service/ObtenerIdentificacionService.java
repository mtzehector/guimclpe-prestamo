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
 * @author juanf.barragan
 */
@Provider
public class ObtenerIdentificacionService extends ServiceDefinition<RequestPrestamo, RequestPrestamo> {
    
     @Inject
    @RestClient
    private ObtenerDocumentosClient obtenerDocumentos;

    @Inject
    BovedaService boveda;
    
        public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
    	log.log(Level.INFO, "########## Step 5 ##########");
    	log.log(Level.INFO, "########## ObtenerIdentificacionService new updateDocument Obteniendo Identificacion ##########");

        if (request.getPayload().getDocumentacion() != null) {
            request.getPayload().setFlatDoc(TipoDocumentoEnum.IDENTIFICACION_OFICIAL.toValue());
            request.getPayload().getDocumento().setIdDocumento(request.getPayload().getDocumentacion().getRefIdentificacionOficial());
            
            log.log(Level.INFO, "########## cveDocumento contrato [{0}] ##########", request.getPayload().getDocumentacion().getIdIdentificacionOficial());
            log.log(Level.INFO, "########## RefBovedaContrato [{0}] ##########", request.getPayload().getDocumentacion().getRefIdentificacionOficial());
            
            if (request.getPayload().getDocumentacion().getIdIdentificacionOficial() != null) {
            	log.log(Level.INFO, "########## Consultando a documentoBack a la tabla de MCLT_DOCUMENTO con la CVE_DOCUMENTO [{0}] ##########", request.getPayload().getDocumentacion().getIdIdentificacionOficial());
            	Response responseLoad = obtenerDocumentos.load(request.getPayload().getDocumentacion().getIdIdentificacionOficial());
            	 if (responseLoad.getStatus() == 200) {
                 	Documento documento  = responseLoad.readEntity(Documento.class);
                 	log.log(Level.INFO, "########## indDocumentoHistorico [{0}] ##########", documento.isIndDocumentoHistorico());
                 	request.getPayload().getDocumento().setIndDocumentoHistorico(documento.isIndDocumentoHistorico());
            	 }
			}
            
            boveda.execute(request);
            
            log.log(Level.INFO, ">>>Termina Obtener identificacion");
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new PrestamoException(), null);

    }
    
}
