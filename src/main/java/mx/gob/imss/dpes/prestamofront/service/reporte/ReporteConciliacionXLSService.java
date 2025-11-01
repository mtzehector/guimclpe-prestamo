/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service.reporte;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.reportes.model.Inconsistencia;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.interfaces.sipre.model.DescuentoEmitido;
import mx.gob.imss.dpes.prestamofront.exception.ReporteExcepcion;
import mx.gob.imss.dpes.prestamofront.service.reporte.util.GenerarExcelComprasDeCarteraEF;
import mx.gob.imss.dpes.prestamofront.service.reporte.util.GenerarExcelConsiliacion;
import mx.gob.imss.dpes.prestamofront.service.reporte.util.GenerarExcelDescuentosEmitidos;
import mx.gob.imss.dpes.prestamofront.service.reporte.util.GenerarExcelReportesEstadisticos;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author juan.garfias
 */
@Provider
public class ReporteConciliacionXLSService extends ServiceDefinition<Reporte, Reporte> {

    @Override
    public Message<Reporte> execute(Message<Reporte> response) throws BusinessException {

                
        log.log(Level.INFO, "ReporteConciliacionXLSService: 1 {0}", response.getPayload());

        switch (response.getPayload().getTipoReporte().intValue()) {
            case 1:
                 try {
                     GenerarExcelConsiliacion xls= new GenerarExcelConsiliacion();
                    //response.getPayload().setReporteConciliacion(testXLS());
                    response.getPayload().getReporteConciliacion().setArchivoXLSXBase64(xls.generaXLS(response.getPayload()));
                    response.getPayload().getReporteConciliacion().setArchivoTxtBase64(generarTxtInconsistencias(response.getPayload()));

                } catch (IOException ex) {
                    Logger.getLogger(ReporteConciliacionXLSService.class.getName()).log(Level.SEVERE, null, ex);
                }

                return response;
            case 2:
                // TODO: 
                break;          
            case 3:
                
                try {
                                    
                    GenerarExcelDescuentosEmitidos gede = new GenerarExcelDescuentosEmitidos();

                    response.getPayload().getRepDesctsEmitds().setArchivoXLSXBase64ByDel(
                        gede.generaXLSByDel(response.getPayload())
                    );
                    response.getPayload().getRepDesctsEmitds().setArchivoXLSXBase64ByEF(
                        gede.generaXLSByEF(response.getPayload())
                    );
                    
                    response.getPayload().getRepDesctsEmitds().setArchivoTxtInconsistenciasDescuentoB64(
                            generarTxtInconsistenciasDescuentos(response.getPayload())
                    );
                    
                    response.getPayload().getRepDesctsEmitds().setArchivoTxtInconsistenciasPrestamoB64(
                            generarTxtInconsistenciasPrestamo(response.getPayload())
                    );
                    
                } catch (IOException ex) {
                    Logger.getLogger(ReporteConciliacionXLSService.class.getName()).log(Level.SEVERE, null, ex);
                }
        
                return response;
            case 4:
                log.log(Level.INFO, "ReporteConciliacionXLSService Case 4" );

                try {
                                    
                    GenerarExcelComprasDeCarteraEF gecdcEF = new GenerarExcelComprasDeCarteraEF();

                    response.getPayload().getReporteCompraCarteraEF().setArchivoXLSX(
                        gecdcEF.generaXLS(response.getPayload())
                    );
                    
                } catch (IOException ex) {
                      
                    log.log(Level.INFO, "ReporteConciliacionXLSService IOException ex: {0}", ex);

                    //Logger.getLogger(ReporteConciliacionXLSService.class.getName()).log(Level.SEVERE, null, ex);
                }
                                
                return response;
            case 5:
                log.log(Level.INFO, "Reporte Prestamos Autorizados Case 5" );

                try {
                                    
                    GenerarExcelReportesEstadisticos reporteXLS = new GenerarExcelReportesEstadisticos();

                    response.getPayload().setArchivoXLSX(
                        reporteXLS.generaXLS(response.getPayload())
                    );
                    
                } catch (IOException ex) {
                      
                    log.log(Level.INFO, "Reporte Prestamos Autorizados ex: {0}", ex);

                    //Logger.getLogger(ReporteConciliacionXLSService.class.getName()).log(Level.SEVERE, null, ex);
                }
                                
                return response;
                
                case 6:
                log.log(Level.INFO, "Reporte Case 6" );

                try {
                                    
                    GenerarExcelReportesEstadisticos reporteXLS = new GenerarExcelReportesEstadisticos();

                    response.getPayload().setArchivoXLSX(
                        reporteXLS.generaXLS(response.getPayload())
                    );
                    
                } catch (IOException ex) {
                      
                    log.log(Level.INFO, "Reporte Prestamos Autorizados ex: {0}", ex);

                    //Logger.getLogger(ReporteConciliacionXLSService.class.getName()).log(Level.SEVERE, null, ex);
                }
                                
                return response;
                case 7:
                log.log(Level.INFO, "Reporte Case 7" );
                try {
                                    
                    GenerarExcelReportesEstadisticos reporteXLS = new GenerarExcelReportesEstadisticos();

                    response.getPayload().setArchivoXLSX(
                        reporteXLS.generaXLS(response.getPayload())
                    );
                    
                } catch (IOException ex) {
                      
                    log.log(Level.INFO, "Reporte Prestamos Autorizados ex: {0}", ex);

                    //Logger.getLogger(ReporteConciliacionXLSService.class.getName()).log(Level.SEVERE, null, ex);
                }
                                
                return response;
            default:
                return response(null, ServiceStatusEnum.EXCEPCION, new ReporteExcepcion("Tipo de reporte inválido"), null);
        }
                        
        return response(null, ServiceStatusEnum.EXCEPCION, new ReporteExcepcion("Tipo de reporte inválido"), null);

    }

    



    public String generarTxtInconsistencias(Reporte rc) {

        String reporteInconsistencias = "Folio del préstamo|Entidad Financiera|NSS|Grupo Familiar|Importe Recuperado|Id Movimiento|";
        if (rc.getReporteConciliacion().getInconsistenciasList() != null) {
            for (Inconsistencia i : rc.getReporteConciliacion().getInconsistenciasList()) {
                reporteInconsistencias += "\n" + i.getFolioPrestamo() + "|" + i.getCveEntidadFinanciera() + "" + i.getNss() + "|" + i.getGrupoFamiliar() + "|" + i.getImporteRecuperado() + "|" + i.getIdMovimiento() + "|";
            }
        }
        String stringTxtB64 = Base64.encodeBase64String(reporteInconsistencias.getBytes());

        return stringTxtB64;
    }
    
        
    public String generarTxtInconsistenciasDescuentos(Reporte rc) {

        String reporteInconsistencias = "Folio del préstamo|Entidad Financiera|NSS|Grupo Familiar|Importe Recuperado|Id Movimiento|";
        if (rc.getRepDesctsEmitds().getDescuIncsttsMap() != null) {
            for(Map.Entry<String, DescuentoEmitido> entry : rc.getRepDesctsEmitds().getDescuIncsttsMap().entrySet()){
               reporteInconsistencias += "\n" + entry.getValue().getIdSolPrestFinanciero()+ "|" + 
                       entry.getValue().getIdInstFincanciera()+ "|" + 
                       entry.getValue().getIdNss() + "|" + 
                       entry.getValue().getIdGrupoFamiliar() + "|" + 
                       entry.getValue().getImpRecuperado() + "|" + 
                       entry.getValue().getMovimiento() + "|";
            }
            
        }
        String stringTxtB64 = Base64.encodeBase64String(reporteInconsistencias.getBytes());

        return stringTxtB64;
    }

            
        
    public String generarTxtInconsistenciasPrestamo(Reporte rc) {

        String reporteInconsistencias = "Folio del préstamo|Entidad Financiera|NSS|Grupo Familiar|Importe Recuperado|Id Movimiento|";
        if (rc.getRepDesctsEmitds().getPrestIncsttsMap()!= null) {
            for(Map.Entry<String, DescuentoEmitido> entry : rc.getRepDesctsEmitds().getPrestIncsttsMap().entrySet()){
               reporteInconsistencias += "\n" + entry.getValue().getIdSolPrestFinanciero()+ "|" + 
                       entry.getValue().getIdInstFincanciera()+ "|" + 
                       entry.getValue().getIdNss() + "|" + 
                       entry.getValue().getIdGrupoFamiliar() + "|" + 
                       entry.getValue().getImpRecuperado() + "|" + 
                       entry.getValue().getMovimiento() + "|";
            }
            
        }
        String stringTxtB64 = Base64.encodeBase64String(reporteInconsistencias.getBytes());

        return stringTxtB64;
    }
}
