/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Correo;

/**
 *
 * @author antonio
 */
public class NotificacionCorreo<T extends BaseModel> extends BaseModel{
  @Getter @Setter Correo correo = new Correo();  
  @Getter @Setter T tipo;
  @Getter @Setter Long idSolicitud;  
  @Getter @Setter Template template = new Template();
  @Getter @Setter boolean addXML = true;
}
