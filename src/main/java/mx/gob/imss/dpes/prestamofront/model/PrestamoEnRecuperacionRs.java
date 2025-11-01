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
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;

/**
 *
 * @author juan.garfias
 */
public class PrestamoEnRecuperacionRs extends BaseModel {
    @Getter
    @Setter
    private List<PrestamoRecuperacion> prestamosEnRecuperacion;

}
