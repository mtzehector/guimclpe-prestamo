/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.rules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.rule.BaseRule;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;
import mx.gob.imss.dpes.support.config.CustomDateSerializer;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class FechaLimite extends BaseRule<RequestPrestamo, RequestPrestamo>{
    
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    Date fechaActual = new Date();
    Integer validarFecha;
    final Integer DIAS = 2;
    Date altaRegistro;
    
    @Override
    public RequestPrestamo apply(RequestPrestamo input) {
        log.log(Level.INFO,">>>>Fecha de registro : {0}", input.getPrestamo().getAltaRegistro());
        altaRegistro = input.getPrestamo().getAltaRegistro();
        
        Calendar c = Calendar.getInstance();
        c.setTime(altaRegistro);
        c.add(Calendar.DAY_OF_MONTH, DIAS);
        Date fechaLimite = c.getTime();
        
        validarFecha = fechaActual.compareTo(fechaLimite);
        switch(validarFecha){
            case 0:
            case -1:
                input.setFlatFecha(1);
                break;
            case 1:
                input.setFlatFecha(0);
                break;
            default:
                break;
        }
        log.log(Level.INFO,">>>>FechaLimite: {0}", input.getFlatMonto());
        return input;
    }
}
