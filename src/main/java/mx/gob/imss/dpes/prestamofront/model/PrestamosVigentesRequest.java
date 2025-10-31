/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
/**
 *
 * @author antonio
 */
public class PrestamosVigentesRequest extends BaseModel{
  @Getter @Setter private Pensionado pensionado;
}
