/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoRequest;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoResponse;

/**
 *
 * @author eduardo.loyo
 */
@Data
public class RequestSistrap extends BaseModel{
    private RegistroPrestamoRequest sistra = new RegistroPrestamoRequest();
    private RegistroPrestamoResponse salida = new RegistroPrestamoResponse();
    private Prestamo prestamo  = new Prestamo();
    private Solicitud solicitud = new Solicitud();
    private Pensionado pensionado = new Pensionado();
    private String registros;
}
