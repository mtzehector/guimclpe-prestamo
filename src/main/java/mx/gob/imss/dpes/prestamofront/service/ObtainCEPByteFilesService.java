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
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.BovedaClient;
import mx.gob.imss.dpes.prestamofront.restclient.ObtenerDocumentosClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author gabriel.rios
 */
@Provider
public class ObtainCEPByteFilesService extends ServiceDefinition<RequestPrestamo, RequestPrestamo> {

    @Inject
    @RestClient
    private ObtenerDocumentosClient obtenerDocumentos;

    @Inject
    BovedaService boveda;

    @Inject
    @RestClient
    private BovedaClient bovedaClient;

    @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {

        //log.log(Level.INFO, ">>>getCEPByteFilesService: {0}", request.getPayload());
        List<Documento> cepEFList = request.getPayload().getCepEntidadFinancieraLista();
        List<Documento> cepEFListXML = request.getPayload().getCepEntidadFinancieraListaXML();
        fillByteFiles(cepEFList, request.getPayload());
        fillByteFiles(cepEFListXML, request.getPayload());

        return request;

    }

    protected void fillByteFiles(List<Documento> cepEFList, RequestPrestamo request) {
        int i = 0;
        if (cepEFList != null && cepEFList.size() > 0) {
            for (Documento documento : cepEFList) {
                if (documento != null && documento.getId() != null) {
                    request.setDocumento(new mx.gob.imss.dpes.prestamofront.model.Documento());
                    request.getDocumento().setIdDocumento(documento.getRefDocBoveda());
                    Response load = bovedaClient.loadBovedaV3(request);
                    if (load.getStatus() == 200) {
                        RequestPrestamo bovedaOut = load.readEntity(RequestPrestamo.class);
                        documento.setArchivo(bovedaOut.getRespuestaBoveda().getArchivo());
                    }
                }
            }
        }
    }
}
