/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Correo;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.CorreoSinAdjuntos;
import mx.gob.imss.dpes.prestamofront.model.RequestPrestamo;
import mx.gob.imss.dpes.prestamofront.restclient.CorreoClient;
import mx.gob.imss.dpes.prestamofront.restclient.EntidadFinancieraBackClient;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author gabriel.rios
 */
@Provider
public class CorreoCompraCarteraService extends ServiceDefinition<RequestPrestamo, RequestPrestamo> {

    @Inject
    @RestClient
    private CorreoClient correoClient;
    @Inject
    private Config config;
    @Inject
    @RestClient
    private EntidadFinancieraBackClient entidadFinancieraClient;

    @Override
    public Message<RequestPrestamo> execute(Message<RequestPrestamo> request) throws BusinessException {
        //log.log(Level.INFO, ">>>CorreoCompraCarteraService RequestPrestamo={0}", request.getPayload());
        List<Documento> cepEFList = request.getPayload().getCepEntidadFinancieraLista();
        List<Documento> cepEFListXML = request.getPayload().getCepEntidadFinancieraListaXML();
        int i = 0;
        if (cepEFList != null && cepEFList.size() > 0) {
            for (Documento documento : cepEFList) {
                log.log(Level.INFO, ">>>CorreoCompraCarteraService[" + (i) + "] cveEntidadFinanciera=" + documento.getCveEntidadFinanciera() + " folioSIPRE=" + documento.getFolioSIPRE() + " numEntidadFinancieraSIPRE=" + documento.getNumEntidadFinancieraSIPRE());
                Correo correo = new Correo();
                Long cveEntidadFinanciera = documento.getCveEntidadFinanciera();
                if (cveEntidadFinanciera != null && cveEntidadFinanciera > 0) {
                    Response response = entidadFinancieraClient.getEntidadFinanciera(cveEntidadFinanciera);
                    EntidadFinanciera entidadFinanciera = response.readEntity(EntidadFinanciera.class);
                    String correoAdminEF = entidadFinanciera.getCorreoAdminEF();
                    String nombreComercial = entidadFinanciera.getNombreComercial();
                    String folioSIPRE = documento.getFolioSIPRE();
                    ArrayList<String> correos = new ArrayList<>();
                    correos.add(correoAdminEF);
                    ArrayList<Adjunto> adjuntos = new ArrayList<Adjunto>();
                    Adjunto adjunto1 = new Adjunto();
                    adjunto1.setNombreAdjunto("CEP.pdf");
                    adjunto1.setAdjuntoBase64(documento.getArchivo());
                    adjuntos.add(adjunto1);
                    if(cepEFListXML.get(i)!=null && cepEFListXML.get(i).getId()!=null){
                        Adjunto adjunto2 = new Adjunto();
                        adjunto2.setNombreAdjunto("CEP.xml");
                        adjunto2.setAdjuntoBase64(cepEFListXML.get(i).getArchivo());
                        adjuntos.add(adjunto2);
                    }
                    correo.setAdjuntos(adjuntos);
                    sendEmailWithAttach(nombreComercial,folioSIPRE,correos,adjuntos);
                } else {
                    log.log(Level.SEVERE, ">>>>>>CorreoCompraCarteraService NO se encuentra la Entidad Financiera asociada a numEntidadFinancieraSIPRE={0}", documento.getNumEntidadFinancieraSIPRE());
                }
                i++;
            }
        }
        return request;
    }


    protected void sendEmail(String nombreComercial, String folioSIPRE, byte[] archivo) {
        log.log(Level.INFO, ">>>CorreoCompraCarteraService sendEmail Init ");
        String plantilla = String.format(config.getValue("plantillaParaAdminEF-Compra_cartera", String.class),
                nombreComercial, folioSIPRE);

        Correo correo = new Correo();

        try {
            correo.setCuerpoCorreo(new String(plantilla.getBytes(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CorreoService.class.getName()).log(Level.SEVERE, null, ex);
        }

        correo.setAsunto("Compra de Cartera");
        ArrayList<String> correos = new ArrayList<>();
//correos.add("gabriel.rios@softtek.com");
        correo.setCorreoPara(correos);
        ArrayList<Adjunto> adjuntos = new ArrayList<Adjunto>();
         correo.setAdjuntos(adjuntos);
        Response response = correoClient.enviaCorreo(correo);

        if (response.getStatus() == 200 || response.getStatus() == 204) {
            log.log(Level.INFO, ">>>>>>CorreoCompraCarteraService sendEmail SE ENVIO CORREO DE AVISO={0}", response.getStatus());

        }
    }
    
    protected void sendEmailWithAttach(String nombreComercial, String folioSIPRE,ArrayList<String> correos, ArrayList<Adjunto> adjuntos) {
        log.log(Level.INFO, ">>>CorreoCompraCarteraService sendEmail Init ");
        String plantilla = String.format(config.getValue("plantillaParaAdminEF-Compra_cartera", String.class),
                nombreComercial, folioSIPRE);

        Correo correo = new Correo();

        try {
            correo.setCuerpoCorreo(new String(plantilla.getBytes(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CorreoService.class.getName()).log(Level.SEVERE, null, ex);
        }

        correo.setAsunto("Compra de Cartera");
//correos.add("gabriel.rios@softtek.com");
        correo.setCorreoPara(correos);
        correo.setAdjuntos(adjuntos);
        Response response = correoClient.enviaCorreo(correo);

        if (response.getStatus() == 200 || response.getStatus() == 204) {
            log.log(Level.INFO, ">>>>>>CorreoCompraCarteraService sendEmail SE ENVIO CORREO DE AVISO={0}", response.getStatus());

        }
    }
}
