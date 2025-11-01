package mx.gob.imss.dpes.prestamofront.service;

import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.solicitud.model.SolicitudEstado;
import mx.gob.imss.dpes.prestamofront.exception.AutorizarPrestamoException;
import mx.gob.imss.dpes.prestamofront.restclient.DocumentoClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class ActualizarDocumentosReinstalacionService extends ServiceDefinition<SolicitudEstado, SolicitudEstado> {

    @Inject
    @RestClient
    private DocumentoClient documentClient;

    @Override
    public Message<SolicitudEstado> execute(Message<SolicitudEstado> request) throws BusinessException {
        try {
            Response responseDocumento = documentClient.obtenerPorSolicitudYRefBoveda(request.getPayload().getIdSolicitud());

            if (responseDocumento != null && responseDocumento.getStatus() == 200) {
                Documento documentoReinstalacion = responseDocumento.readEntity(Documento.class);

                Documento documentoCleanByTipo = new Documento();
                documentoCleanByTipo.setCveSolicitud(request.getPayload().getIdSolicitud());
                documentoCleanByTipo.setTipoDocumento(TipoDocumentoEnum.CARTA_REINSTALACION);

                Response responseCleanByTipo = documentClient.updateSolicitudCleanByTipo(documentoReinstalacion);
                if (responseCleanByTipo != null && responseCleanByTipo.getStatus() == 200) {

                    Response respuestaSolicitud = documentClient.updateSolicitud(documentoReinstalacion);
                    if (respuestaSolicitud != null && respuestaSolicitud.getStatus() == 200) {

                        Documento documentoActualizado = respuestaSolicitud.readEntity(Documento.class);
                        log.log(Level.INFO, "Los documentos de la reinstalacion fueron actualizados correctamente = {0}", documentoActualizado);
                        return request;
                    }
                }
            }
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR ActualizarDocumentosReinstalacionService.execute()", e);
        }
        return response(
                null,
                ServiceStatusEnum.EXCEPCION,
                new AutorizarPrestamoException(AutorizarPrestamoException.ERROR_AL_ACTUALIZAR_DOCUMENTOS_REINSTALACION),
                null
        );
    }
}
