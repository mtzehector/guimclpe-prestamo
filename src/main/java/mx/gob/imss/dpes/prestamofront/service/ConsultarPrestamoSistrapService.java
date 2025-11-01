/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.util.List;
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
import mx.gob.imss.dpes.prestamofront.model.sistrap.PrestamoSistrap;
import mx.gob.imss.dpes.prestamofront.model.sistrap.PrestamoSistrapRequest;
import mx.gob.imss.dpes.prestamofront.restclient.PrestamoSistrapClient;


/**
 *
 * @author antonio
 */
@Provider
public class ConsultarPrestamoSistrapService extends ServiceDefinition<PrestamosVigentesComposite, 
        PrestamosVigentesComposite>{

  
  @Inject
  @RestClient
  private PrestamoSistrapClient prestamoSistrapClient;
  
  @Override
  public Message<PrestamosVigentesComposite> execute(Message<PrestamosVigentesComposite> request) {
    
    log.log(Level.INFO, ">>>ConsultarPrestamoSistrapService Iniciando el servicio de consultas de prestamos a SISTRAP");
    
    PrestamoSistrapRequest prestamoSistrapRequest = null;
    List<PrestamoVigenteComposite> prestamosVigentesComposite 
            = request.getPayload().getPrestamosVigentesComposite();
    
    if(prestamosVigentesComposite != null && !prestamosVigentesComposite.isEmpty()){
        log.log(Level.INFO, ">>>ConsultarPrestamoSistrapService Se van a consultar  {0} prestamos:", 
                prestamosVigentesComposite.size());
    
        for (PrestamoVigenteComposite prestamoVigenteComposite : prestamosVigentesComposite) {
            prestamoSistrapRequest = new PrestamoSistrapRequest();
            prestamoSistrapRequest.setNumFolio(prestamoVigenteComposite.getSolicitudVigenteMclpeResponse().getNumFolioSolicitud());
            log.log(Level.INFO, ">>>ConsultarPrestamoSistrapService Se van a consultar primer descuento {0} :", 
                prestamoVigenteComposite.getPrestamoMclpeResponse().getPrimerDescuento());
            prestamoSistrapRequest.setIdPeriodo(
                    Integer.toString(prestamoVigenteComposite.getPeriodoNominaMclpeResponse().getNumPeriodoNomina()));
            prestamoSistrapRequest.setIdNss(request.getPayload().getPensionado().getNss());
            prestamoSistrapRequest.setGrupoFamiliar(request.getPayload().getPensionado().getGrupoFamiliar());

            log.log(Level.INFO, ">>>ConsultarPrestamoSistrapService Invocando el servicio de pestamos sistrap: {0}", prestamoSistrapRequest);

            Response load = prestamoSistrapClient.load(prestamoSistrapRequest);
            if( load.getStatus() == 200 ){
              PrestamoSistrap prestamoSistrap = load.readEntity(PrestamoSistrap.class);
              prestamoVigenteComposite.setPrestamoSistrap(prestamoSistrap);

            }else{
                 //TODO: Cambiar por excepcion de infraestructura   
                return response(null, ServiceStatusEnum.EXCEPCION, new PrestamosVigentesException(), null );
            }

        }
    }
    
   return request; 
   
  }    
  
}
