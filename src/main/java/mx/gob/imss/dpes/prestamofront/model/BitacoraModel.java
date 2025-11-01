/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;
import mx.gob.imss.dpes.support.config.CustomDateSerializer;

/**
 *
 * @author eduardo.loyo
 */
@Data
public class BitacoraModel extends BaseModel {
    private Long id;
    @JsonDeserialize( using = CustomDateDeserializer.class )
  @JsonSerialize(using = CustomDateSerializer.class)
    private Date altaRegistro;
    @JsonDeserialize( using = CustomDateDeserializer.class )
  @JsonSerialize(using = CustomDateSerializer.class)
    private Date bajaRegistro;
    @JsonDeserialize( using = CustomDateDeserializer.class )
  @JsonSerialize(using = CustomDateSerializer.class)
    private Date actualizacionRegistro;
    private String sesion;
    private Long idSolicitud;
    private String curp;
    private Long tipo;
    private Long estadoSolicitud;
    private String descripcion;
}
