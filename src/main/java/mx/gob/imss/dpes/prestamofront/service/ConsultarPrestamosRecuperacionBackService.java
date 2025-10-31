/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.util.List;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.prestamofront.exception.PrestamoException;
import mx.gob.imss.dpes.prestamofront.model.PrestamoEnRecuperacionRs;
import mx.gob.imss.dpes.prestamofront.model.PrestamosEnRecuperacionRequest;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.PrestamoClient;
import mx.gob.imss.dpes.prestamofront.rules.FechaLimite;
import mx.gob.imss.dpes.prestamofront.rules.ValidarMontos;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ConsultarPrestamosRecuperacionBackService extends ServiceDefinition<PrestamosEnRecuperacionRequest, PrestamosEnRecuperacionRequest> {

    @Inject
    @RestClient
    private PrestamoClient service;

    @Inject
    private EfByCveSipreService efByCveSipreService;

    @Override
    public Message<PrestamosEnRecuperacionRequest> execute(Message<PrestamosEnRecuperacionRequest> request) throws BusinessException {

        Response load = service.consultaPrestamosPorSolicitud(request.getPayload().getRequest().getId());
        if (load.getStatus() == 200) {
            PrestamoEnRecuperacionRs rs = load.readEntity(PrestamoEnRecuperacionRs.class);
//            for (PrestamoRecuperacion p : rs.getPrestamosEnRecuperacion()) {
//                    EntidadFinanciera e = new EntidadFinanciera();
//                    e.setIdSipre(p.getNumEntidadFinanciera().trim());
//                    Message<EntidadFinanciera> es =efByCveSipreService.execute(new Message<>(e));
//                    p.setNombreComercial(es.getPayload().getNombreComercial());
//            }

            request.getPayload().setResponse(rs);

            return new Message<>(request.getPayload());

        } else {

            return response(null, ServiceStatusEnum.EXCEPCION, new PrestamoException(), null);
        }

    }
}
