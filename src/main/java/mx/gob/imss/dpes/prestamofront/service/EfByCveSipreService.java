/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.prestamofront.restclient.EntidadFinancieraBackClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juan.garfias
 */
@Provider
public class EfByCveSipreService extends ServiceDefinition<EntidadFinanciera,EntidadFinanciera>{
     @Inject
    @RestClient
    private EntidadFinancieraBackClient backClient;
     
      @Override
    public Message<EntidadFinanciera> execute(Message<EntidadFinanciera> request) throws BusinessException {
        
        Response response = backClient.load(request.getPayload().getIdSipre());
        EntidadFinanciera e = response.readEntity(EntidadFinanciera.class);
        request.setPayload(e);
        return request;
    }
     
}
