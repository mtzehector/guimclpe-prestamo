/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.util.Date;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.SIPRELoanException;
import mx.gob.imss.dpes.common.exception.VariableMessageException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoRQ;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoRequest;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.prestamofront.assembler.RegistroPrestamoAssembler;
import mx.gob.imss.dpes.prestamofront.exception.GuardaPrestamoException;
import mx.gob.imss.dpes.prestamofront.model.PersonaModel;
import mx.gob.imss.dpes.prestamofront.model.PrestamoEnRecuperacionRs;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.EntidadFinancieraBackClient;
import mx.gob.imss.dpes.prestamofront.restclient.PrestamoClient;
import mx.gob.imss.dpes.prestamofront.restclient.PrestamoSistrapClient;
import mx.gob.imss.dpes.prestamofront.restclient.SolicitudClient;
import mx.gob.imss.dpes.support.util.ServiceLogger;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author gabriel.rios
 */
@Provider
public class PrestamoSIPREService extends ServiceDefinition<RequestPrestamo, RequestPrestamo> {

    @Inject
    @RestClient
    private PrestamoSistrapClient sipreClient;
    @Inject
    @RestClient
    private EntidadFinancieraBackClient entidadFinancieraClient;
    @Inject
    @RestClient
    private SolicitudClient solicitudClient;
    @Inject
    @RestClient
    private PrestamoClient prestamoClient;

    @Inject
    private CreateBitacoraService createBitacoraService;

    private static final int SIPRE_SUCCESS = 1;
    private static final int SIPRE_ERROR = 0;
    private static String ALTA = "A";
    private static String BAJA = "B";
    private static String SOLICITUD = "S";
    private static String EXTENSION = "E";
    private static String SUSPENSION = "P";
    private static String REANUDAR = "R";

    @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
        log.log(Level.INFO, ">>> prestamoFront PrestamoSIPREService.execute request={0}", request);
        String tipoMovimiento = "";
        String prestamosRecuperacionFolios = "";
        Response responseEF = entidadFinancieraClient.getEntidadFinanciera(request.getPayload().getSolicitud().getCveEntidadFinanciera());
        EntidadFinanciera entidadFinanciera = responseEF.readEntity(EntidadFinanciera.class);

        request.getPayload().getPrestamo().getOferta().getEntidadFinanciera().setCveEntidadFinancieraSipre(entidadFinanciera.getCveEntidadFinancieraSipre());
        RegistroPrestamoAssembler assembler = new RegistroPrestamoAssembler();
        RegistroPrestamoRequest registroPrestamoRequest = assembler.assemble(request.getPayload());
        Solicitud solicitud = request.getPayload().getSolicitud();
        registroPrestamoRequest.setIdSolPrestLiq1("");
        registroPrestamoRequest.setIdSolPrestLiq2("");
        registroPrestamoRequest.setIdSolPrestLiq3("");
        Response load = prestamoClient.consultaPrestamosPorSolicitud(solicitud.getId());
        log.log(Level.INFO, ">>> prestamoFront PrestamoSIPREService.execute consultaPrestamosPorSolicitud load={0}", load);
        if (load.getStatus() == 200) {
            PrestamoEnRecuperacionRs rs = load.readEntity(PrestamoEnRecuperacionRs.class);
            int i = 0;
            log.log(Level.INFO, ">>> prestamoFront PrestamoSIPREService.execute  rs.getPrestamosEnRecuperacion().size={0}", rs.getPrestamosEnRecuperacion().size());
            if (rs.getPrestamosEnRecuperacion().size() > 0) {
                tipoMovimiento = EXTENSION;
                for (PrestamoRecuperacion prestamoRecuperacion : rs.getPrestamosEnRecuperacion()) {
                    if (i > 0) {
                        prestamosRecuperacionFolios += ",";
                    }
                    prestamosRecuperacionFolios += prestamoRecuperacion.getNumSolicitudSipre();
                    log.log(Level.INFO, "           >>> prestamoFront PrestamoSIPREService.execute prestamoRecuperacion[" + i + "]=" + prestamoRecuperacion);
                    i++;
                }
            } else {
                tipoMovimiento = ALTA;
            }
        }

        if ((request.getPayload().getSolicitud().getCveEstadoSolicitud().getId() == (TipoEstadoSolicitudEnum.BAJA_IMPROCEDENCIA.toValue()) || request.getPayload().getSolicitud().getCveEstadoSolicitud().getId() == (TipoEstadoSolicitudEnum.BAJA_LIQUIDACION_ANTICIPADA.toValue()))) {
            tipoMovimiento = BAJA;

        }
        if ((request.getPayload().getSolicitud().getCveEstadoSolicitud().getId() == (TipoEstadoSolicitudEnum.PRESTAMO.toValue()))) {
            tipoMovimiento = SUSPENSION;

        }
        if ((request.getPayload().getSolicitud().getCveEstadoSolicitud().getId() == (TipoEstadoSolicitudEnum.PRESTAMO_RECUPERACION.toValue()))) {
            tipoMovimiento = REANUDAR;

        }

        registroPrestamoRequest.setIdSolPrestLiq1(prestamosRecuperacionFolios);
        registroPrestamoRequest.setTipoMovimiento(tipoMovimiento);
        log.log(Level.INFO, ">>> prestamoFront PrestamoSIPREService.execute  prestamosRecuperacionFolios=" + prestamosRecuperacionFolios + " tipoMovimiento=" + tipoMovimiento);
        //log.log(Level.INFO, ">>> PrestamoFront PrestamoSIPREService.execute before sendSipreLoan registroPrestamoRequest={0}", registroPrestamoRequest);
        sendSipreLoan(registroPrestamoRequest, request);
        return new Message<>(request.getPayload());

    }

    protected boolean sendSipreLoan(RegistroPrestamoRequest registroPrestamoRequest, Message<RequestPrestamo> request) throws BusinessException {
        Solicitud solicitud = request.getPayload().getSolicitud();
        log.log(Level.INFO, ">>> PrestamoFront PrestamoSIPREService.sendSipreLoan registroPrestamoRequest={0}", registroPrestamoRequest);
        log.log(Level.INFO, ">>> PrestamoFront PrestamoSIPREService.sendSipreLoan TipoMovimiento={0}", registroPrestamoRequest.getTipoMovimiento());
        if (!(registroPrestamoRequest.getNombre() != null)) {
            registroPrestamoRequest.setApellidoMaterno("Materno");
            registroPrestamoRequest.setApellidoPaterno("Materno");
            registroPrestamoRequest.setNombre("Nombre");
            log.log(Level.INFO, ">>> ERPE sendSipreLoan PrestamoFront PrestamoSIPREService.sendSipreLoan registroPrestamoRequest={0}", registroPrestamoRequest);

        }

        RegistroPrestamoRQ response = sipreClient.save(registroPrestamoRequest);
        int codigoError = 0;
        log.log(Level.INFO, ">>> PrestamoFront PrestamoSIPREService.sendSipreLoan response={0}", response);
        log.log(Level.INFO, ">>> PrestamoFront PrestamoSIPREService.sendSipreLoan response.getRegistroPrestamoResponse().getCodigoError()={0}", response.getRegistroPrestamoResponse().getCodigoError());
        codigoError = Integer.parseInt(response.getRegistroPrestamoResponse().getCodigoError());

        if (codigoError == 200) {
            solicitud.setIndSIPREStatus(SIPRE_SUCCESS);
            solicitud.setFecSIPREModifica(new Date());
            solicitudClient.updateSolicitud(solicitud);
            return true;
        }

        if (codigoError == 401) {

            log.log(Level.SEVERE, ">>>ERROR! prestamoFront PrestamoSIPREService.execute Error !=200  Msg=" + response.getRegistroPrestamoResponse().getMensajeError() + " codigoError=" + codigoError);

            //createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(), codigoError, 1L, true)));
            throw new GuardaPrestamoException();
        }

        log.log(Level.SEVERE, ">>>ERROR! prestamoFront PrestamoSIPREService.execute Error !=200  Msg=" + response.getRegistroPrestamoResponse().getMensajeError() + " codigoError=" + codigoError);

        //createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(), codigoError, 1L, true)));
        throw new SIPRELoanException(codigoError);

    }

}
