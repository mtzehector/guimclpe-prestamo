/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.model;

import java.util.List;
import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.personaef.model.PersonaEF;
import mx.gob.imss.dpes.interfaces.bitacora.model.CronTarea;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;

/**
 *
 * @author eduardo.loyo
 */
@Data
public class RequestPrestamo extends BaseModel{
    private Prestamo prestamo = new Prestamo();
    private Solicitud solicitud = new Solicitud();
    private BitacoraModel bitacora = new BitacoraModel();
    private PrestamoRecuperacion prestamoRecuperacion = new PrestamoRecuperacion();
    private PrestamoEnRecuperacionRs listPrestamoRecuperacion;
    private String montoLiquidar; 
    private PersonaModel persona = new PersonaModel();
    private Integer flatMonto;
    private Integer flatFecha;
    private PersonaEF personaEf = new PersonaEF();
    private Documento documento = new Documento();
    private RespuestaBoveda respuestaBoveda = new RespuestaBoveda();
    private Long flatDoc;
    private Documentacion documentacion;
    private CronTarea crontareaResponse = new CronTarea();
    private mx.gob.imss.dpes.interfaces.documento.model.Documento docPdfPensionadoCEP;
    private mx.gob.imss.dpes.interfaces.documento.model.Documento docXmlPensionadoCEP;
    private List<mx.gob.imss.dpes.interfaces.documento.model.Documento> cepEntidadFinancieraLista;
    private List<mx.gob.imss.dpes.interfaces.documento.model.Documento> cepEntidadFinancieraListaXML;
    
}
