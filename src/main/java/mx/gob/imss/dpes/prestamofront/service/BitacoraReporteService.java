/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraReporte;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.prestamofront.restclient.BitacoraClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class BitacoraReporteService extends ServiceDefinition<Reporte, Reporte> {

    @Inject
    @RestClient
    private BitacoraClient client;

    @Override
    public Message<Reporte> execute(Message<Reporte> request) throws BusinessException {

        log.log(Level.INFO, "BitacoraReporteService 1 {0} ", request.getPayload().getReporteRs());
        switch (request.getPayload().getTipoReporte().intValue()) {
            case 1:
                if (request.getPayload().getBitacoraReporte().getCveReporte() == null) {
                    request.getPayload().getBitacoraReporte().setCveReporte(
                            request.getPayload().getReporteRs().getReporte().getId()
                    );
                }

                if (request.getPayload().getBitacoraReporte().getEstadoReporte() == null) {
                    request.getPayload().getBitacoraReporte().setEstadoReporte(
                            request.getPayload().getReporteRs().getReporte().getEstadoReporte()
                    );
                }

                if (request.getPayload().getBitacoraReporte().getTipoReporte() == null) {
                    request.getPayload().getBitacoraReporte().setTipoReporte(
                            request.getPayload().getReporteRs().getReporte().getTipoReporte()
                    );
                }

                if (request.getPayload().getBitacoraReporte().getSubTipoReporte() == null) {
                    request.getPayload().getBitacoraReporte().setSubTipoReporte(
                            request.getPayload().getReporteRs().getReporte().getSubTipoReporte()
                    );
                }

                log.log(Level.INFO, "BitacoraReporteService 2 {0} ", request.getPayload().getBitacoraReporte());
                client.createBitacoraReporte(request.getPayload().getBitacoraReporte());
                return new Message<>(request.getPayload());
            case 4:

                log.log(Level.INFO, "BitacoraReporteService 3 {0} ", request.getPayload().getBitacoraReporte());
                request.getPayload().setBitacoraReporte(new BitacoraReporte());
                request.getPayload().getBitacoraReporte().setCveReporte(
                        request.getPayload().getReporteRs().getReporte().getId()
                );

                request.getPayload().getBitacoraReporte().setEstadoReporte(
                        request.getPayload().getReporteRs().getReporte().getEstadoReporte()
                );

                request.getPayload().getBitacoraReporte().setTipoReporte(
                        request.getPayload().getReporteRs().getReporte().getTipoReporte()
                );

                request.getPayload().getBitacoraReporte().setSubTipoReporte(
                        request.getPayload().getReporteRs().getReporte().getSubTipoReporte()
                );
                
                request.getPayload().getBitacoraReporte().setCurpUsuario(request.getPayload().getCurpUsuario());
                request.getPayload().getBitacoraReporte().setPeriodoNomina(request.getPayload().getAnioNominal()+""+request.getPayload().getMesNominal());

                log.log(Level.INFO, "BitacoraReporteService 4 {0} ", request.getPayload().getBitacoraReporte());
                client.createBitacoraReporte(request.getPayload().getBitacoraReporte());
                return new Message<>(request.getPayload());
        }
        return new Message<>(request.getPayload());

    }
}
