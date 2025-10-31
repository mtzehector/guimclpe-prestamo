/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.reportes.model.Boveda;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.prestamofront.assembler.BovedaAssembler;
import mx.gob.imss.dpes.prestamofront.exception.ReporteExcepcion;
import mx.gob.imss.dpes.prestamofront.restclient.BovedaClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class BovedaReportesService extends ServiceDefinition<Reporte, Reporte> {

    @Inject
    @RestClient
    private BovedaClient client;

    @Inject
    private CrearDocumentoService documentoService;


    @Override
    public Message<Reporte> execute(Message<Reporte> request) throws BusinessException {

        switch (request.getPayload().getTipoReporte().intValue()) {
            case 1:
                        //log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute {0} ", request.getPayload());
                log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute");

                Integer cont = 1;
                while (cont <= 2) {
                    if (cont == 1) {
                        BovedaAssembler assembler = new BovedaAssembler();
                        Response conciliacion = client.create(assembler.assemble(request.getPayload()));
                        if (conciliacion.getStatus() == 200) {
                            Boveda boveda = conciliacion.readEntity(Boveda.class);
                            log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|docConciliacion boveda {0}", boveda);
                            request.getPayload().setBoveda(boveda);
                            request.getPayload().setFlagReporte(cont);
                            documentoService.execute(request);
                            //log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|docConciliacion {0} ", c.getPayload().getDocConciliacion());
                            log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|docConciliacion");
                        } else {
                            return response(null, ServiceStatusEnum.EXCEPCION, null, null);
                        }
                    } else {
                        BovedaAssembler assembler = new BovedaAssembler();
                        Response inconsistencias = client.create(assembler.assembleTxt(request.getPayload()));
                        if (inconsistencias.getStatus() == 200) {
                            Boveda boveda = inconsistencias.readEntity(Boveda.class);
                            log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|docConciliacion inconsistencias {0}", boveda);
                            request.getPayload().setBoveda(new Boveda());
                            request.getPayload().setBoveda(boveda);
                            request.getPayload().setFlagReporte(cont);
                            documentoService.execute(request);
                            //log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|docInconsistencias {0} ", i.getPayload().getDocInconsistencias());
                            log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|docInconsistencias");
                        } else {
                            return response(null, ServiceStatusEnum.EXCEPCION, null, null);
                        }
                    }
                    cont++;
                }

                return new Message<>(request.getPayload());

             
            case 2:
                // TODO: 
                break;          
            case 3:
                String periodoNomina2Chars=request.getPayload().getAnioNominal().substring(2, 4);
                String mesNominal=request.getPayload().getMesNominal();
                String tipoRepoChar=request.getPayload().getTipoReporte().toString();
                        //log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute {0} ", request.getPayload());
                log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute : 3");
                   
                // POR ENTIDAD FINANCIERA CLAVE 19
                log.log(Level.INFO, "POR ENTIDAD FINANCIERA CLAVE 19");
                BovedaAssembler assembler = new BovedaAssembler();
                Response response_1 = client.create(assembler.assembleXLSX( 
                        periodoNomina2Chars.concat(mesNominal)+tipoRepoChar,
                        request.getPayload().getRepDesctsEmitds().getArchivoXLSXBase64ByEF(),
                        19L,
                        request.getPayload().getSesion()
                ));
                if (response_1.getStatus() == 200) {
                    Boveda boveda_1 = response_1.readEntity(Boveda.class);
                    log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|getArchivoXLSXBase64ByEF boveda {0}", boveda_1);
                    request.getPayload().setBoveda(boveda_1);
                    request.getPayload().setFlagReporte(3);
                    documentoService.execute(request);
                    //log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|docConciliacion {0} ", c.getPayload().getDocConciliacion());
                    log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|getArchivoXLSXBase64ByEF");
                } else {
                    return response(null, ServiceStatusEnum.EXCEPCION, null, null);
                }
                
                

                // POR INCONSISTENCIAS DE PRESTAMOS 21
                log.log(Level.INFO, "POR INCONSISTENCIAS DE PRESTAMOS 21");
                BovedaAssembler assembler3 = new BovedaAssembler();
                Response response_3 = client.create(assembler3.assembleXLSX( 
                        periodoNomina2Chars.concat(mesNominal)+tipoRepoChar,
                        request.getPayload().getRepDesctsEmitds().getArchivoTxtInconsistenciasPrestamoB64(),
                        21L,
                        request.getPayload().getSesion()
                ));
                if (response_3.getStatus() == 200) {
                    Boveda boveda_3 = response_3.readEntity(Boveda.class);
                    log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|getArchivoTxtInconsistenciasPrestamoB64 boveda {0}", boveda_3);
                    request.getPayload().setBoveda(boveda_3);
                    request.getPayload().setFlagReporte(5);
                    documentoService.execute(request);
                    //log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|docConciliacion {0} ", c.getPayload().getDocConciliacion());
                    log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|getArchivoTxtInconsistenciasPrestamoB64");
                } else {
                    return response(null, ServiceStatusEnum.EXCEPCION, null, null);
                }                
                
               
                // POR INCONSISTENCIAS DE DESCUENTOS 22
                BovedaAssembler assembler4 = new BovedaAssembler();
                Response response_4 = client.create(assembler4.assembleXLSX( 
                        periodoNomina2Chars.concat(mesNominal)+tipoRepoChar,
                        request.getPayload().getRepDesctsEmitds().getArchivoTxtInconsistenciasDescuentoB64(),
                        22L,
                        request.getPayload().getSesion()
                ));
                if (response_4.getStatus() == 200) {
                    Boveda boveda_4 = response_4.readEntity(Boveda.class);
                    log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|getArchivoTxtInconsistenciasDescuentoB64 boveda {0}", boveda_4);
                    request.getPayload().setBoveda(boveda_4);
                    request.getPayload().setFlagReporte(6);
                    documentoService.execute(request);
                    //log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|docConciliacion {0} ", c.getPayload().getDocConciliacion());
                    log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|getArchivoTxtInconsistenciasDescuentoB64");
                } else {
                    return response(null, ServiceStatusEnum.EXCEPCION, null, null);
                }                

                // POR DELEGACION CLAVE 20
                log.log(Level.INFO, "POR DELEGACION CLAVE 20");

                BovedaAssembler assembler2 = new BovedaAssembler();
                Response response_2 = client.create(assembler2.assembleXLSX( 
                        periodoNomina2Chars.concat(mesNominal)+tipoRepoChar,
                        request.getPayload().getRepDesctsEmitds().getArchivoXLSXBase64ByDel(),
                        20L,
                        request.getPayload().getSesion()
                ));
                
                if (response_2.getStatus() == 200) {
                    Boveda boveda_2 = response_2.readEntity(Boveda.class);
                    log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|getArchivoXLSXBase64ByDel boveda {0}", boveda_2);
                    request.getPayload().setBoveda(boveda_2);
                    request.getPayload().setFlagReporte(4);
                    documentoService.execute(request);
                    //log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|docConciliacion {0} ", c.getPayload().getDocConciliacion());
                    log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|getArchivoXLSXBase64ByDel");
                } else {
                    return response(null, ServiceStatusEnum.EXCEPCION, null, null);
                }
                
                
                return new Message<>(request.getPayload());

            case 4:
                
                BovedaAssembler assemblerCase4 = new BovedaAssembler();
                Response response = client.create(assemblerCase4.assembleReporteComprasCarteraEFXLSX(request.getPayload()));
                if (response.getStatus() == 200) {
                    Boveda boveda = response.readEntity(Boveda.class);
                    log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService ReporteComprasCarteraEF boveda {0}", boveda);
                    request.getPayload().setBoveda(boveda);
                    request.getPayload().setFlagReporte(7);
                    documentoService.execute(request);
                    //log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService|execute|docConciliacion {0} ", c.getPayload().getDocConciliacion());
                    log.log(Level.INFO, ">>>prestamoFront|BovedaReportesService ReporteComprasCarteraEF");
                } else {
                    return response(null, ServiceStatusEnum.EXCEPCION, null, null);
                }
                                
                return new Message<>(request.getPayload());

                //break;
            default:
                return response(null, ServiceStatusEnum.EXCEPCION, new ReporteExcepcion("Tipo de reporte inválido"), null);
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new ReporteExcepcion("Tipo de reporte inválido"), null);
        
        
    }
}
