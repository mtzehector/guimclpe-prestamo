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
public class PrestamoException extends BusinessException{
    public final static String KEY = "ms0088";
    public final static String DOCTOS_UPDATE_ERROR = "msg026";
    
    public PrestamoException(){
        super(KEY);
    }
    public PrestamoException(String messageKey) {
        super(messageKey);
    }
    
}
