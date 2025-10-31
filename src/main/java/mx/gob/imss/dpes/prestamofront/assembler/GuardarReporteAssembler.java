/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.common.enums.TipoReporteEnum;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.interfaces.reportesMclp.model.EstadoReporte;
import mx.gob.imss.dpes.interfaces.reportesMclp.model.ReporteDocumento;
import mx.gob.imss.dpes.interfaces.reportesMclp.model.ReporteRq;
import mx.gob.imss.dpes.interfaces.reportesMclp.model.SubTipoReporte;
import mx.gob.imss.dpes.interfaces.reportesMclp.model.TipoReporte;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class GuardarReporteAssembler extends BaseAssembler<Reporte, ReporteRq> {

    @Override
    public ReporteRq assemble(Reporte source) {

        ReporteRq reporteRQ = new ReporteRq();
        mx.gob.imss.dpes.interfaces.reportesMclp.model.Reporte reporte = new mx.gob.imss.dpes.interfaces.reportesMclp.model.Reporte();
        reporte.setEstadoReporte(new EstadoReporte());
        reporte.getEstadoReporte().setId(1L);//GENERADO
        reporte.setPeriodoNomina(source.getAnioNominal().concat(source.getMesNominal()));
        reporte.setSubTipoReporte(new SubTipoReporte());
        reporte.getSubTipoReporte().setId(0L);//NO APLICA
        reporte.setTipoReporte(new TipoReporte());
        reporte.getTipoReporte().setId(
                source.getTipoReporte()
        );
        if (source.getCveEntidadFinanciera() != null) {
            reporte.setCveEntidadFinanciera(Long.parseLong(source.getCveEntidadFinanciera()));
        }
        reporteRQ.setReporte(reporte);
        List<ReporteDocumento> docList = new ArrayList<>();

        if (source.getTipoReporte() == 1) {
            ReporteDocumento docConciliacion = new ReporteDocumento();
            docConciliacion.setDocumento(new Documento());
            docConciliacion.getDocumento().setId(source.getDocConciliacion().getId());
            docList.add(docConciliacion);
            ReporteDocumento docInconsistencias = new ReporteDocumento();
            docInconsistencias.setDocumento(new Documento());
            docInconsistencias.getDocumento().setId(source.getDocInconsistencias().getId());
            docList.add(docInconsistencias);

        }
        if (source.getTipoReporte() == 3) {
            ReporteDocumento doc1 = new ReporteDocumento();
            doc1.setDocumento(new Documento());
            doc1.getDocumento().setId(source.getDocDescEmitByDel().getId());
            docList.add(doc1);
            ReporteDocumento doc2 = new ReporteDocumento();
            doc2.setDocumento(new Documento());
            doc2.getDocumento().setId(source.getDocDescEmitByEF().getId());
            docList.add(doc2);
            ReporteDocumento doc3 = new ReporteDocumento();
            doc3.setDocumento(new Documento());
            doc3.getDocumento().setId(source.getDocInconsDescuentos().getId());
            docList.add(doc3);
            ReporteDocumento doc4 = new ReporteDocumento();
            doc4.setDocumento(new Documento());
            doc4.getDocumento().setId(source.getDocInconsPrestamos().getId());
            docList.add(doc4);

        }

        if (source.getTipoReporte() == 4) {
            ReporteDocumento reporte4 = new ReporteDocumento();
            reporte4.setDocumento(new Documento());
            reporte4.getDocumento().setId(source.getDocComprasDeCarteraEF().getId());
            docList.add(reporte4);
        }
        reporteRQ.setReporteDocumentos(docList);
        log.log(Level.INFO, ">>>prestamoFront|GuardarReporteAssembler|assemble {0}", reporteRQ);
        return reporteRQ;
    }
}
