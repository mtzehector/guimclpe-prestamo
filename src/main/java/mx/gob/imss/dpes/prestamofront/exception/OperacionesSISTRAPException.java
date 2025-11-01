package mx.gob.imss.dpes.prestamofront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;


public class OperacionesSISTRAPException extends BusinessException{
    
    public final static String CLAVE_EF_ERRONEA = "msg032";
    public final static String ERROR_CONSULTA_ENTIDAD_FINANCIERA = "msg033";
    public final static String ERROR_OBTENCION_SOLICITUD = "msg034";
    public final static String ERROR_ACTUALIZACION_SOLICITUD = "msg035";
    public final static String DATO_NULO = "msg036";
    public final static String ERROR = "msg037";
    public final static String ERROR_DESCONOCIDO = "msg028";
    public final static String ERROR_OPERACIONES_SISTRAP = "msg038";
    public final static String ERROR_BITACORA = "msg039";
    public final static String ERROR_CONSULTA_PRESTAMO_SOLICITUD = "msg040";
    public final static String ERROR_ACTUALIZACION_SOLICITUD_ADMINISTRADOR = "msg041";
    public final static String ERROR_DESCONOCIDO_ADMINISTRADOR = "msg042";
   
    public OperacionesSISTRAPException(String messageKey) {
        super(messageKey);
    }
    
}
