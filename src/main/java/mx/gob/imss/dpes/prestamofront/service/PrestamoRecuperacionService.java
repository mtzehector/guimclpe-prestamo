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
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.prestamofront.exception.PrestamoException;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.PrestamoClient;
import mx.gob.imss.dpes.prestamofront.rules.FechaLimite;
import mx.gob.imss.dpes.prestamofront.rules.ValidarMontos;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class PrestamoRecuperacionService extends ServiceDefinition<RequestPrestamo, RequestPrestamo> {
    
    @Inject
    @RestClient
    private PrestamoClient service;
    
    @Inject
    ValidarMontos rule1;
    
    @Inject
    FechaLimite rule2;
    
    @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
        log.log(Level.INFO, ">>>prestamoFront|PrestamoRecuperacionService|execute {0}", request.getPayload());
       
        //rule1.apply(request.getPayload());
        //rule2.apply(request.getPayload());
        //if(request.getPayload().getFlatMonto() == 1 && request.getPayload().getFlatFecha() == 1){
        Response load = null;
        for(PrestamoRecuperacion p: request.getPayload().getListPrestamoRecuperacion().getPrestamosEnRecuperacion()){
             p.setMontoActualizado(1L);
             load = service.registrarMonto(p);
             if (load.getStatus() != 200) {
                return response(null, ServiceStatusEnum.EXCEPCION, new PrestamoException(), null);
            }
        }
        return new Message<>(request.getPayload());
    }
}
