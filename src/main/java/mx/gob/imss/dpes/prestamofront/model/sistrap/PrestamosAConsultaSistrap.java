/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.model.sistrap;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.prestamofront.model.Pensionado;

/**
 *
 * @author antonio
 */
public class PrestamosAConsultaSistrap extends BaseModel{
  @Getter @Setter private Pensionado pensionado;
  @Getter @Setter private String periodoNomina;
  @Getter @Setter private List<Solicitud>  solicitudesVigentes;
}
