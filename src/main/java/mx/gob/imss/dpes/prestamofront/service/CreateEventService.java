/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import mx.gob.imss.dpes.common.enums.EventEnum;
import mx.gob.imss.dpes.interfaces.evento.model.Evento;
import mx.gob.imss.dpes.prestamofront.restclient.EventoClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.interfaces.evento.service.BaseCreateEventService;
import mx.gob.imss.dpes.interfaces.evento.service.BaseEventoClient;

/**
 *
 * @author antonio
 */
@Provider
public class CreateEventService extends BaseCreateEventService {

  @Inject
  @RestClient
  private EventoClient client;

  @Override
  public BaseEventoClient getClient() {
    return client;
  }

  @Override
  public void initEvent(Evento evento) {        
    evento.setEvent(EventEnum.CREAR_CORREO_AUTORIZACION_CARTA_INSTRUCCION);    
  }
  
}
