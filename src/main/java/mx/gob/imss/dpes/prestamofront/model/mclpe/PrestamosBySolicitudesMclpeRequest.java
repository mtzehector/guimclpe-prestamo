/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.model.mclpe;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.prestamofront.model.PrestamoVigenteComposite;

/**
 *
 * @author antonio
 */
public class PrestamosBySolicitudesMclpeRequest extends BaseModel{

    public PrestamosBySolicitudesMclpeRequest(List<PrestamoVigenteComposite> prestamosCompuestos) {
        solicitudes = new ArrayList<>();
        for (PrestamoVigenteComposite prestamoCompuesto : prestamosCompuestos) {
            solicitudes.add(prestamoCompuesto.getSolicitudVigenteMclpeResponse().getId());
        }
        
    }
    
    
  @Getter @Setter private List<Long> solicitudes;
  
}
