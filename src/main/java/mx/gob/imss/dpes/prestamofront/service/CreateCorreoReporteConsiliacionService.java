/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import mx.gob.imss.dpes.interfaces.reportes.model.Inconsistencia;

import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.interfaces.reportesMclp.model.Correo;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;
import mx.gob.imss.dpes.prestamofront.model.NotificacionCorreo;
import mx.gob.imss.dpes.prestamofront.model.Template;
import mx.gob.imss.dpes.prestamofront.restclient.CorreoClient;

/**
 * @author salvador.pocteco
 */
@Provider
public class CreateCorreoReporteConsiliacionService extends ServiceDefinition<Reporte, Reporte> {

    @Inject
    @RestClient
    private CorreoClient client;

    @Override
    public Message<Reporte> execute(Message<Reporte> request) throws BusinessException {

        Template template = new Template();
        NotificacionCorreo<Reporte> notificacion = new NotificacionCorreo<>();

        template.setContent("<html>\n"
                + "\n"
                + "<head>\n"
                + "    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />\n"
                + "    <title></title>\n"
                + "    <style type='text/css'>\n"
                + "        a:hover {\n"
                + "            color: #7b9cf0;\n"
                + "        }\n"
                + "\n"
                + "        .header span {\n"
                + "            color: #ffffff;\n"
                + "            font: normal 16px Helvetica, Arial;\n"
                + "            margin: 0px;\n"
                + "            padding: 0px;\n"
                + "            line-height: 16px;\n"
                + "            margin: 0px;\n"
                + "            padding: 0px;\n"
                + "            padding: 0px;\n"
                + "        }\n"
                + "\n"
                + "        .content h4 {\n"
                + "            color: #5A5A5A;\n"
                + "            margin: 0px;\n"
                + "            padding: 0px;\n"
                + "            line-height: 30px;\n"
                + "            font-size: 24px;\n"
                + "            font-family: Helvetica, Arial;\n"
                + "        }\n"
                + "\n"
                + "        .content p {\n"
                + "            color: #5A5A5A;\n"
                + "            font-weight: normal;\n"
                + "            margin: 0px;\n"
                + "            padding: 0px;\n"
                + "            line-height: 23px;\n"
                + "            font-size: 16px;\n"
                + "            font-family: Helvetica, Arial;\n"
                + "        }\n"
                + "\n"
                + "        .content .note p {\n"
                + "            color: #5A5A5A;\n"
                + "            font-weight: normal;\n"
                + "            margin: 0px;\n"
                + "            padding: 0px;\n"
                + "            font-style: italic;\n"
                + "            line-height: 20px;\n"
                + "            font-size: 12px;\n"
                + "            font-family: Helvetica, Arial;\n"
                + "        }\n"
                + "\n"
                + "        .footer a {\n"
                + "            color: #12c;\n"
                + "        }\n"
                + "\n"
                + "        .footer p {\n"
                + "            padding: 0px;\n"
                + "            font-size: 13px;\n"
                + "            color: #5A5A5A;\n"
                + "            margin: 0px;\n"
                + "            font-family: Helvetica, Arial;\n"
                + "            text-transform: uppercase;\n"
                + "            color: #5A5A5A;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "\n"
                + "<body style='margin: 0; padding: 0; background: #F3F3F3;'>\n"
                + "    <table cellpadding='0' cellspacing='0' border='0' align='center' width='100%'>\n"
                + "        <tr>\n"
                + "            <td align='center' style='margin: 0; padding: 0; background: #F3F3F3; padding: 27px 0px'>\n"
                + "                <table cellpadding='0' cellspacing='0' border='0' align='center' width='560px'\n"
                + "                    style='font-family: Helvetica, Arial; font-size: 16px; color: #ffffff; background: #621132'\n"
                + "                    class='header'>\n"
                + "                    <tr>\n"
                + "                        <td height='76px' width='50%' valign='middle' style='padding-left: 27px;'> <img width='126'\n"
                + "                                height='39' alt='gob.mx'\n"
                + "                                src='http://serviciosdigitales.imss.gob.mx/delta/resources/imagenes/gobmx/logos/gobmxlogo_g.png' />\n"
                + "                        </td>\n"
                + "                        <td height='76px' width='50%' valign='middle' align='right'><img width='246' height='81'\n"
                + "                                style='float: right; margin: 0px -20px 0px 0px;'\n"
                + "                                src='https://www.gob.mx/cms/uploads/identity/image/5131/logo_274x90_LOGO_IMSS_SOMBRA_BLANCO.png' />\n"
                + "                        </td>\n"
                + "                    </tr>\n"
                + "                </table>\n"
                + "                <table cellpadding='0' cellspacing='0' border='0' align='center' width='560px'\n"
                + "                    style='font-family: Helvetica, Arial; background: #ffffff;' bgcolor='#ffffff'>\n"
                + "                    <tr>\n"
                + "                        <td width='560px;' valign='top' align='left' bgcolor='#ffffff'\n"
                + "                            style='font-family: Helvetica, Arial; font-size: 16px; color: #5A5A5A; background: #fff; padding: 38px 27px 76px;'>\n"
                + "                            <table cellpadding='0' cellspacing='0' border='0'\n"
                + "                                style='color: #717171; font: normal 16px Helvetica, Arial; margin: 0px; padding: 0;'\n"
                + "                                width='100%' class='content'>\n"
                + "                                <tr>\n"
                + "                                    <td>\n"
                + "                                        <p> Estimado(a) usuario(a): </p><br>\n"
                + "                                        <p\n"
                + "                                            style='text-align: justify; color: #5A5A5A; font-weight: normal; margin: 0px; padding: 0px; line-height: 23px; font-size: 18px; font-family: Helvetica, Arial;'>\n"
                + "                                            Le notificamos que la generación del reporte de\n"
                + "Conciliación IMSS ha presentado NUMERO_INCONSISTENCIAS errores.</p><br> \n"
                + "MENSAJE_SI_HAY_INCONSISTENCIAS"
                + "                                        <p style='text-align: justify; color: #5A5A5A; font-weight: normal; margin: 0px; padding: 0px; line-height: 23px; font-size: 18px; font-family: Helvetica, Arial;'>\n"
                + "                                            Atentamente<br> Instituto Mexicano del Seguro Social<br> Préstamos a\n"
                + "                                            pensionados con Entidades Financieras</p>\n"
                + "                                    </td>\n"
                + "                                </tr>\n"
                + "                                <tr>\n"
                + "                                    <td align='center'>\n"
                + "                                        <table class='nssDataWrapper'\n"
                + "                                            style='clear: both; font-size: 12px !important; margin-bottom: 6px !important; max-width: none !important;'>\n"
                + "                                        </table>\n"
                + "                                    </td>\n"
                + "                                </tr>\n"
                + "                            </table>\n"
                + "                        </td>\n"
                + "                    </tr>\n"
                + "                </table>\n"
                + "                <table cellpadding='0' cellspacing='0' border='0' align='center' width='560px'\n"
                + "                    style='font-family: Helvetica, Arial; font-size: 16px; color: #ffffff; background: #221e1e'\n"
                + "                    class='mainFooter'>\n"
                + "                    <tr>\n"
                + "                        <td align='left' height='76px' width='50%' valign='middle' style='padding-left: 27px;'><img\n"
                + "                                width='63' height='19' alt='gob.mx'\n"
                + "                                src='http://serviciosdigitales.imss.gob.mx/delta/resources/imagenes/gobmx/logos/gobmxlogo_g.png' />\n"
                + "                        </td>\n"
                + "                    </tr>\n"
                + "                </table>\n"
                + "            </td>\n"
                + "        </tr>\n"
                + "    </table>\n"
                + "</body>\n"
                + "\n"
                + "</html>");
        template.setName("/template/correo1.html");

        String template2 = template.getContent();

        for (Correo c : request.getPayload().getReporteRs().getCorreos()) {
            notificacion.getCorreo().getCorreoPara().add(
                    c.getCorreo()
            );
        }

        notificacion.getCorreo().setAsunto("Reporte de inconsistencias en Conciliación IMSS");
        if (request.getPayload().getTipoReporte() == 1) {

            if (request.getPayload().getReporteConciliacion().getInconsistenciasList() == null) {
                List<Inconsistencia> lst = new ArrayList<>();
                request.getPayload().getReporteConciliacion().setInconsistenciasList(lst);
            }
            template2 = template2.replaceAll("NUMERO_INCONSISTENCIAS", Integer.toString(request.getPayload().getReporteConciliacion().getInconsistenciasList().size()));

            if (!request.getPayload().getReporteConciliacion().getInconsistenciasList().isEmpty()) {
                template2 = template2.replaceAll("MENSAJE_SI_HAY_INCONSISTENCIAS",
                        "<p style='text-align: justify; color: #5A5A5A; font-weight: normal; margin: 0px; "
                        + "padding: 0px; line-height: 23px; font-size: 18px; font-family: Helvetica, Arial;'>"
                        + "Se anexa el Reporte de Inconsistencias para su conocimiento y atención."
                        + "</p><br>");

                List<Adjunto> adjuntos = new ArrayList<>();
                Adjunto adjunto = new Adjunto();

                adjunto.setNombreAdjunto("Inconsistencias.txt");
                byte[] decodedBytes = Base64.getDecoder().decode(request.getPayload().getReporteConciliacion().getArchivoTxtBase64());
                adjunto.setAdjuntoBase64(
                        decodedBytes
                );

                adjuntos.add(adjunto);

                notificacion.getCorreo().setAdjuntos(adjuntos);

            } else {
                template2 = template2.replaceAll("MENSAJE_SI_HAY_INCONSISTENCIAS",
                        "");
            }
        }
        notificacion.getCorreo().setCuerpoCorreo(template2);

        Response respuesta = client.create(notificacion.getCorreo());

        if (respuesta.getStatus() == 200) {

            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }
}
