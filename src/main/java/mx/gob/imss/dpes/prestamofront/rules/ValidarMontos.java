/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.rules;

import java.util.logging.Level;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.rule.BaseRule;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ValidarMontos extends BaseRule<RequestPrestamo, RequestPrestamo>{
    
    @Override
    public RequestPrestamo apply(RequestPrestamo input) {
        
        if(Double.parseDouble(input.getMontoLiquidar()) < input.getPrestamo().getMonto()){
            input.setFlatMonto(0);
        }else{
            input.setFlatMonto(1);
        }
        log.log(Level.INFO, "ValidarMontos Rule {0}", input.getFlatMonto());
        return input;
    }
    
}
