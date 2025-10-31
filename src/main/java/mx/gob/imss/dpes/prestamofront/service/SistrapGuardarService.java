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
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoRequest;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoResponse;
import mx.gob.imss.dpes.prestamofront.assembler.SistrapAssembler;
import mx.gob.imss.dpes.prestamofront.exception.GuardaPrestamoException;
import mx.gob.imss.dpes.prestamofront.model.RequestSistrap;
import mx.gob.imss.dpes.prestamofront.restclient.SistrapClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class SistrapGuardarService extends ServiceDefinition<RequestSistrap, RequestSistrap> {

    @Inject
    @RestClient
    private SistrapClient service;

    @Inject
    private SistrapAssembler assembler;

    @Override
    public Message<RequestSistrap> execute(Message<RequestSistrap> request) throws BusinessException {
        log.log(Level.INFO, "Step 1");
        //se inserta solicitud
       
        
        RegistroPrestamoRequest requestSistrap = assembler.assemble(request.getPayload());
        log.log(Level.INFO, "Request hacia guardado Sistrap : {0}", requestSistrap);
        Response load = service.create(requestSistrap);
        if (load.getStatus() == 200) {
            RegistroPrestamoResponse out = load.readEntity(RegistroPrestamoResponse.class);
            request.getPayload().setSalida(out);
            return new Message<>(request.getPayload());
        }
        throw new GuardaPrestamoException();
        

    }

}
