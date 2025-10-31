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
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.DocumentoClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author gabriel.rios
 */
@Provider
public class UpdateDocumentsService extends ServiceDefinition<RequestPrestamo, RequestPrestamo> {

    @Inject
    @RestClient
    private DocumentoClient documentClient;

    @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {

        Solicitud solicitud = request.getPayload().getSolicitud();
        solicitud.setId(request.getPayload().getPrestamo().getSolicitud());
        Long cveSolicitud = solicitud.getId();
        log.log(Level.INFO, ">>>UpdateDocumentsService solicitud: {0}", solicitud);
        Documento cepPensionado = request.getPayload().getDocPdfPensionadoCEP();
        cepPensionado.setCveSolicitud(cveSolicitud);
        cepPensionado.setTipoDocumento(TipoDocumentoEnum.CEP_PENSIONADO);
        updateSolicitudCleanByTipo(cepPensionado);
        log.log(Level.INFO, ">>>UpdateDocumentsService cepPensionado: {0}", cepPensionado);
        cepPensionado = updateDocument(cepPensionado);
        request.getPayload().setDocPdfPensionadoCEP(cepPensionado);
        Documento cepPensionadoXML = request.getPayload().getDocXmlPensionadoCEP();
        if(cepPensionadoXML!=null && cepPensionadoXML.getId()!=null){
            cepPensionadoXML.setCveSolicitud(cveSolicitud);
            cepPensionadoXML.setTipoDocumento(TipoDocumentoEnum.CEP_PENSIONADO_XML);
            updateSolicitudCleanByTipo(cepPensionadoXML);
            cepPensionadoXML = updateDocument(cepPensionadoXML);
            request.getPayload().setDocXmlPensionadoCEP(cepPensionadoXML);
        }
        
        List<Documento> cepEFList = request.getPayload().getCepEntidadFinancieraLista();
        updateCepEntidadFinancieraList(cepEFList,cveSolicitud,TipoDocumentoEnum.CEP_ENTIDAD_FINANCIERA);
        List<Documento> cepEFListXML = request.getPayload().getCepEntidadFinancieraListaXML();
        updateCepEntidadFinancieraList(cepEFListXML,cveSolicitud,TipoDocumentoEnum.CEP_ENTIDAD_FINANCIERA_XML);
        
        return request;
    }
    
    protected void updateCepEntidadFinancieraList(List<Documento> cepEFList,Long cveSolicitud , TipoDocumentoEnum tipoDocumentoEnum) throws BusinessException {
        log.log(Level.INFO, ">>>updateCepEntidadFinancieraList tipoDocumentoEnum="+tipoDocumentoEnum+" cepEFList = {0}", cepEFList);
        boolean isCleaned = false;
        int i = 0;
        if(cepEFList!=null && cepEFList.size()>0){
            for(Documento documento:cepEFList){
                documento.setCveSolicitud(cveSolicitud);
                documento.setTipoDocumento(tipoDocumentoEnum);
                log.log(Level.INFO, "       >>>updateCepEntidadFinancieraList before update documento["+i+"]="+documento+" =>"+tipoDocumentoEnum);
                if(!isCleaned){
                    if(documento!=null && documento.getId()!=null){
                        updateSolicitudCleanByTipo(documento);
                        isCleaned = true;
                    }
                }
                if(documento!=null && documento.getId()!=null){
                    documento = updateDocument(documento);
                }
                log.log(Level.INFO, "       >>>updateCepEntidadFinancieraList After updateDocument [" + i+"]= {0}", documento);
                i++;
            }
        }
        
    }

    protected Documento updateDocument(Documento documento) throws BusinessException {
        Response respuesta = documentClient.updateSolicitud(documento);
        if (respuesta.getStatus() == 200) {
            documento = respuesta.readEntity(Documento.class);
            log.log(Level.INFO, ">>>UpdateDocumentsService updateDocuments [" + documento.getTipoDocumento() + "]= {0}", documento);

        } else {
            log.log(Level.SEVERE, ">>>UpdateDocumentsService ERROR updateDocuments [" + documento.getTipoDocumento() + "]= {0}", documento);
            throw new UnknowException();
        }
        return documento;
    }
    
    protected void updateSolicitudCleanByTipo(Documento documento) throws BusinessException {
        Response respuesta = documentClient.updateSolicitudCleanByTipo(documento);
        if (respuesta.getStatus() != 200) {
            log.log(Level.SEVERE, ">>>UpdateDocumentsService ERROR updateSolicitudCleanByTipo documento==" + documento);
            throw new UnknowException();
        }
        
    }

}
