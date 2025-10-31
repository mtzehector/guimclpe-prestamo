/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

/**
 *
 * @author edgar.arenas
 */
public class ReporteExcepcion extends BusinessException{
    public final static String KEY = "msg027";
   
    public ReporteExcepcion(){
        super(KEY);
    }
    public ReporteExcepcion(String messageKey) {
        super(messageKey);
    }
}
