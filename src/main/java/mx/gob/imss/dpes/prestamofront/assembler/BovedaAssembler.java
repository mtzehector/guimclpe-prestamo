/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.assembler;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.prestamofront.model.BovedaRequest;
import mx.gob.imss.dpes.prestamofront.model.Tramite;
import mx.gob.imss.dpes.prestamofront.model.Usuario;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class BovedaAssembler extends BaseAssembler<Reporte, BovedaRequest> {

    private String dateFormat = "yyMMddHHmmss";

    @Override
    public BovedaRequest assemble(Reporte source) {

        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String tm = formatter.format(ts);

        BovedaRequest bovedaIn = new BovedaRequest();
        bovedaIn.setUsuario(new Usuario());
        bovedaIn.getUsuario().setIdUsr("1");
        bovedaIn.setTramite(new Tramite());
        //int numero = (int)(Math. random()*200+1);
        //bovedaIn.getTramite().setFolioTramite(String.valueOf(numero));
        bovedaIn.getTramite().setFolioTramite(source.getAnioNominal().substring(2, 4).concat(source.getMesNominal()) + source.getTipoReporte().toString() + tm);
        bovedaIn.setDocumento(new mx.gob.imss.dpes.prestamofront.model.Documento());
        bovedaIn.getDocumento().setNombreArchivo(TipoDocumentoEnum.REPORTE_CONCILIACION_XLSX.getDescripcion());
        bovedaIn.getDocumento().setExtencion("." + TipoDocumentoEnum.REPORTE_CONCILIACION_XLSX.getMedia());
        bovedaIn.getDocumento().setArchivo(Base64.decodeBase64(source.getReporteConciliacion().getArchivoXLSXBase64().getBytes()));
        bovedaIn.setSesion(source.getSesion());
        log.log(Level.INFO, ">>>prestamoFront|BovedaAssembler|assemble {0}", bovedaIn);
        return bovedaIn;
    }

    public BovedaRequest assembleTxt(Reporte source) {

        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String tm = formatter.format(ts);

        BovedaRequest bovedaIn = new BovedaRequest();
        bovedaIn.setUsuario(new Usuario());
        bovedaIn.getUsuario().setIdUsr("1");
        bovedaIn.setTramite(new Tramite());
//        int numero = (int)(Math. random()*200+1);
//        bovedaIn.getTramite().setFolioTramite(String.valueOf(numero));
        bovedaIn.getTramite().setFolioTramite(source.getAnioNominal().substring(2, 4).concat(source.getMesNominal()) + source.getTipoReporte().toString() + tm);
        bovedaIn.setDocumento(new mx.gob.imss.dpes.prestamofront.model.Documento());
        bovedaIn.getDocumento().setNombreArchivo(TipoDocumentoEnum.REPORTE_INCONSISTENCIAS.getDescripcion());
        bovedaIn.getDocumento().setExtencion("." + TipoDocumentoEnum.REPORTE_INCONSISTENCIAS.getMedia());
        bovedaIn.getDocumento().setArchivo(Base64.decodeBase64(source.getReporteConciliacion().getArchivoTxtBase64().getBytes()));
        bovedaIn.setSesion(source.getSesion());
        log.log(Level.INFO, ">>>prestamoFront|BovedaAssembler|assembleTxt {0}", bovedaIn);
        return bovedaIn;
    }

    public BovedaRequest assembleXLSX(String uid, String file, Long tipoDocumento, Long sesion) {

        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String tm = formatter.format(ts);

        BovedaRequest bovedaIn = new BovedaRequest();
        bovedaIn.setUsuario(new Usuario());
        bovedaIn.getUsuario().setIdUsr("1");
        bovedaIn.setTramite(new Tramite());
        //int numero = (int)(Math. random()*200+1);
        //bovedaIn.getTramite().setFolioTramite(String.valueOf(numero));
        bovedaIn.getTramite().setFolioTramite(uid + tm);
        bovedaIn.setDocumento(new mx.gob.imss.dpes.prestamofront.model.Documento());
        bovedaIn.getDocumento().setNombreArchivo(TipoDocumentoEnum.forValue(tipoDocumento).getDescripcion());
        bovedaIn.getDocumento().setExtencion("." + TipoDocumentoEnum.forValue(tipoDocumento).getMedia());
        bovedaIn.getDocumento().setArchivo(Base64.decodeBase64(file.getBytes()));
        bovedaIn.setSesion(sesion);
        log.log(Level.INFO, ">>>prestamoFront|BovedaAssembler|assembleXLSX {0}", bovedaIn);
        return bovedaIn;
    }

    public BovedaRequest assembleTxt(String uid, String file, Long tipoDocumento, Long sesion) {

        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String tm = formatter.format(ts);

        BovedaRequest bovedaIn = new BovedaRequest();
        bovedaIn.setUsuario(new Usuario());
        bovedaIn.getUsuario().setIdUsr("1");
        bovedaIn.setTramite(new Tramite());
//        int numero = (int)(Math. random()*200+1);
//        bovedaIn.getTramite().setFolioTramite(String.valueOf(numero));
        bovedaIn.getTramite().setFolioTramite(uid + tm);
        bovedaIn.setDocumento(new mx.gob.imss.dpes.prestamofront.model.Documento());
        bovedaIn.getDocumento().setNombreArchivo(TipoDocumentoEnum.forValue(tipoDocumento).getDescripcion());
        bovedaIn.getDocumento().setExtencion("." + TipoDocumentoEnum.forValue(tipoDocumento).getMedia());
        bovedaIn.getDocumento().setArchivo(Base64.decodeBase64(file.getBytes()));
        bovedaIn.setSesion(sesion);
        log.log(Level.INFO, ">>>prestamoFront|BovedaAssembler|assembleTxt {0}", bovedaIn);
        return bovedaIn;
    }

    public BovedaRequest assembleReporteComprasCarteraEFXLSX(Reporte source) {

        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String tm = formatter.format(ts);

        BovedaRequest bovedaIn = new BovedaRequest();
        bovedaIn.setUsuario(new Usuario());
        bovedaIn.getUsuario().setIdUsr("1");
        bovedaIn.setTramite(new Tramite());
        //int numero = (int)(Math. random()*200+1);
        //bovedaIn.getTramite().setFolioTramite(String.valueOf(numero));
        bovedaIn.getTramite().setFolioTramite(source.getAnioNominal().substring(2, 4).concat(source.getMesNominal()) + source.getTipoReporte().toString() + tm);
        bovedaIn.setDocumento(new mx.gob.imss.dpes.prestamofront.model.Documento());
        bovedaIn.getDocumento().setNombreArchivo(TipoDocumentoEnum.REPORTE_COMPRAS_CARTERA_EF.getDescripcion());
        bovedaIn.getDocumento().setExtencion("." + TipoDocumentoEnum.REPORTE_COMPRAS_CARTERA_EF.getMedia());
        bovedaIn.getDocumento().setArchivo(Base64.decodeBase64(source.getReporteCompraCarteraEF().getArchivoXLSX().getBytes()));
        bovedaIn.setSesion(source.getSesion());
        log.log(Level.INFO, ">>>prestamoFront|BovedaAssembler|assemble {0}", bovedaIn);
        return bovedaIn;
    }
}
