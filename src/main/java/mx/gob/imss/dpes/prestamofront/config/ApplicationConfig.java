/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.config;

/**
 *
 * @author antonio
 */
import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(mx.gob.imss.dpes.common.exception.AlternateFlowMapper.class);
        resources.add(mx.gob.imss.dpes.common.exception.BusinessMapper.class);
        resources.add(mx.gob.imss.dpes.common.rule.MontoTotalRule.class);
        resources.add(mx.gob.imss.dpes.common.rule.PagoMensualRule.class);
        resources.add(mx.gob.imss.dpes.prestamofront.assembler.BitacoraReporteAssembler.class);
        resources.add(mx.gob.imss.dpes.prestamofront.assembler.BovedaAssembler.class);
        resources.add(mx.gob.imss.dpes.prestamofront.assembler.GuardarReporteAssembler.class);
        resources.add(mx.gob.imss.dpes.prestamofront.assembler.PrestamoVigenteCompositeAssembler.class);
        resources.add(mx.gob.imss.dpes.prestamofront.assembler.PrestamosEnRecuperacionAssambler.class);
        resources.add(mx.gob.imss.dpes.prestamofront.assembler.RegistroPrestamoAssembler.class);
        resources.add(mx.gob.imss.dpes.prestamofront.assembler.SistrapAssembler.class);
        resources.add(mx.gob.imss.dpes.prestamofront.assembler.SolicitudAssembler.class);
        resources.add(mx.gob.imss.dpes.prestamofront.endpoint.AutorizarPrestamoEndPoint.class);
        resources.add(mx.gob.imss.dpes.prestamofront.endpoint.PrestamoEndPoint.class);
        resources.add(mx.gob.imss.dpes.prestamofront.endpoint.PrestamoSistrapEndPoint.class);
        resources.add(mx.gob.imss.dpes.prestamofront.endpoint.PrestamosVigentesEndPoint.class);
        resources.add(mx.gob.imss.dpes.prestamofront.endpoint.ReportesEndPoint.class);
        resources.add(mx.gob.imss.dpes.prestamofront.rules.FechaLimite.class);
        resources.add(mx.gob.imss.dpes.prestamofront.rules.ValidarMontos.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ActualizarSolicitudCapacidadService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ActualizarSolicitudService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.AutorizarReporteService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.BitacoraInterfazService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.BitacoraReporteService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.BovedaReportesService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.BovedaService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ConsultaPrestamosEnRecuperacionSistrapService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ConsultaReportesEstadisticosService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ConsultarCondicionOfertaService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ConsultarDescuentosEmitidosService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ConsultarPeriodoNominaService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ConsultarPrestamoSistrapService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ConsultarPrestamosCompraCarteraEFService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ConsultarPrestamosRecuperacionBackService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ConsultarPrestamosVigentesService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ConsultarSolicitudesVigentesService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.CorreoAutorizarService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.CorreoCompraCarteraService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.CorreoMontoLiquidarRenovacionService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.CorreoMontoLiquidarService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.CorreoService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.CrearDocumentoService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.CreateBitacoraService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.CreateCorreoReporteConsiliacionService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.CreateEventService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.CronJobAutorizarService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.CronJobService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.EfByCveSipreService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.GuardarReporteService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ObtainCEPByteFilesService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ObtenerAmortizacionService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ObtenerCartaInstruccionService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ObtenerContratoService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ObtenerIdentificacionService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.PrestamoRecuperacionCapacidad.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.PrestamoRecuperacionService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.PrestamoSIPREService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.PrestamoService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ResumenConciliacionEFService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ResumenConciliacionService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.SistrapGuardarService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.SistrapValidarService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.SolicitudService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.UpdateAutorizacionDocumentsService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.UpdateDocumentsService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.UpdateEstadoService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.UpdateEstadoSolicitudService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ValidacionService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ValidarGeneracionReporteService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.reporte.ReporteConciliacionXLSService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ActualizaEstadoSolicitudService.class);
        resources.add(mx.gob.imss.dpes.prestamofront.service.ActualizarDocumentosReinstalacionService.class);
    }
    
}
