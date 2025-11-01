/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

/**
 *
 * @author edgar.arenas
 */


import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.prestamofront.exception.PrestamoException;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.PrestamoClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Provider
public class PrestamoRecuperacionCapacidad extends ServiceDefinition<RequestPrestamo, RequestPrestamo>{
    
    @Inject
    @RestClient
    private PrestamoClient service;
    
     @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
        log.log(Level.INFO, "Registro Monto Liquidar {0}", request.getPayload());
       
             
            request.getPayload().getPrestamoRecuperacion().setCanMontoSol(Double.parseDouble(request.getPayload().getMontoLiquidar()));
            Response load = service.registrarMonto(request.getPayload().getPrestamoRecuperacion());
            if (load.getStatus() == 200) {
                return new Message<>(request.getPayload());
            }
        
          return response(null, ServiceStatusEnum.EXCEPCION, new PrestamoException(), null);
    }
}
