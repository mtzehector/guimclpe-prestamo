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
/**
 *
 * @author antonio
 */
public class PrestamoSistrapRequest extends BaseModel{
  @Getter @Setter private String idNss;
  @Getter @Setter private String grupoFamiliar;
  @Getter @Setter private String idPeriodo;
  @Getter @Setter private String numFolio;
}
