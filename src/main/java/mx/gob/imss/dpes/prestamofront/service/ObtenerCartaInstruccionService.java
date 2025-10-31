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
public class ObtenerCartaInstruccionService extends ServiceDefinition<RequestPrestamo, RequestPrestamo>{
    
     @Inject
    @RestClient
    private ObtenerDocumentosClient obtenerDocumentos;
    
    @Inject
    BovedaService boveda;
    
    @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
        log.log(Level.INFO, "########## Step 5 ##########");
        log.log(Level.INFO, "########## ObtenerCartaInstruccionService Obteniendo Carta Instruccion ##########");
//      log.log(Level.INFO,">>>Obteniendo Carta Instruccion: {0}", request.getPayload());
        
        if(request.getPayload().getDocumentacion() != null){
            request.getPayload().setFlatDoc(TipoDocumentoEnum.CARTA_INSTRUCCION.toValue());
            request.getPayload().getDocumento().setIdDocumento(request.getPayload().getDocumentacion().getRefBovedaCarta());
            
            log.log(Level.INFO, "########## cveDocumento Carta Instruccion [{0}] ##########", request.getPayload().getDocumentacion().getIdCartaInstruccion());
            log.log(Level.INFO, "########## RefBovedaCarta [{0}] ##########", request.getPayload().getDocumentacion().getRefBovedaCarta());
            
            if (request.getPayload().getDocumentacion().getIdCartaInstruccion() != null) {
                log.log(Level.INFO, "########## Consultando a documentoBack a la tabla de MCLT_DOCUMENTO con la CVE_DOCUMENTO [{0}] ##########", request.getPayload().getDocumentacion().getIdCartaInstruccion());
                Response responseLoad = obtenerDocumentos.load(request.getPayload().getDocumentacion().getIdCartaInstruccion());
                 if (responseLoad.getStatus() == 200) {
                    Documento documento  = responseLoad.readEntity(Documento.class);
                    log.log(Level.INFO, "########## indDocumentoHistorico [{0}] ##########", documento.isIndDocumentoHistorico());
                    request.getPayload().getDocumento().setIndDocumentoHistorico(documento.isIndDocumentoHistorico());
                 }
            }
            
            boveda.execute(request);
            
            log.log(Level.INFO,">>>Termina Carta Instruccion");
            return new Message<>(request.getPayload());
        }
        
        return response(null, ServiceStatusEnum.EXCEPCION, new PrestamoException(), null);
        
//        Response load;
//        load = obtenerDocumentos.load(request.getPayload().getIdsDocumentosAutorizar()[1]);
//        if (load.getStatus() == 200) {
//                    
//            Documento doc = load.readEntity(Documento.class);
//            request.getPayload().getDocumento().setIdDocumento(doc.getRefDocBoveda());
//            request.getPayload().setFlatDoc(TipoDocumentoEnum.CARTA_INSTRUCCION.toValue());
//            boveda.execute(request);
//            log.log(Level.INFO,">>>Termina Carta Instruccion");
//            return new Message<>(request.getPayload());
//        }
//        return response(null, ServiceStatusEnum.EXCEPCION, new PrestamoException(), null);
    }
}
