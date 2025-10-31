/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author edgar.arenas
 */
@Data
public class BovedaRequest extends BaseModel {
    
   Documento documento = new Documento();
   Tramite tramite = new Tramite();
   Usuario usuario = new Usuario();
   private Long sesion;
}
