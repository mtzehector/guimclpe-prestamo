/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.model;
import lombok.Getter;
import lombok.Setter;
/**
 *
 * @author antonio
 */
public class PrestamoVigente {
  @Getter @Setter private String folio;
  @Getter @Setter private String entidadFinanciera;
  @Getter @Setter private String cat;
  @Getter @Setter private String montoSolicitado;
  @Getter @Setter private String descuentoMensual;
  @Getter @Setter private String mensualidadesDescontadas;
}
