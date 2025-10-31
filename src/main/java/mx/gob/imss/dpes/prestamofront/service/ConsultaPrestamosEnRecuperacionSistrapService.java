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

import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.interfaces.sipre.model.ConsultaCapacidadRequest;
import mx.gob.imss.dpes.interfaces.sipre.model.ConsultaPrestamosEnRecuperacionRQ;
import mx.gob.imss.dpes.interfaces.sipre.model.PrestamoRecuperado;
import mx.gob.imss.dpes.prestamofront.restclient.PrestamoSistrapClient;
import mx.gob.imss.dpes.prestamofront.model.PrestamoVigenteComposite;
import mx.gob.imss.dpes.prestamofront.model.PrestamosVigentesComposite;

/**
 *
 * @author antonio
 */
@Provider
public class ConsultaPrestamosEnRecuperacionSistrapService extends ServiceDefinition<PrestamosVigentesComposite, PrestamosVigentesComposite> {

    @Inject
    @RestClient
    private PrestamoSistrapClient prestamoSistrapClient;
    
    @Inject
    private EfByCveSipreService efByCveSipreService;

    @Override
    public Message<PrestamosVigentesComposite> execute(Message<PrestamosVigentesComposite> request)
            throws BusinessException {

        log.log(Level.INFO, ">>>ConsultaPrestamosEnRecuperacionSistrapService JUAN LOG ========= -1 |> Iniciando el servicio de consultas de prestamos en recuperacion a SISTRAP : {0}", request);

        ConsultaPrestamosEnRecuperacionRQ cperrq = new ConsultaPrestamosEnRecuperacionRQ();

        ConsultaCapacidadRequest ccr = new ConsultaCapacidadRequest(
                request.getPayload().getPensionado().getNss(),
                request.getPayload().getPensionado().getGrupoFamiliar()
        );

        cperrq.setPrestamosEnRecuperacionRq(ccr);

        log.log(Level.INFO, ">>>ConsultaPrestamosEnRecuperacionSistrapService JUAN LOG ========= 0 |> RQ armado para consulta a sipre : {0}", cperrq);

        try {
            Response load = prestamoSistrapClient.consultaPrestamosEnRecuperacion(cperrq);
            if (load.getStatus() == 200) {
                ConsultaPrestamosEnRecuperacionRQ prestamos = load.readEntity(ConsultaPrestamosEnRecuperacionRQ.class);
                log.log(Level.INFO, ">>>ConsultaPrestamosEnRecuperacionSistrapService JUAN LOG 1 ========= |> : {0}", cperrq);

                if(prestamos != null && prestamos.getPrestamosEnRecuperacionRs() != null && prestamos.getPrestamosEnRecuperacionRs().getPrestamosEnRecuperacionVoList() != null)
                for (PrestamoRecuperado p : prestamos.getPrestamosEnRecuperacionRs().getPrestamosEnRecuperacionVoList()) {

                    //log.log(Level.INFO, ">>>ConsultaPrestamosEnRecuperacionSistrapService JUAN LOG 1.1 ========= |> : {0}", p);                    
                    EntidadFinanciera e = new EntidadFinanciera();
                    String cveEntidadFinancieraSIPRE = p.getInsFinanciera();
                    //log.log(Level.INFO, ">>>ConsultaPrestamosEnRecuperacionSistrapService cveEntidadFinancieraSIPRE={0}", cveEntidadFinancieraSIPRE);
                    e.setIdSipre(cveEntidadFinancieraSIPRE);
                    Message<EntidadFinanciera> es =efByCveSipreService.execute(new Message<>(e));
                    p.setClabe(es.getPayload().getClabe());
//                    Integer mensualidadesDescontadas =  p.getMesesRecuperados().intValue();
//                    Integer descripcionPlazo = p.getNumMeses().intValue() ;
                    
                    //log.log(Level.INFO, ">>>ConsultaPrestamosEnRecuperacionSistrapService JUAN LOG 1.1 ========= mensualidadesDescontadas > : {0}", mensualidadesDescontadas);
                    //log.log(Level.INFO, ">>>ConsultaPrestamosEnRecuperacionSistrapService JUAN LOG 1.1 ========= descripcionPlazo > : {0}", descripcionPlazo);
/*
                    switch (descripcionPlazo) {
                        case 6:
                            if(mensualidadesDescontadas >= 6){
                                PrestamoVigenteComposite pvc = new PrestamoVigenteComposite();
                                pvc.setPrestamoRecuperado(p);
                                request.getPayload().getPrestamosVigentesComposite().add(pvc);
                            } 
                            break;
                        case 9:
                            if(mensualidadesDescontadas >= 9){
                                PrestamoVigenteComposite pvc = new PrestamoVigenteComposite();
                                pvc.setPrestamoRecuperado(p);
                                request.getPayload().getPrestamosVigentesComposite().add(pvc);
                            } 
                            break;    
                        case 12:
                            if(mensualidadesDescontadas >= 12){
                                PrestamoVigenteComposite pvc = new PrestamoVigenteComposite();
                                pvc.setPrestamoRecuperado(p);
                                request.getPayload().getPrestamosVigentesComposite().add(pvc);
                            } 
                            break;
                        case 18:
                            if(mensualidadesDescontadas >= 12){
                                PrestamoVigenteComposite pvc = new PrestamoVigenteComposite();
                                pvc.setPrestamoRecuperado(p);
                                request.getPayload().getPrestamosVigentesComposite().add(pvc);
                            } 
                            break;
                        case 24:
                            if(mensualidadesDescontadas >= 12){
                                PrestamoVigenteComposite pvc = new PrestamoVigenteComposite();
                                pvc.setPrestamoRecuperado(p);
                                request.getPayload().getPrestamosVigentesComposite().add(pvc);
                            } 
                        break;
                        default:
                            if(mensualidadesDescontadas >= 12){
                                PrestamoVigenteComposite pvc = new PrestamoVigenteComposite();
                                pvc.setPrestamoRecuperado(p);
                                request.getPayload().getPrestamosVigentesComposite().add(pvc);
                            } 
                            break;
                    }
*/
                    if(p.getNumMeses().intValue() > 24 && p.getMesesRecuperados().intValue() >= 24) {
                        PrestamoVigenteComposite pvc = new PrestamoVigenteComposite();
                        pvc.setPrestamoRecuperado(p);
                        request.getPayload().getPrestamosVigentesComposite().add(pvc);
                    }
                }

            } else {
                //TODO: Cambiar por excepcion de infraestructura   
                log.log(Level.INFO, "JUAN LOG 2 ========= |> : {0}", cperrq);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "JUAN LOG 3 ========= |> Error : {0}", e);
        }
        log.log(Level.INFO, "JUAN LOG 4 ========= |> Finalizo : {0}", request);

        return request;

    }

}
