package mx.gob.imss.dpes.prestamofront.service;

import java.util.Date;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import mx.gob.imss.dpes.common.enums.BitacoraEnum;
import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.bitacora.model.Bitacora;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoRQ;
import mx.gob.imss.dpes.interfaces.sipre.model.RegistroPrestamoRequest;
import mx.gob.imss.dpes.interfaces.solicitud.model.EstadoSolicitud;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.prestamofront.assembler.RegistroPrestamoConvertidor;
import mx.gob.imss.dpes.prestamofront.exception.OperacionesSISTRAPException;
import mx.gob.imss.dpes.prestamofront.model.PrestamoEnRecuperacionRs;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.BitacoraClient;
import mx.gob.imss.dpes.prestamofront.restclient.EntidadFinancieraBackClient;
import mx.gob.imss.dpes.prestamofront.restclient.PrestamoClient;
import mx.gob.imss.dpes.prestamofront.restclient.PrestamoSistrapClient;
import mx.gob.imss.dpes.prestamofront.restclient.SolicitudClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Provider
public class OperacionesSISTRAPService extends ServiceDefinition<RequestPrestamo, RequestPrestamo> {

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
    @RestClient
    private SolicitudClient serviceClient;
	@Inject
	@RestClient
	private BitacoraClient bitacoraClient;
    

    private static final int SIPRE_SUCCESS = 1;
    private static final int SIPRE_ERROR = 0;
    private static String ALTA = "A";
    private static String BAJA = "B";
    private static String EXTENSION = "E";
    private static String SUSPENSION = "P";
    private static String REANUDAR = "R";

	@Override
	public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
		boolean bitacora = false;
		Message respuesta = null;
		try {
			
			// Recupera la clave de la entidad financiera en SIPRE y la anexa al prestamo
			request.getPayload().getPrestamo().getOferta().getEntidadFinanciera().setCveEntidadFinancieraSipre(this.obtenerEntidadFinancia(request.getPayload().getSolicitud().getCveEntidadFinanciera()));
		
			// Recueperamos la Solicitud de BD antes de la sincronizacion
			Solicitud solicitudRecuperada = this.recuperarSolicitud(request.getPayload().getSolicitud().getId());
			EstadoSolicitud estadoRecuperada = new EstadoSolicitud();
			estadoRecuperada.setId(solicitudRecuperada.getCveEstadoSolicitud().getId());
			estadoRecuperada.setDesEstadoSolicitud(solicitudRecuperada.getCveEstadoSolicitud().getDesEstadoSolicitud());
			int indSIPREStatusRecuperada = solicitudRecuperada.getIndSIPREStatus();
			
			//Instanciamos una Solicitud con los datos enviados desde el Front
			Solicitud solicitudPayload = request.getPayload().getSolicitud();
			
			//Actualizamos la solicitud en SIPRE antes de relizar la actualizacion es SISTRAP
			solicitudRecuperada.setIndSIPREStatus(SIPRE_ERROR);
			actualizaEstadoSolicitudSIPRE(solicitudPayload,solicitudRecuperada, false);
			
			//Mandamos la solicitud a SISTRAP para poder dar de baja el prestamo
			RegistroPrestamoRequest prestamoRequest = this.setRegistroPrestamo(request);
			if(sincronizacionSISTRAP(prestamoRequest)) {
				//Se actualiza el estado SIPRE de la solicitud para completar la sincronizacion de la baja
				bitacora = true;
				actualizacionEstadoSIPRE(solicitudPayload);
			} else {
				//En caso de que falle el servicio de SISTRAP, regresamos el valor de origen la solicitud y mandamos un error SISTRAP
				solicitudPayload.setCveEstadoSolicitud(estadoRecuperada);
				solicitudRecuperada.setIndSIPREStatus(indSIPREStatusRecuperada);
				actualizaEstadoSolicitudSIPRE(solicitudPayload,solicitudRecuperada, true);
				throw new OperacionesSISTRAPException(OperacionesSISTRAPException.ERROR_OPERACIONES_SISTRAP);
			}
		}catch (BusinessException e) {
			respuesta = response(null, ServiceStatusEnum.EXCEPCION, e, null);
		}catch (Exception e) {
			log.log(Level.SEVERE, "ERROR OperacionesSISTRAPService.execute() - request[" + request + "]", e);
		} finally {
			if (bitacora){
				this.registrarBitacora(request.getPayload());
				return new Message<>(request.getPayload());
			}
		}
		if (respuesta != null)
			return respuesta;
		else
			return response(null,
				ServiceStatusEnum.EXCEPCION,
				new OperacionesSISTRAPException(OperacionesSISTRAPException.ERROR_DESCONOCIDO),
				null);

	}
	
	private String obtenerEntidadFinancia(Long cveEntidadFinanciera) throws BusinessException {
		try {
			if(cveEntidadFinanciera == null)
				throw new OperacionesSISTRAPException(OperacionesSISTRAPException.DATO_NULO);
			
			Response responseEF = entidadFinancieraClient.getEntidadFinanciera(cveEntidadFinanciera);
			if (!(responseEF != null && responseEF.getStatus() == 200))
				throw new OperacionesSISTRAPException(OperacionesSISTRAPException.ERROR_CONSULTA_ENTIDAD_FINANCIERA);

			EntidadFinanciera entidadFinanciera = responseEF.readEntity(EntidadFinanciera.class);
			return entidadFinanciera.getCveEntidadFinancieraSipre();

		} catch (BusinessException e) {
			log.log(Level.SEVERE, "ERROR! OperacionesSISTRAPService.obtenerEntidadFinancia - " +
					"cveEntidadFinanciera = [" + cveEntidadFinanciera + "]", e);
			throw e;
		} catch (Exception ex) {
			log.log(Level.SEVERE,
					"ERROR! OperacionesSISTRAPService.obtenerEntidadFinancia - " +
							"cveEntidadFinanciera = [" + cveEntidadFinanciera + "]", ex);
			throw new OperacionesSISTRAPException(OperacionesSISTRAPException.ERROR_DESCONOCIDO);
		}
	}
	
	private Solicitud recuperarSolicitud(Long idSolicitud) throws BusinessException {
		try {
			if (idSolicitud == null)
				throw new OperacionesSISTRAPException(OperacionesSISTRAPException.DATO_NULO);

			Response solicitudAuxResponse = serviceClient.getSolicitud(idSolicitud);
			if (solicitudAuxResponse != null && solicitudAuxResponse.getStatus() == 200)
				return solicitudAuxResponse.readEntity(Solicitud.class);
			else
				throw new OperacionesSISTRAPException(OperacionesSISTRAPException.ERROR_OBTENCION_SOLICITUD);

		} catch (BusinessException e) {
			log.log(Level.SEVERE, "ERROR! OperacionesSISTRAPService.recuperarSolicitud - " +
					"idSolicitud = [" + idSolicitud + "]", e);
			throw e;
		} catch (Exception ex) {
			log.log(Level.SEVERE, "ERROR! OperacionesSISTRAPService.recuperarSolicitud - " +
					"idSolicitud = [" + idSolicitud + "]", ex);
			throw new OperacionesSISTRAPException(OperacionesSISTRAPException.ERROR_DESCONOCIDO);
		}
	}
	
	protected void actualizaEstadoSolicitudSIPRE (Solicitud solicitudPayload, Solicitud solicitudRecuperada,
				  boolean notificaAdmin) throws BusinessException {
		try {
			if (solicitudPayload.getEstadoSolicitud() == null && solicitudPayload.getCveEstadoSolicitud().getId() == null)
				throw new OperacionesSISTRAPException(OperacionesSISTRAPException.DATO_NULO);

			if (solicitudPayload.getCveEstadoSolicitud() == null) 
				solicitudRecuperada.getCveEstadoSolicitud().setId(solicitudPayload.getEstadoSolicitud().toValue());
			else 
				solicitudRecuperada.getCveEstadoSolicitud().setId(solicitudPayload.getCveEstadoSolicitud().getId());
			
			solicitudRecuperada.setFecSIPREModifica(new Date());
			
			Response load = serviceClient.updateEstadoPreSISTRAP(solicitudRecuperada);
			if (!(load != null && load.getStatus() == 200)){
				if(notificaAdmin)
					throw new OperacionesSISTRAPException(
							OperacionesSISTRAPException.ERROR_ACTUALIZACION_SOLICITUD_ADMINISTRADOR
					);
				else
					throw new OperacionesSISTRAPException(
							OperacionesSISTRAPException.ERROR_ACTUALIZACION_SOLICITUD
					);
			}
    	} catch (BusinessException e) {
			log.log(Level.SEVERE, "ERROR! OperacionesSISTRAPService.actualizaEstadoSolicitudSIPRE - " +
					"SolicitudPayload = [" + solicitudPayload + "], " +
					"solicitudRecuperada = [" + solicitudRecuperada + "]", e);
			throw e;
		} catch (Exception ex) {
			log.log(Level.SEVERE, "ERROR! OperacionesSISTRAPService.actualizaEstadoSolicitudSIPRE - " +
					"SolicitudPayload = [" + solicitudPayload + "], " +
					"solicitudRecuperada = [" + solicitudRecuperada + "]", ex);
			if(notificaAdmin)
				throw new OperacionesSISTRAPException(OperacionesSISTRAPException.ERROR_DESCONOCIDO_ADMINISTRADOR);
			else
				throw new OperacionesSISTRAPException(OperacionesSISTRAPException.ERROR_DESCONOCIDO);
		}
    	
    }
	
	 protected boolean sincronizacionSISTRAP(RegistroPrestamoRequest registroPrestamoRequest) {
		 RegistroPrestamoRQ responseRegistroPrestamo = null;
		 try {
			 if(registroPrestamoRequest == null) {
				 log.log(Level.SEVERE, ">>>ERROR! OperacionesSISTRAPService.sincronizacionSISTRAP - " +
						 "registroPrestamoRequest = [" + registroPrestamoRequest + "]");
				 return false; 
			 }
			 responseRegistroPrestamo = sipreClient.save(registroPrestamoRequest);
			 if (responseRegistroPrestamo != null && responseRegistroPrestamo.getRegistroPrestamoResponse() != null &&
					 responseRegistroPrestamo.getRegistroPrestamoResponse().getCodigoError() != null &&
					 responseRegistroPrestamo.getRegistroPrestamoResponse().getCodigoError().equals("200"))
				return true;
			 else{
				 log.log(Level.SEVERE, "ERROR OperacionesSISTRAPService.sincronizacionSISTRAP - " +
						 "registroPrestamoRequest = [" + registroPrestamoRequest + "], " +
						 "responseRegistroPrestamo = [" + responseRegistroPrestamo + "]");
				 return false;
			 }
		 } catch (Exception ex) {
				log.log(Level.SEVERE, "ERROR! OperacionesSISTRAPService.sincronizacionSISTRAP - " +
						"registroPrestamoRequest = [" + registroPrestamoRequest + "], " +
						"responseRegistroPrestamo = [" + responseRegistroPrestamo + "]", ex);
				return false;
		 }
		
	}
	
	protected void actualizacionEstadoSIPRE(Solicitud solicitud) {
		try {
			solicitud.setIndSIPREStatus(SIPRE_SUCCESS);
			solicitud.setFecSIPREModifica(new Date());
			Response respuesta = solicitudClient.updateSolicitud(solicitud);
			if (!(respuesta != null && respuesta.getStatus() == 200))
				throw new OperacionesSISTRAPException(OperacionesSISTRAPException.ERROR_ACTUALIZACION_SOLICITUD);
		} catch (BusinessException e) {
			log.log(Level.WARNING, "ERROR! BajaPrestamoSISTRAP.actualizacionEstadoSIPRE() - " +
					"solicitud = [" + solicitud + "]", e);
		} catch (Exception ex) {
			log.log(Level.WARNING, "ERROR! BajaPrestamoSISTRAP.actualizacionEstadoSIPRE() - " +
					"solicitud = [" + solicitud + "]", ex);
		}
	}

	protected RegistroPrestamoRequest setRegistroPrestamo (Message<RequestPrestamo> request) throws BusinessException {
		try {
			Solicitud solicitud = request.getPayload().getSolicitud();
					
			RegistroPrestamoConvertidor convertidor = new RegistroPrestamoConvertidor();
			RegistroPrestamoRequest registroPrestamoRequest = convertidor.ensambladorRegistroPrestamo(request.getPayload());
			
			if(registroPrestamoRequest == null) 
				return registroPrestamoRequest;
			
			Response load = prestamoClient.consultaPrestamosPorSolicitud(solicitud.getId());
			if (!(load != null && load.getStatus() == 200))
				throw new OperacionesSISTRAPException(OperacionesSISTRAPException.ERROR_CONSULTA_PRESTAMO_SOLICITUD);
			
			PrestamoEnRecuperacionRs rs = load.readEntity(PrestamoEnRecuperacionRs.class);
			registroPrestamoRequest.setTipoMovimiento(
					tipoMovimiento(
							solicitud.getCveEstadoSolicitud().getId(),
							rs.getPrestamosEnRecuperacion().size()
					));
			
			int i = 0;
			String prestamosRecuperacionFolios = "";
			if (rs.getPrestamosEnRecuperacion().size() > 0) {
				for (PrestamoRecuperacion prestamoRecuperacion : rs.getPrestamosEnRecuperacion()) {
					if (i > 0) {
						prestamosRecuperacionFolios += ",";
					}
					prestamosRecuperacionFolios += prestamoRecuperacion.getNumSolicitudSipre();
					i++;
				}
			}
			
			registroPrestamoRequest.setIdSolPrestLiq1(prestamosRecuperacionFolios);
			
			return registroPrestamoRequest;
		} catch (BusinessException e) {
			log.log(Level.SEVERE, "ERROR! BajaPrestamoSISTRAP.setRegistroPrestamo() - " +
					"request = [" + request + "]", e);
			throw e;
		} catch (Exception ex) {
			log.log(Level.SEVERE, "ERROR! BajaPrestamoSISTRAP.setRegistroPrestamo() - " +
					"request = [" + request + "]", ex);
			throw new OperacionesSISTRAPException(OperacionesSISTRAPException.ERROR_DESCONOCIDO);
		}
		
	}

	protected String tipoMovimiento(Long estadoSolicitud, int totalPrestamoRecuperacion) {
		String tipoMovimiento = "";
		
		if (totalPrestamoRecuperacion > 0)
			tipoMovimiento = EXTENSION;
		else
			tipoMovimiento = ALTA;
		
		if (estadoSolicitud == TipoEstadoSolicitudEnum.PRESTAMO_RECUPERACION.toValue())
			tipoMovimiento = REANUDAR;
		
		if (estadoSolicitud == TipoEstadoSolicitudEnum.PRESTAMO.toValue())
			tipoMovimiento = SUSPENSION;
		
		if (estadoSolicitud == TipoEstadoSolicitudEnum.BAJA_IMPROCEDENCIA.toValue()
				|| estadoSolicitud == TipoEstadoSolicitudEnum.BAJA_LIQUIDACION_ANTICIPADA.toValue())
			tipoMovimiento = BAJA;

		return tipoMovimiento;
	}

	protected void registrarBitacora(RequestPrestamo prestamo) {
		try {
			Bitacora bitacora = new Bitacora();
			switch (prestamo.getBitacora().getTipo().intValue()){
				case 1:
					bitacora.setTipo(BitacoraEnum.CANCELAR_PRESTAMO);
					break;
				case 2:
					bitacora.setTipo(BitacoraEnum.SUSPENDER_PRESTAMO);
					break;
				case 3:
					bitacora.setTipo(BitacoraEnum.REANUDAR_PRESTAMO);
					break;
			}
			bitacora.setCurp(prestamo.getBitacora().getCurp());
			bitacora.setSesion(new Long(prestamo.getBitacora().getSesion()));
			bitacora.setIdSolicitud(prestamo.getSolicitud().getId());
			bitacora.setEstadoSolicitud(
					prestamo.getSolicitud().getCveEstadoSolicitud() == null?
							TipoEstadoSolicitudEnum.forValue(prestamo.getSolicitud().getEstadoSolicitud().toValue()) :
							TipoEstadoSolicitudEnum.forValue(prestamo.getSolicitud().getCveEstadoSolicitud().getId()
					));
			bitacoraClient.create(bitacora);
		}catch (Exception e){
			log.log(Level.WARNING, "ERROR OperacionesSISTRAPService.registraBitacora() - " +
					"prestamo = [" + prestamo + "]", e);
		}
	}

}
