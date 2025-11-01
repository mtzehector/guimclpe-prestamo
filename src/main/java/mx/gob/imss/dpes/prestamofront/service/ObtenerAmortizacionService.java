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
public class ObtenerAmortizacionService extends ServiceDefinition<RequestPrestamo, RequestPrestamo>{
    
     @Inject
    @RestClient
    private ObtenerDocumentosClient obtenerDocumentos;
    
    @Inject
    BovedaService boveda;
    
     @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
        
        log.log(Level.INFO,">>>Obteniendo Amortizacion: {0}", request.getPayload());
        
        if(request.getPayload().getDocumentacion() != null){
             request.getPayload().setFlatDoc(TipoDocumentoEnum.TABLA_DE_AMORTIZACION_DE_CREDITO.toValue());
                request.getPayload().getDocumento().setIdDocumento(request.getPayload().getDocumentacion().getRefBovedaAmortizacion());
                boveda.execute(request);
            log.log(Level.INFO,">>>Termina Amortizacion");
        return new Message<>(request.getPayload()); 
        }
        
        return response(null, ServiceStatusEnum.EXCEPCION, new PrestamoException(), null);
        
//        Response load;
//        load = obtenerDocumentos.load(request.getPayload().getIdsDocumentosAutorizar()[2]);
//        if (load.getStatus() == 200) {
//                    
//            Documento doc = load.readEntity(Documento.class);
//            request.getPayload().getDocumento().setIdDocumento(doc.getRefDocBoveda());
//            request.getPayload().setFlatDoc(TipoDocumentoEnum.TABLA_DE_AMORTIZACION_DE_CREDITO.toValue());
//            boveda.execute(request);
//            log.log(Level.INFO,">>>Termina Amortizacion");
//            return new Message<>(request.getPayload());
//        }
      //  return response(null, ServiceStatusEnum.EXCEPCION, new PrestamoException(), null);
    }
}
