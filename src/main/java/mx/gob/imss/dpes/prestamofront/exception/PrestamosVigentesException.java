/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;


/**
 *
 * @author antonio
 */
public class PrestamosVigentesException extends BusinessException{
  private final static String KEY = "msg058";
  
  public PrestamosVigentesException() {
    super(KEY);
  }
  
}
