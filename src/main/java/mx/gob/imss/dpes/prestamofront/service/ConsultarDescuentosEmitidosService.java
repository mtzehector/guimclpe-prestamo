/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraInterfaz;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.interfaces.sipre.model.ConsultaDescuentoEmitidoRequest;
import mx.gob.imss.dpes.interfaces.sipre.model.ConsultaDescuentoEmitidoResponse;
import mx.gob.imss.dpes.interfaces.sipre.model.ConsultaDescuentosEmitidos;
import mx.gob.imss.dpes.prestamofront.exception.ReporteExcepcion;
import mx.gob.imss.dpes.prestamofront.restclient.DescuentosEmitidosClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ConsultarDescuentosEmitidosService extends ServiceDefinition<Reporte, Reporte> {

    @Inject
    @RestClient
    private DescuentosEmitidosClient client;

    @Override
    public Message<Reporte> execute(Message<Reporte> request) throws BusinessException {
        //log.log(Level.INFO, ">>>prestamoFront|ConsultarDescuentosEmitidosService|execute {0}",request.getPayload());
//        ConsultaDescuentosEmitidos descuentos = new ConsultaDescuentosEmitidos();
//        ConsultaDescuentoEmitidoRequest desRequest = new ConsultaDescuentoEmitidoRequest();
//        desRequest.setPeriodoNomina(request.getPayload().getAnioNominal().concat(request.getPayload().getMesNominal()));
//        descuentos.setDescuentosRequest(desRequest);
//
//        BitacoraInterfaz bi = new BitacoraInterfaz();
//        bi.setEndpoint("POST http://localhost/sistrap/webresources/descuentosEmitidos");
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//
//        try {
//            bi.setDescRequest(mapper.writeValueAsString(descuentos));
//        } catch (JsonProcessingException ex) {
//            Logger.getLogger(ConsultarDescuentosEmitidosService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        request.getPayload().setBitacoraInterfaz(bi);
//        
//        Response respuesta = null;
//        Response respuesta2= null;
//        ConsultaDescuentosEmitidos descuentosEmitidos2 = null;
        switch (request.getPayload().getTipoReporte().intValue()) {
            case 1:
                //respuesta = client.load(descuentos);
                return consiliacionIMSS(request);
                //break;
            case 2:
                // TODO: 
                break;          
            case 3:
                return descuentosEmitidos(request);
                //break;
            default:
                return response(null, ServiceStatusEnum.EXCEPCION, new ReporteExcepcion("Tipo de reporte inválido"), null);
        }

//        if (respuesta.getStatus() == 200) {
//
//            ConsultaDescuentosEmitidos descuentosEmitidos = respuesta.readEntity(ConsultaDescuentosEmitidos.class);
//            
//            request.getPayload().setDescuentosResponse(descuentosEmitidos.getDescuentosResponse());
//            
//            if(request.getPayload().getTipoReporte().intValue()==3){
//                request.getPayload().getDescuentosResponse().setDescuentosVoList(descuentosEmitidos2.getDescuentosResponse().getDescuentosVoList());
//            }
//
//            try {
//                bi.setReponseEndpoint(mapper.writeValueAsString(request.getPayload().getDescuentosResponse()));
//            } catch (JsonProcessingException ex) {
//                Logger.getLogger(ConsultarDescuentosEmitidosService.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            
//            return request;
//        }
        return response(null, ServiceStatusEnum.EXCEPCION, null, null);
    }

    public Message<Reporte> consiliacionIMSS(Message<Reporte> request) throws BusinessException {     
        log.log(Level.INFO, ">>>prestamoFront|ConsultarDescuentosEmitidosService|consiliacionIMSS {0}",request.getPayload());

        ConsultaDescuentosEmitidos descuentos = new ConsultaDescuentosEmitidos();
        ConsultaDescuentoEmitidoRequest desRequest = new ConsultaDescuentoEmitidoRequest();
        desRequest.setPeriodoNomina(request.getPayload().getAnioNominal().concat(request.getPayload().getMesNominal()));
        descuentos.setDescuentosRequest(desRequest);
        
        Response respuesta = null;

        BitacoraInterfaz bi = new BitacoraInterfaz();
        bi.setEndpoint("POST http://localhost/sistrap/webresources/descuentosEmitidos");

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            bi.setDescRequest(mapper.writeValueAsString(descuentos));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ConsultarDescuentosEmitidosService.class.getName()).log(Level.SEVERE, null, ex);
        }

        request.getPayload().setBitacoraInterfaz(bi);
        
        respuesta = client.load(descuentos);
        
        if (respuesta.getStatus() == 200) {

            ConsultaDescuentosEmitidos descuentosEmitidos = respuesta.readEntity(ConsultaDescuentosEmitidos.class);
            
            request.getPayload().setDescuentosResponse(descuentosEmitidos.getDescuentosResponse());
            
            try {
                bi.setReponseEndpoint(mapper.writeValueAsString(request.getPayload().getDescuentosResponse()));
            } catch (JsonProcessingException ex) {
                Logger.getLogger(ConsultarDescuentosEmitidosService.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
            log.log(Level.INFO, ">>>prestamoFront|ConsultarDescuentosEmitidosService|consiliacionIMSS FINAL {0}",request.getPayload());

            return request;
        }
        return response(null, ServiceStatusEnum.EXCEPCION, null, null);
    }
    
    public Message<Reporte> descuentosEmitidos(Message<Reporte> request){
          
        log.log(Level.INFO, ">>>prestamoFront|ConsultarDescuentosEmitidosService|descuentosEmitidos JGV 1 {0}",request.getPayload());

        ConsultaDescuentosEmitidos descuentos = new ConsultaDescuentosEmitidos();
        ConsultaDescuentoEmitidoRequest desRequest = new ConsultaDescuentoEmitidoRequest();
        desRequest.setPeriodoNomina(request.getPayload().getAnioNominal().concat(request.getPayload().getMesNominal()));
        descuentos.setDescuentosRequest(desRequest);
        
        BitacoraInterfaz bi = new BitacoraInterfaz();
        bi.setEndpoint("POST http://localhost/sistrap/webresources/ConsultaPrestamosconDescuento http://localhost/sistrap/webresources/ConsultaPrestamosEnCursoPorMovimiento");

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            bi.setDescRequest(mapper.writeValueAsString(descuentos));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ConsultarDescuentosEmitidosService.class.getName()).log(Level.SEVERE, null, ex);
        }

        request.getPayload().setBitacoraInterfaz(bi);

        
        ConsultaDescuentoEmitidoResponse cder = new ConsultaDescuentoEmitidoResponse();

        
        Response respPrestamos = 
                client.consultaPrestamosconDescuento(descuentos);
        
        if (respPrestamos.getStatus() == 200) {

            ConsultaDescuentosEmitidos prestamosEnCursoVoList = 
                    respPrestamos.readEntity(ConsultaDescuentosEmitidos.class);
                    
            //log.log(Level.INFO, ">>>ConsultarDescuentosEmitidosService|descuentosEmitidos descuentosVoList JGV {0}",descuentosVoList);

            cder.setPrestamosEnCursoVoList(prestamosEnCursoVoList.getDescuentosResponse().getPrestamosEnCursoVoList());
            
        } else {
                
            return response(null, ServiceStatusEnum.EXCEPCION, null, null);

        }
        
        Response resDescuentos = 
                client.consultaPrestamosEnCursoPorMovimiento(descuentos);
              
        if (resDescuentos.getStatus() == 200) {

            ConsultaDescuentosEmitidos descuentosVoList = 
                    resDescuentos.readEntity(ConsultaDescuentosEmitidos.class);
            
            //log.log(Level.INFO, ">>>ConsultarDescuentosEmitidosService|descuentosEmitidos prestamosEnCursoVoList JGV {0}",prestamosEnCursoVoList);
            
            cder.setDescuentosVoList(descuentosVoList.getDescuentosResponse().getDescuentosVoList());
            
        } else {
        
            return response(null, ServiceStatusEnum.EXCEPCION, null, null);
    
        }
        
                
        //log.log(Level.INFO, ">>>ConsultarDescuentosEmitidosService|descuentosEmitidos cder JGV {0}",cder);

        
        request.getPayload().setDescuentosResponse(cder);
         
        try {
        
            bi.setReponseEndpoint(mapper.writeValueAsString(request.getPayload().getDescuentosResponse()));
            
        } catch (JsonProcessingException ex) {
        
            Logger.getLogger(ConsultarDescuentosEmitidosService.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        //log.log(Level.INFO, ">>>prestamoFront|ConsultarDescuentosEmitidosService|descuentosEmitidos FINAL {0}",request.getPayload());

        
        return request;
        
    }
    
}
