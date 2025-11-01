/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.interfaces.reportes.model.ConciliacionEF;
import mx.gob.imss.dpes.interfaces.reportes.model.EntidadFinancieraEncabezado;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.interfaces.reportes.model.ReporteConciliacionEF;
import mx.gob.imss.dpes.interfaces.sipre.model.DescuentoEmitido;
import mx.gob.imss.dpes.prestamofront.exception.ReporteExcepcion;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ResumenConciliacionEFService extends ServiceDefinition<Reporte, Reporte> {

    @Inject
    private EfByCveSipreService efByCveSipreService;

    static final String CONCEPTO_NOMINA_301 = "301";
    static final String CONCEPTO_NOMINA_380 = "380";
    static final String MOVIMIENTO_DA = "DA";//Descuento aplicado
    static final String MOVIMIENTO_NA = "NA";//Descuento no aplicado
    static final String MOVIMIENTO_FA = "FA";//Notificacion de fallecidos
    static final Double COSTO_ADMINISTRATIVO_I = 0.005;
    static final Double COSTO_ADMINISTRATIVO_IVA = 0.16;

    private String periodoNominal;

    private List<EntidadFinancieraEncabezado> entidadFinanciera;
    private List<ConciliacionEF> prestamosRecuperados;
    private List<ConciliacionEF> prestamosNoRecuperados;
    private List<ConciliacionEF> prestamosBajaDefuncion;

    @Override
    public Message<Reporte> execute(Message<Reporte> request) throws BusinessException {
        log.log(Level.INFO, ">>>prestamoFront|ResumenConciliacionEFService|execute {0}", request.getPayload());
        periodoNominal = request.getPayload().getAnioNominal().concat(request.getPayload().getMesNominal());
        if (request.getPayload().getDescuentosResponse().getDescuentosEmitidos() != null && request.getPayload().getDescuentosResponse().getDescuentosEmitidos().size() > 0) {
            entidadFinanciera = new ArrayList<>();
            for (DescuentoEmitido descuento : request.getPayload().getDescuentosResponse().getDescuentosEmitidos()) {
                if (descuento.getConceptoNomina().equals(CONCEPTO_NOMINA_301)) {
                    containEntidadFinanciera(descuento);
                }
            }

            for (EntidadFinancieraEncabezado ef : entidadFinanciera) {
                prestamosRecuperados = new ArrayList<>();
                prestamosNoRecuperados = new ArrayList<>();
                prestamosBajaDefuncion = new ArrayList<>();
                for (DescuentoEmitido desc : request.getPayload().getDescuentosResponse().getDescuentosEmitidos()) {
                    if (ef.getIdEFSipre().equals(desc.getIdInstFincanciera())) {
                        switch (desc.getMovimiento()) {
                            case MOVIMIENTO_DA:
                                agregarDescuentoAplicado(desc);
                                break;
                            case MOVIMIENTO_NA:
                                agregarDescuentosNoAplicados(desc);
                                break;
                            case MOVIMIENTO_FA:
                                agregarFallecidos(desc);
                                break;
                            default:
                                break;
                        }
                    }
                }
                ef.setPrestamosRecuperados(prestamosRecuperados);
                ef.setPrestamosNoRecuperados(prestamosNoRecuperados);
                ef.setPrestamosBajaDefuncion(prestamosBajaDefuncion);
            }
            ReporteConciliacionEF reporteConciliacionEF = new ReporteConciliacionEF();
            reporteConciliacionEF.setEntidadFinanciera(entidadFinanciera);
            request.getPayload().setReporteConciliacionEF(reporteConciliacionEF);
            log.log(Level.INFO, ">>>prestamoFront|ResumenConciliacionEFService|respone {0}", request.getPayload());
            return request;
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new ReporteExcepcion(), null);
    }

    private void containEntidadFinanciera(DescuentoEmitido in) throws BusinessException {

        if (entidadFinanciera != null && entidadFinanciera.size() > 0) {
            for (EntidadFinancieraEncabezado e : entidadFinanciera) {
                if (e.getIdEFSipre().equals(in.getIdInstFincanciera())) {
                    break;
                } else {
                    agregarEntidadFinanciera(in);
                }
            }
        } else {
            agregarEntidadFinanciera(in);
        }
    }

    private void agregarEntidadFinanciera(DescuentoEmitido in) throws BusinessException {

        EntidadFinanciera e = new EntidadFinanciera();
        e.setIdSipre(in.getIdInstFincanciera());
        Message<EntidadFinanciera> entidadResponse = efByCveSipreService.execute(new Message<>(e));

        EntidadFinancieraEncabezado ef = new EntidadFinancieraEncabezado();
        ef.setRazonSocial(entidadResponse.getPayload().getRazonSocial());
        ef.setNumProveedor(entidadResponse.getPayload().getNumProveedor());
        ef.setPeriodoNominal(periodoNominal);
        ef.setFechaEmision(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        ef.setRazonSocial(entidadResponse.getPayload().getRazonSocial());
        ef.setIdEFSipre(in.getIdInstFincanciera());

        entidadFinanciera.add(ef);

    }

    private void agregarDescuentoAplicado(DescuentoEmitido in) throws BusinessException {

        ConciliacionEF c = new ConciliacionEF();
        c.setNss(in.getNss());
        c.setGrupoFamiliar(in.getIdGrupoFamiliar());
        c.setFolioSolicitud(in.getIdSolPrestFinanciero());
        c.setImporteTotalPrestamo(null);
        c.setSaldo(null);
        c.setDescuentoMensual(null);
        c.setTotalMensualidades(null);
        c.setNumMensualidad(null);
        c.setConceptoDescuento(null);
        c.setImporteRecuperado(in.getImpRecuperado());
        prestamosRecuperados.add(c);

    }

    private void agregarDescuentosNoAplicados(DescuentoEmitido in) throws BusinessException {

        ConciliacionEF c = new ConciliacionEF();
        c.setNss(in.getNss());
        c.setGrupoFamiliar(in.getIdGrupoFamiliar());
        c.setDelegacion(in.getDelegacion());
        c.setContrato(null);
        c.setImporteTotalPrestamo(null);
        c.setSaldo(null);
        c.setDescuentoMensual(null);
        c.setTotalMensualidades(null);
        c.setNumMensualidad(null);
        c.setCausa(null);
        c.setCapacidadCredito(null);
        prestamosNoRecuperados.add(c);

    }

    private void agregarFallecidos(DescuentoEmitido in) throws BusinessException {

        ConciliacionEF c = new ConciliacionEF();
        c.setNss(in.getNss());
        c.setGrupoFamiliar(in.getIdGrupoFamiliar());
        c.setDelegacion(in.getDelegacion());
        c.setContrato(null);
        c.setImporteTotalPrestamo(null);
        c.setSaldo(null);
        c.setDescuentoMensual(null);
        c.setTotalMensualidades(null);
        c.setNumMensualidad(null);
        c.setImporteCobradoDemasia(null);
        c.setFechaFallecimiento(null);
        prestamosBajaDefuncion.add(c);

    }
}
