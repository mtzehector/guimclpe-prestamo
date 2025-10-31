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
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.model.Message;

import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import mx.gob.imss.dpes.prestamofront.exception.PrestamosVigentesException;
import mx.gob.imss.dpes.prestamofront.model.PrestamoVigenteComposite;
import mx.gob.imss.dpes.prestamofront.model.PrestamosVigentesComposite;
import mx.gob.imss.dpes.prestamofront.model.mclpe.PrestamosBySolicitudesMclpeRequest;
import mx.gob.imss.dpes.prestamofront.restclient.PrestamosVigentesClient;

/**
 *
 * @author antonio
 */
@Provider
public class ConsultarPrestamosVigentesService extends ServiceDefinition<PrestamosVigentesComposite, 
        PrestamosVigentesComposite>{

  
  @Inject
  @RestClient
  private PrestamosVigentesClient prestamosVigentesClient;
  
   public Message<PrestamosVigentesComposite> execute(Message<PrestamosVigentesComposite> request) {
    log.log(Level.INFO, "Iniciando las consultas de prestamos por solicitudes");
    PrestamosBySolicitudesMclpeRequest prestamosBySolicitudesMclpeRequest 
                = new PrestamosBySolicitudesMclpeRequest(request.getPayload().getPrestamosVigentesComposite());
        
    //Obtenemos la lista de prestamos vigentes de la propia BD
    log.log(Level.INFO, "Se llama al servicio de prestamos back");
    
    // Solo si hay prestamos en la lista
    if( prestamosBySolicitudesMclpeRequest.getSolicitudes().isEmpty() ){
      return request;
    }
    
    Response load = prestamosVigentesClient.load(prestamosBySolicitudesMclpeRequest);
    if( load.getStatus() == 200 ){
      log.log(Level.INFO, "Se recibió respuesta exitosa de los prestamos");
       List<Prestamo> listaPrestamosVigentes
              = load.readEntity(new GenericType<List<Prestamo>>() {});
      log.info("numero de prestamosVigentes :" + listaPrestamosVigentes.size());
      
      request.getPayload().setPrestamosVigentesComposite(this.poblarPrestamos(listaPrestamosVigentes, 
              request.getPayload().getPrestamosVigentesComposite()));
      
      
       return request; 
    }
    
    //TODO: Cambiar por excepcion de infraestructura
    return response(null, ServiceStatusEnum.EXCEPCION, new PrestamosVigentesException(), null );
  }
    
    private List<PrestamoVigenteComposite> poblarPrestamos(List<Prestamo> prestamos,
            List<PrestamoVigenteComposite> prestamosCompuestos){
        
        List<PrestamoVigenteComposite> resultado = new ArrayList<>();
        for (Prestamo prestamoVigente : prestamos) {
            
            
            for (PrestamoVigenteComposite prestamoCompuesto : prestamosCompuestos) {                
                if(prestamoVigente.getSolicitud().equals(
                        prestamoCompuesto.getSolicitudVigenteMclpeResponse().getId())){
                    prestamoCompuesto.setPrestamoMclpeResponse(prestamoVigente);
                    resultado.add(prestamoCompuesto);
                    break;
                }
            }
        }
        return resultado;
    }
  
}
