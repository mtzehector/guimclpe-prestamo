package mx.gob.imss.dpes.prestamofront.service;

import java.util.List;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.common.model.Message;

import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.prestamofront.assembler.SolicitudAssembler;
import mx.gob.imss.dpes.prestamofront.exception.PrestamosVigentesException;
import mx.gob.imss.dpes.prestamofront.model.PrestamosVigentesComposite;
import mx.gob.imss.dpes.prestamofront.model.mclpe.SolicitudesVigentesMclpeRequest;
import mx.gob.imss.dpes.prestamofront.restclient.SolicitudesVigentesClient;



@Provider
public class ConsultarSolicitudesVigentesService extends ServiceDefinition<PrestamosVigentesComposite, 
        PrestamosVigentesComposite>{

  
  @Inject
  @RestClient
  private SolicitudesVigentesClient solicitudesVigentesClient;
  
  @Inject
  private SolicitudAssembler solicitudAssembler;  
  
  public Message<PrestamosVigentesComposite> execute(Message<PrestamosVigentesComposite> request) {
    log.log(Level.INFO, "Iniciando las consultas de solicitudes pendientes");
      SolicitudesVigentesMclpeRequest solicitudesVigentesMclpeRequest
              = new SolicitudesVigentesMclpeRequest();
      solicitudesVigentesMclpeRequest.setNss(request.getPayload().getPensionado().getNss());
      solicitudesVigentesMclpeRequest.setGrupoFamiliar(request.getPayload().getPensionado().getGrupoFamiliar());
      Long[] estados = { TipoEstadoSolicitudEnum.AUTORIZADO.getTipo()};
      solicitudesVigentesMclpeRequest.setEstados(estados);
    
    log.log(Level.INFO, "Se llama al servicio de solicitudes back");
    Response load = solicitudesVigentesClient.load(solicitudesVigentesMclpeRequest);
    if( load.getStatus() == 200 ){
      log.log(Level.INFO, "Se recibió respuesta exitosa de las solicitudes");
      //Obtenemos la lista de prestamos vigentes de la propia BD
      List<Solicitud> solicitudesVigentes
              = load.readEntity(new GenericType<List<Solicitud>>() {});
      
      request.getPayload().setPrestamosVigentesComposite(
              this.solicitudAssembler.assembleList(solicitudesVigentes));
       return request; 
    }
    
    return response(null, ServiceStatusEnum.EXCEPCION, new PrestamosVigentesException(), null );
  }
  
}
