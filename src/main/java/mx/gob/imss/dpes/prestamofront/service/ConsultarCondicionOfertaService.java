package mx.gob.imss.dpes.prestamofront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.model.Message;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Oferta;
import mx.gob.imss.dpes.prestamofront.model.PrestamoVigenteComposite;
import mx.gob.imss.dpes.prestamofront.model.PrestamosVigentesComposite;
import mx.gob.imss.dpes.prestamofront.model.mclpe.CondicionOfertaMclpeRequest;
import mx.gob.imss.dpes.prestamofront.restclient.CondicionOfertaClient;


@Provider
public class ConsultarCondicionOfertaService extends ServiceDefinition<PrestamosVigentesComposite, 
        PrestamosVigentesComposite>{
  
  @Inject
  @RestClient
  private CondicionOfertaClient condicionOfertaClient;
  
  public Message<PrestamosVigentesComposite> execute(Message<PrestamosVigentesComposite> request) {
    
      log.log(Level.INFO, "Iniciando las consultas de condiciones de oferta");
       CondicionOfertaMclpeRequest condicionOfertaMclpeRequest = null;
         Oferta condicionOfertaMclpeResponse  = null;
          for (PrestamoVigenteComposite prestamoVigente : request.getPayload().getPrestamosVigentesComposite()) {
              
              
                if(prestamoVigente.getPrestamoMclpeResponse().getIdOferta()  != null){
                    log.info("   enviando a obtener la condicion oferta:" + prestamoVigente.getPrestamoMclpeResponse().getIdOferta() );

                    condicionOfertaMclpeRequest = new CondicionOfertaMclpeRequest();
                    condicionOfertaMclpeRequest.setClave(prestamoVigente.getPrestamoMclpeResponse().getIdOferta().toString() );

                    Response load = condicionOfertaClient.load(condicionOfertaMclpeRequest);
                    
                    if( load.getStatus() == 200 ){
                        condicionOfertaMclpeResponse
                            = load.readEntity(Oferta.class);
                        prestamoVigente.setCondicionOfertaMclpeResponse(condicionOfertaMclpeResponse);
      
                    }
                    
                    
                }
            }
          
          return request;
  }
  
}
