/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.interfaces.reportes.model.Conciliacion;
import mx.gob.imss.dpes.interfaces.reportes.model.Inconsistencia;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.interfaces.reportes.model.ReporteDescuentosEmitidos;
import mx.gob.imss.dpes.interfaces.reportes.model.ReporteConciliacion;
import mx.gob.imss.dpes.interfaces.reportes.model.ReporteDescuentoEmitidoRow;
import mx.gob.imss.dpes.interfaces.sipre.model.DescuentoEmitido;
import mx.gob.imss.dpes.prestamofront.exception.ReporteExcepcion;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ResumenConciliacionService extends ServiceDefinition<Reporte, Reporte> {

    @Inject
    private EfByCveSipreService efByCveSipreService;

    static final String CONCEPTO_NOMINA_301 = "301";
    static final String CONCEPTO_NOMINA_380 = "380";
    static final String MOVIMIENTO_DA = "DA";//Descuento aplicado
    static final String MOVIMIENTO_NA = "NA";//Descuento no aplicado
    static final String MOVIMIENTO_FA = "FA";//Notificacion de fallecidos
    static final Double COSTO_ADMINISTRATIVO_I = 0.005;
    static final Double COSTO_ADMINISTRATIVO_IVA = 0.16;

    ReporteConciliacion reporteConciliacion;
    private List<Conciliacion> conciliacionList;
    private List<Inconsistencia> inconsistenciaList;

    @Override
    public Message<Reporte> execute(Message<Reporte> request) throws BusinessException {
        
        switch (request.getPayload().getTipoReporte().intValue()) {
            case 1:
                //respuesta = client.load(descuentos);
                return consiliacionIMSS(request);
                //break;
            case 2:
                // TODO: 
                break;          
            case 3:
                return descuentosEmitidos(request);
                //break;
            default:
                return response(null, ServiceStatusEnum.EXCEPCION, new ReporteExcepcion("Tipo de reporte inválido"), null);
        }
                        
        return response(null, ServiceStatusEnum.EXCEPCION, new ReporteExcepcion("Tipo de reporte inválido"), null);

    }

    public Message<Reporte> consiliacionIMSS(Message<Reporte> request) throws BusinessException {
    //log.log(Level.INFO, ">>>prestamoFront|ResumenConciliacionService|execute {0}", request.getPayload());
        if (request.getPayload().getDescuentosResponse().getDescuentosEmitidos() != null && request.getPayload().getDescuentosResponse().getDescuentosEmitidos().size() > 0) {
            reporteConciliacion = new ReporteConciliacion();
            conciliacionList = new ArrayList<>();
            inconsistenciaList = new ArrayList<>();

            for (DescuentoEmitido descuento : request.getPayload().getDescuentosResponse().getDescuentosEmitidos()) {
                if (descuento.getConceptoNomina().equals(CONCEPTO_NOMINA_301) || descuento.getConceptoNomina().equals(CONCEPTO_NOMINA_380)) {
                    calcularDescuentosEmitidos(descuento);
                    switch (descuento.getMovimiento()) {
                        case MOVIMIENTO_DA:
                            calcularDescuentosAplicados(descuento);
                            break;
                        case MOVIMIENTO_NA:
                            calcularDescuentosNoAplicados(descuento);
                            break;
                        case MOVIMIENTO_FA:
                            calcularNotificacionFallecidos(descuento);
                            break;
                        default:
                            calcularInconsistencias(descuento);
                            break;
                    }
                }
            }
            calcularTotalFila();
            calcularTotalColumna();
            reporteConciliacion.setLstConciliaciones(conciliacionList);
            reporteConciliacion.setInconsistenciasList(inconsistenciaList);
            //log.log(Level.INFO, ">>>prestamoFront|ResumenConciliacionService|list {0}", reporteConciliacion);
            request.getPayload().setReporteConciliacion(reporteConciliacion);
            //log.log(Level.INFO, ">>>prestamoFront|ResumenConciliacionService|respone {0}", request.getPayload());
            return request;
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new ReporteExcepcion(), null);
    }
    
       
    public Message<Reporte> descuentosEmitidos(Message<Reporte> request) throws BusinessException {
                        
        log.log(Level.INFO, "Inicia DESCUENTOS EMITIDOS");

        Map<String,DescuentoEmitido> prestamosMap = new HashMap<>();
        Map<String,DescuentoEmitido> descEmitMap = new HashMap<>();
        
        //Map<String,DescuentoEmitido> descEmitByEFMap = new HashMap<>();
        //Map<String,DescuentoEmitido> descEmitByDelMap = new HashMap<>();
        
        Map<String,DescuentoEmitido> prestIncsttsMap = new HashMap<>();
        Map<String,DescuentoEmitido> descuIncsttsMap = new HashMap<>();
        
        //Map<String,DescuentoEmitido> prestIncsttsByEFMap = new HashMap<>();
        //Map<String,DescuentoEmitido> descuIncsttsByDelMap = new HashMap<>();
        
        Map<String,String> delegaciones = new HashMap<>();
        Map<String,String> entidadesFinancieras = new HashMap<>();
        
        Map<String, ReporteDescuentoEmitidoRow> reporteByDel = new HashMap<>();
        Map<String, ReporteDescuentoEmitidoRow> reporteByEF = new HashMap<>();
        
        for(DescuentoEmitido prestamo : request.getPayload().getDescuentosResponse().getPrestamosEnCursoVoList()){
            
            if (prestamo.getImpRecuperado()==null) {
                prestamo.setImpRecuperado(0.0);
            }
            
            if (prestamo.getDelegacion()==null) {
                prestamo.setDelegacion("NULL");
            }
            
            if (prestamo.getDelegacion() != null && prestamo.getIdInstFincanciera()!=null) {
                delegaciones.put(prestamo.getDelegacion(),prestamo.getDelegacion());
                entidadesFinancieras.put(prestamo.getIdInstFincanciera(),prestamo.getIdInstFincanciera());
            }
            
            if (prestamo.getMovimiento().equals("RP") || prestamo.getMovimiento().equals("PO")) {
                prestamosMap.put(prestamo.getIdSolPrestFinanciero(), prestamo);
                
                // LLENA PARTE DEL REPORTE POR DELEGACION
                if (!reporteByDel.containsKey(prestamo.getDelegacion())) {
                    reporteByDel.put(prestamo.getDelegacion(), new ReporteDescuentoEmitidoRow(
                            0,0.0,
                            1,prestamo.getImpRecuperado(),
                            0,0.0
                    )
                    );
                } else {
                    reporteByDel.get(prestamo.getDelegacion()).setReposEmit_casos(
                            reporteByDel.get(prestamo.getDelegacion()).getReposEmit_casos()+1
                    );
                    reporteByDel.get(prestamo.getDelegacion()).setReposEmit_importe(
                            reporteByDel.get(prestamo.getDelegacion()).getReposEmit_importe()+prestamo.getImpRecuperado()
                    );
                }
                
                // LLENA PARTE DEL REPORTE POR EF
                          
                if (!reporteByEF.containsKey(prestamo.getIdInstFincanciera())) {
                    reporteByEF.put(prestamo.getIdInstFincanciera(), new ReporteDescuentoEmitidoRow(0,0.0 ,
                            1,prestamo.getImpRecuperado(),
                            0,0.0));
                } else {
                    reporteByEF.get(prestamo.getIdInstFincanciera()).setReposEmit_casos(
                            reporteByEF.get(prestamo.getIdInstFincanciera()).getReposEmit_casos()+1
                    );
                    reporteByEF.get(prestamo.getIdInstFincanciera()).setReposEmit_importe(
                            reporteByEF.get(prestamo.getIdInstFincanciera()).getReposEmit_importe()+prestamo.getImpRecuperado()
                    );
                }
                
            } else {
                prestIncsttsMap.put(prestamo.getIdSolPrestFinanciero(), prestamo);
                
                // LLENA PARTE DEL REPORTE POR DELEGACION INCONSISTENCIAS
                if (!reporteByDel.containsKey(prestamo.getDelegacion())) {
                    reporteByDel.put(prestamo.getDelegacion(), new ReporteDescuentoEmitidoRow(
                            0,0.0,
                            0,0.0,
                            1,prestamo.getImpRecuperado())
                    );
                } else {
                    reporteByDel.get(prestamo.getDelegacion()).setIncons_casos(
                            reporteByDel.get(prestamo.getDelegacion()).getIncons_casos()+1
                    );
                    reporteByDel.get(prestamo.getDelegacion()).setIncons_importe(
                            reporteByDel.get(prestamo.getDelegacion()).getIncons_importe()+prestamo.getImpRecuperado()
                    );
                }

                // LLENA PARTE DEL REPORTE POR EF INCONSISTENCIAS         
                if (!reporteByEF.containsKey(prestamo.getIdInstFincanciera())) {
                    reporteByEF.put(prestamo.getIdInstFincanciera(), new ReporteDescuentoEmitidoRow(
                            0,0.0,
                            0,0.0,
                            1,prestamo.getImpRecuperado()
                    ));
                } else {
                    reporteByEF.get(prestamo.getIdInstFincanciera()).setIncons_casos(
                            reporteByEF.get(prestamo.getIdInstFincanciera()).getIncons_casos()+1
                    );
                    reporteByEF.get(prestamo.getIdInstFincanciera()).setIncons_importe(
                            reporteByEF.get(prestamo.getIdInstFincanciera()).getIncons_importe()+prestamo.getImpRecuperado()
                    );
                }
            }
        }
                
        for(DescuentoEmitido descuento : request.getPayload().getDescuentosResponse().getDescuentosVoList()){
            
             
            if (descuento.getImpRecuperado()==null) {
                descuento.setImpRecuperado(0.0);
            }
            
            if (descuento.getDelegacion()==null) {
                descuento.setDelegacion("NULL");
            }
            
            if (descuento.getDelegacion() != null && descuento.getIdInstFincanciera()!=null) {
                delegaciones.put(descuento.getDelegacion(),descuento.getDelegacion());
                entidadesFinancieras.put(descuento.getIdInstFincanciera(),descuento.getIdInstFincanciera());
            }
            if (descuento.getMovimiento().equals("RP") || descuento.getMovimiento().equals("PO")) {
                if (prestamosMap.containsKey(descuento.getIdSolPrestFinanciero())) {
                    
                    if(
                            prestamosMap.get(
                                    descuento.getIdSolPrestFinanciero()
                            ).getIdNss().equals(
                                    descuento.getIdNss()
                            ) &&
                            prestamosMap.get(
                                    descuento.getIdSolPrestFinanciero()
                            ).getIdInstFincanciera().equals(
                                    descuento.getIdInstFincanciera()
                            )
                            &&
                            prestamosMap.get(
                                    descuento.getIdSolPrestFinanciero()
                            ).getIdGrupoFamiliar().equals(
                                    descuento.getIdGrupoFamiliar()
                            )
                            ){
                        
                        descEmitMap.put(descuento.getIdSolPrestFinanciero(), descuento);
                        //descEmitByEFMap.put(descuento.getIdInstFincanciera(), descuento);
                        //descEmitByDelMap.put(descuento.getDelegacion(), descuento);
                        
                       // LLENA PARTE DEL REPORTE POR DELEGACION
                       if (!reporteByDel.containsKey(descuento.getDelegacion())) {
                           reporteByDel.put(descuento.getDelegacion(), new ReporteDescuentoEmitidoRow(
                                   1,descuento.getImpRecuperado(),
                                   0,0.0 ,
                                   0,0.0));
                       } else {
                           reporteByDel.get(descuento.getDelegacion()).setDescEmit_casos(
                                   reporteByDel.get(descuento.getDelegacion()).getDescEmit_casos()+1
                           );
                           reporteByDel.get(descuento.getDelegacion()).setDescEmit_importe(
                                   reporteByDel.get(descuento.getDelegacion()).getDescEmit_importe()+descuento.getImpRecuperado()
                           );
                       }

                       // LLENA PARTE DEL REPORTE POR EF

                       if (!reporteByEF.containsKey(descuento.getIdInstFincanciera())) {
                           reporteByEF.put(descuento.getIdInstFincanciera(), new ReporteDescuentoEmitidoRow(
                                   1,descuento.getImpRecuperado(),
                                   0,0.0,
                                   0,0.0));
                       } else {
                           reporteByEF.get(descuento.getIdInstFincanciera()).setDescEmit_casos(
                                   reporteByEF.get(descuento.getIdInstFincanciera()).getDescEmit_casos()+1
                           );
                           reporteByEF.get(descuento.getIdInstFincanciera()).setDescEmit_importe(
                                   reporteByEF.get(descuento.getIdInstFincanciera()).getDescEmit_importe()+descuento.getImpRecuperado()
                           );
                       }
                        
                    } else {
                        descuIncsttsMap.put(descuento.getIdSolPrestFinanciero(), descuento);
                    }
                } else {           
                    descuIncsttsMap.put(descuento.getIdSolPrestFinanciero(), descuento);
                }
            } else {
                descuIncsttsMap.put(descuento.getIdSolPrestFinanciero(), descuento);
            }
        }
        
        log.log(Level.INFO, ">>>getDescuentosVoList {0}",request.getPayload().getDescuentosResponse().getDescuentosVoList().size());
        log.log(Level.INFO, ">>>getPrestamosEnCursoVoList {0}",request.getPayload().getDescuentosResponse().getPrestamosEnCursoVoList().size());
        
        log.log(Level.INFO, ">>>prestamosMap {0}",prestamosMap.size());
        log.log(Level.INFO, ">>>descEmitMap {0}",descEmitMap.size());
        
        //log.log(Level.INFO, ">>>descEmitByEFMap {0}",descEmitByEFMap.size());
        //log.log(Level.INFO, ">>>descEmitByDelMap {0}",descEmitByDelMap.size());
        
        log.log(Level.INFO, ">>>descuIncsttsMap {0}",descuIncsttsMap.size());
        log.log(Level.INFO, ">>>prestIncsttsMap {0}",prestIncsttsMap.size());
        

        

        ReporteDescuentosEmitidos rde = new ReporteDescuentosEmitidos();
        
        //rde.setPrestamosMap(prestamosMap);
        //rde.setDescEmitMap(descEmitMap);
        
        //rde.setDescEmitByDelMap(descEmitByDelMap);
        //rde.setDescEmitByEFMap(descEmitByEFMap);
        
        rde.setDescuIncsttsMap(descuIncsttsMap);
        rde.setPrestIncsttsMap(prestIncsttsMap);
        rde.setDelegaciones(delegaciones);
        rde.setEntidadesFinancieras(entidadesFinancieras);
        rde.setReporteByDel(reporteByDel);
        rde.setReporteByEF(reporteByEF);
        
        
        request.getPayload().setRepDesctsEmitds(rde);
                
        //log.log(Level.INFO, ">>>REQUEST prestamosMap {0}",request.getPayload().getRepDesctsEmitds().getPrestamosMap());
        //log.log(Level.INFO, ">>>REQUEST descEmitMap {0}",request.getPayload().getRepDesctsEmitds().getDescEmitMap());
        
//        log.log(Level.INFO, ">>>REQUEST descEmitByEFMap {0}",request.getPayload().getRepDesctsEmitds().getDescEmitByEFMap());
//        log.log(Level.INFO, ">>>REQUEST descEmitByDelMap {0}",request.getPayload().getRepDesctsEmitds().getDescEmitByDelMap());
        
        log.log(Level.INFO, ">>>REQUEST descuIncsttsMap {0}",request.getPayload().getRepDesctsEmitds().getDescuIncsttsMap().size());
        log.log(Level.INFO, ">>>REQUEST prestIncsttsMap {0}",request.getPayload().getRepDesctsEmitds().getPrestIncsttsMap().size());
        
        log.log(Level.INFO, ">>>REQUEST getDelegaciones {0}",request.getPayload().getRepDesctsEmitds().getDelegaciones());
        log.log(Level.INFO, ">>>REQUEST getEntidadesFinancieras {0}",request.getPayload().getRepDesctsEmitds().getEntidadesFinancieras());
        
//        log.log(Level.INFO, ">>>REQUEST prestamosMap {0}",request.getPayload().getRepDesctsEmitds().getPrestamosMap().size());
//        log.log(Level.INFO, ">>>REQUEST descEmitMap {0}",request.getPayload().getRepDesctsEmitds().getDescEmitMap().size());
        
//        log.log(Level.INFO, ">>>REQUEST descEmitByEFMap {0}",request.getPayload().getRepDesctsEmitds().getDescEmitByEFMap().size());
//        log.log(Level.INFO, ">>>REQUEST descEmitByDelMap {0}",request.getPayload().getRepDesctsEmitds().getDescEmitByDelMap().size());
        
        log.log(Level.INFO, ">>>REQUEST descuIncsttsMap {0}",request.getPayload().getRepDesctsEmitds().getDescuIncsttsMap().size());
        log.log(Level.INFO, ">>>REQUEST prestIncsttsMap {0}",request.getPayload().getRepDesctsEmitds().getPrestIncsttsMap().size());
        
        log.log(Level.INFO, ">>>REQUEST reporteByDel {0}",request.getPayload().getRepDesctsEmitds().getReporteByDel());
        log.log(Level.INFO, ">>>REQUEST reporteByEF {0}",request.getPayload().getRepDesctsEmitds().getReporteByEF());
        
        log.log(Level.INFO, "Termina DESCUENTOS EMITIDOS");
        return request;
    }
    
    private void calcularDescuentosEmitidos(DescuentoEmitido in) throws BusinessException {

        Boolean flagExiste = false;
        if (conciliacionList != null && conciliacionList.size() > 0) {
            for (Conciliacion c : conciliacionList) {
                if (c.getId_EF_SIPRE().equals(in.getIdInstFincanciera())) {
                    c.setDescYRepoEmit_casos(c.getDescYRepoEmit_casos() + 1);
                    c.setDescYRepoEmit_importe(c.getDescYRepoEmit_importe() + in.getImpRecuperado());
                    flagExiste = true;
                    break;
                } else {
                    flagExiste = false;
                }
            }
        } else {
            addEntidadFinanciera(in);
            flagExiste = true;
        }

        if (!flagExiste) {
            addEntidadFinanciera(in);
        }
    }

    private void calcularDescuentosAplicados(DescuentoEmitido in) throws BusinessException {

        for (Conciliacion c : conciliacionList) {
            if (c.getId_EF_SIPRE().equals(in.getIdInstFincanciera())) {
                c.setDescYRepoPagados_casos(c.getDescYRepoPagados_casos() + 1);
                c.setDescYRepoPagados_importe(c.getDescYRepoPagados_importe() + in.getImpRecuperado());
                break;
            }
        }
    }

    private void calcularDescuentosNoAplicados(DescuentoEmitido in) throws BusinessException {

        for (Conciliacion c : conciliacionList) {
            if (c.getId_EF_SIPRE().equals(in.getIdInstFincanciera())) {
                c.setNoPagado_casos(c.getNoPagado_casos() + 1);
                c.setNoPagado_importe(c.getNoPagado_importe() + in.getImpRecuperado());
                break;
            }
        }
    }

    private void calcularNotificacionFallecidos(DescuentoEmitido in) throws BusinessException {

        for (Conciliacion c : conciliacionList) {
            if (c.getId_EF_SIPRE().equals(in.getIdInstFincanciera())) {
                c.setFallecidos_casos(c.getFallecidos_casos() + 1);
                c.setFallecidos_importe(c.getFallecidos_importe() + in.getImpRecuperado());
                break;
            }
        }
    }
    
    private void calcularInconsistencias(DescuentoEmitido in)throws BusinessException {
        
        for (Conciliacion c : conciliacionList) {
            if (c.getId_EF_SIPRE().equals(in.getIdInstFincanciera())) {
                c.setInconsistencias_casos(c.getInconsistencias_casos() + 1);
                c.setInconsistencias_importe(c.getInconsistencias_importe() + in.getImpRecuperado());
                //Set para llenar el reporte de inconsistencias
                Inconsistencia inconsistencia = new Inconsistencia();
                inconsistencia.setFolioPrestamo(in.getIdSolPrestFinanciero());
                inconsistencia.setCveEntidadFinanciera(c.getId_EF());
                inconsistencia.setGrupoFamiliar(in.getIdGrupoFamiliar());
                inconsistencia.setIdMovimiento(in.getMovimiento());
                inconsistencia.setImporteRecuperado(in.getImpRecuperado());
                inconsistencia.setNss(in.getNss());
                inconsistenciaList.add(inconsistencia);
                break;
            }
        }
    }

    private void addEntidadFinanciera(DescuentoEmitido in) throws BusinessException {

        EntidadFinanciera e = new EntidadFinanciera();
        e.setIdSipre(in.getIdInstFincanciera());
        Message<EntidadFinanciera> es = efByCveSipreService.execute(new Message<>(e));

        Conciliacion conciliacion = new Conciliacion();
        conciliacion.setId_EF(es.getPayload().getId() != null ? es.getPayload().getId().toString() : "");
        conciliacion.setId_EF_SIPRE(in.getIdInstFincanciera());
        conciliacion.setRazonSocial_EF(es.getPayload().getRazonSocial());
        conciliacion.setNoProv_EF(es.getPayload().getNumProveedor());
        conciliacion.setProducto_EF(es.getPayload().getNombreComercial());
        conciliacion.setDescYRepoEmit_casos(1);
        conciliacion.setDescYRepoEmit_importe(in.getImpRecuperado());
        conciliacionList.add(conciliacion);
    }
    
    private void calcularTotalFila() throws BusinessException {

        for (Conciliacion c : conciliacionList) {
            c.setTotalRetenciones(c.getDescYRepoPagados_importe());
            Double costAdmin = c.getDescYRepoPagados_importe() * COSTO_ADMINISTRATIVO_I;
            c.setCostoAdmin_i(Math.round(costAdmin * 100.0) / 100.0);
            Double costAdminIVA = c.getCostoAdmin_i() * COSTO_ADMINISTRATIVO_IVA;
            c.setCostoAdmin_iva(Math.round(costAdminIVA * 100.0) / 100.0);
            Double impNeto = (c.getDescYRepoPagados_importe() - c.getCostoAdmin_i() - c.getCostoAdmin_iva());
            c.setNetoSinDescFallecidos_importe(Math.round(impNeto * 100.0) / 100.0);
            Double impReal = c.getNetoSinDescFallecidos_importe() - c.getFallecidos_importe();
            c.setPagoReal(Math.round(impReal * 100.0) / 100.0);
        }

    }

    private void calcularTotalColumna() throws BusinessException {

        Integer totalCasosEmit = 0;
        Integer totalCasosPag = 0;
        Integer totalCasosNoPag = 0;
        Integer totalCasosFall = 0;
        Integer totalCasosInc = 0;
        Double totalImporteEmit = 0d;
        Double totalImportePag = 0d;
        Double totalImporteNoPag = 0d;
        Double totalImporteRet = 0d;
        Double totalImporteCAI = 0d;
        Double totalImporteCAIVA = 0d;
        Double totalImportenNETO = 0d;
        Double totalImportenFall = 0d;
        Double totalImporteReal = 0d;
        Double totalImporteInc = 0d;

        for (Conciliacion c : conciliacionList) {
            totalCasosEmit += c.getDescYRepoEmit_casos();
            totalImporteEmit += c.getDescYRepoEmit_importe();
            totalCasosPag += c.getDescYRepoPagados_casos();
            totalImportePag += c.getDescYRepoPagados_importe();
            totalCasosNoPag += c.getNoPagado_casos();
            totalImporteNoPag += c.getNoPagado_importe();
            totalImporteRet += c.getTotalRetenciones();
            totalImporteCAI += c.getCostoAdmin_i();
            totalImporteCAIVA += c.getCostoAdmin_iva();
            totalImportenNETO += c.getNetoSinDescFallecidos_importe();
            totalCasosFall += c.getFallecidos_casos();
            totalImportenFall += c.getFallecidos_importe();
            totalImporteReal += c.getPagoReal();
            totalCasosInc += c.getInconsistencias_casos();
            totalImporteInc += c.getInconsistencias_importe();
        }
        reporteConciliacion.setTotal_descYRepoEmit_casos(totalCasosEmit);
        reporteConciliacion.setTotal_descYRepoEmit_importe(totalImporteEmit);
        reporteConciliacion.setTotal_descYRepoPagados_casos(totalCasosPag);
        reporteConciliacion.setTotal_descYRepoPagados_importe(totalImportePag);
        reporteConciliacion.setTotal_noPagado_casos(totalCasosNoPag);
        reporteConciliacion.setTotal_noPagado_importe(totalImporteNoPag);
        reporteConciliacion.setTotal_totalRetenciones(totalImporteRet);
        reporteConciliacion.setTotal_costoAdmin_i(totalImporteCAI);
        reporteConciliacion.setTotal_costoAdmin_iva(totalImporteCAIVA);
        reporteConciliacion.setTotal_netoSinDescFallecidos_importe(totalImportenNETO);
        reporteConciliacion.setTotal_fallecidos_casos(totalCasosFall);
        reporteConciliacion.setTotal_fallecidos_importe(totalImportenFall);
        reporteConciliacion.setTotal_pagoReal(totalImporteReal);
        reporteConciliacion.setTotal_inconsistencias_casos(totalCasosInc);
        reporteConciliacion.setTotal_inconsistencias_importe(totalImporteInc);
    }

}
