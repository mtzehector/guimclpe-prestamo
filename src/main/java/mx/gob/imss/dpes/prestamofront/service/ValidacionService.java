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
import mx.gob.imss.dpes.interfaces.bitacora.model.Bitacora;
import mx.gob.imss.dpes.prestamofront.exception.PrestamoException;
import mx.gob.imss.dpes.prestamofront.model.BitacoraModel;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.BitacoraClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class ValidacionService extends ServiceDefinition<RequestPrestamo, RequestPrestamo> {

    @Inject
    @RestClient
    private BitacoraClient client;

    @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
        log.log(Level.INFO, "Step 3 Valiacion");
        //se inserta solicitud

        Bitacora bitacorain = new Bitacora();
        bitacorain.setIdSolicitud(request.getPayload().getSolicitud().getId());
        log.log(Level.INFO, "Request Bitacora: {0}", bitacorain);
        Response load = client.getBitacoraBySolicitud(bitacorain);
        if (load.getStatus() == 200) {
            BitacoraModel bitacora = load.readEntity(BitacoraModel.class);
            log.log(Level.INFO, "Bitacora salida: {0}", bitacora);
            //TODO:VALIDACION 72 HORAS
           
            request.getPayload().setBitacora(bitacora);
            log.log(Level.INFO, "Respuesta Bitacora: {0}", bitacora);
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new PrestamoException(), null);
    }

}
