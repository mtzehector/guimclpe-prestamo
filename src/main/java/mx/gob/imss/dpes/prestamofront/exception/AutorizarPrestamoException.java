package mx.gob.imss.dpes.prestamofront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class AutorizarPrestamoException extends BusinessException {

    public final static String ERROR_DESCONOCIDO_EN_EL_SERVICIO = "msg028";
    public final static String MENSAJE_DE_SOLICITUD_INCORRECTO = "msg029";
    public final static String ERROR_AL_AUTORIZAR_REINSTALACION = "msg030";
    public final static String ERROR_AL_ACTUALIZAR_DOCUMENTOS_REINSTALACION = "msg031";

    public AutorizarPrestamoException(String messageKey) {
        super(messageKey);
    }
}
