/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.assembler;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.prestamofront.model.PrestamoVigenteComposite;

/**
 *
 * @author Diego Velazquez
 */
@Provider
public class SolicitudAssembler extends BaseAssembler<Solicitud, PrestamoVigenteComposite> {
 
  @Override
  public PrestamoVigenteComposite assemble(Solicitud source) {
      
      PrestamoVigenteComposite prestamoVigenteComposite = new PrestamoVigenteComposite();
      prestamoVigenteComposite.setSolicitudVigenteMclpeResponse(source);
        
                
    return prestamoVigenteComposite; 
    }

}
