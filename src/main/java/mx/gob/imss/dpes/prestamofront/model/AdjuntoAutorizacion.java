/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author edgar.arenas
 */
public class AdjuntoAutorizacion extends BaseModel {

    @Getter
    @Setter
    String nombreAdjunto;
    @Getter
    @Setter
    String adjuntoBase64;
}
