/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.prestamofront.restclient.BitacoraClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class BitacoraInterfazService extends ServiceDefinition<Reporte, Reporte> {

    @Inject
    @RestClient
    private BitacoraClient client;

    @Override
    public Message<Reporte> execute(Message<Reporte> request) throws BusinessException {

        //log.log(Level.INFO, ">>>prestamoFront|BitacoraReporteService|execute getBitacoraInterfaz {0} ", request.getPayload().getBitacoraInterfaz());

        client.createInterfaz(request.getPayload().getBitacoraInterfaz());
        return new Message<>(request.getPayload());

    }
}
