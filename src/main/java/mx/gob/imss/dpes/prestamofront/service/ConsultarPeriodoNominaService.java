/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.model.Message;

import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.prestamofront.exception.PrestamosVigentesException;
import mx.gob.imss.dpes.prestamofront.model.PrestamoVigenteComposite;
import mx.gob.imss.dpes.prestamofront.model.PrestamosVigentesComposite;
import mx.gob.imss.dpes.prestamofront.model.mclpe.PeriodoNominaMclpeRequest;
import mx.gob.imss.dpes.prestamofront.model.mclpe.PeriodoNominaMclpeResponse;
import mx.gob.imss.dpes.prestamofront.restclient.PeriodoNominaClient;


@Provider
public class ConsultarPeriodoNominaService extends ServiceDefinition<PrestamosVigentesComposite, PrestamosVigentesComposite>{

  
  @Inject
  @RestClient
  private PeriodoNominaClient periodoNominaClient;
  
  
  public Message<PrestamosVigentesComposite> execute(Message<PrestamosVigentesComposite> request) {
    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    
    PeriodoNominaMclpeRequest periodoNominaMclpeRequest = new PeriodoNominaMclpeRequest();
    periodoNominaMclpeRequest.setFechaBusqueda(formateador.format(new Date()));
    
    log.log(Level.INFO, "Invocando el servicio, {0}", periodoNominaMclpeRequest.getFechaBusqueda());
    
    Response load = periodoNominaClient.load(periodoNominaMclpeRequest);
    if( load.getStatus() == 200 ){
        
      PeriodoNominaMclpeResponse periodoNominaMclpeResponse = load.readEntity(PeriodoNominaMclpeResponse.class);
      
      for (PrestamoVigenteComposite prestamoVigente : request.getPayload().getPrestamosVigentesComposite()) {
          prestamoVigente.setPeriodoNominaMclpeResponse(periodoNominaMclpeResponse);
      }
       return request; 
    }    
    return response(null, ServiceStatusEnum.EXCEPCION, new PrestamosVigentesException(), null );
  }
  
  
}
