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
public class Documentacion extends BaseModel {
    
   private Long idContrato ;
   private Long idCartaInstruccion ;
   private Long idAmortizacion ;
   private Long idIdentificacionOficial ;
   private String refBovedaContrato ;
   private String refBovedaCarta ;
   private String refBovedaAmortizacion ; 
   private String refIdentificacionOficial ; 
}
