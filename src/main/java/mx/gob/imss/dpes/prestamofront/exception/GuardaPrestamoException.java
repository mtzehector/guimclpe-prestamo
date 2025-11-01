/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

/**
 *
 * @author eduardo.loyo
 */
public class GuardaPrestamoException extends BusinessException{
    public final static String KEY = "msg025";
    public GuardaPrestamoException(){
        super(KEY);
    }
    public GuardaPrestamoException(String messageKey) {
        super(messageKey);
    }
}
