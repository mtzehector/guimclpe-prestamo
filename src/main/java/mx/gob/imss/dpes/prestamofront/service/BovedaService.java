/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.prestamofront.model.Documento;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.BovedaClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class BovedaService extends ServiceDefinition<RequestPrestamo, RequestPrestamo>{
    
    @Inject
    @RestClient
    private BovedaClient client;
 
    @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
        log.log(Level.INFO, "******************************Step Obtener documento desde prestamoFront******************************************");
        log.log(Level.INFO, "Inicia Boveda obtención");
        //se inserta solicitud

//        log.log(Level.INFO, "Request hacia Boveda download: {0}", request.getPayload());
        
        Response load;
        
        if (request.getPayload().getDocumento().isIndDocumentoHistorico()) {
            log.log(Level.INFO, "########## Es un documento historico y la solicitud es a la boveda V2 ##########");
            load = client.load(request.getPayload());
        } else {
            log.log(Level.INFO, "########## No es un documento historico y la solicitud es a la boveda V3 ##########");
            load = client.loadBovedaV3(request.getPayload());
        }
        
        if (load.getStatus() == 200) {
            log.log(Level.INFO,"Respondio 200 ok ");
            RequestPrestamo bovedaOut = load.readEntity(RequestPrestamo.class);
            log.log(Level.INFO,"Respondio1 200 ok ", request.getPayload().getFlatDoc().intValue());
            switch(request.getPayload().getFlatDoc().intValue()){
                case 3://Carta de Libranza
                    request.getPayload().getDocumento().setCarta(bovedaOut.getRespuestaBoveda().getArchivo());
                    break;
                case 5://Contrato
                    request.getPayload().getDocumento().setContrato(bovedaOut.getRespuestaBoveda().getArchivo());
                    break;
                case 6://Amortizacion
                    request.getPayload().getDocumento().setAmortizacion(bovedaOut.getRespuestaBoveda().getArchivo());
                    break;
                case 4://Identificacion
                    request.getPayload().getDocumento().setIdentificacion(bovedaOut.getRespuestaBoveda().getArchivo());
                    break;
                default:
                    break;
            }
                               
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, null, null);
    }
}
