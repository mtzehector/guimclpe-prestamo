/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;
import mx.gob.imss.dpes.support.config.CustomDateSerializer;

/**
 *
 * @author juan.garfias
 */
public class PrestamosAutorizadosEFRQ extends BaseModel {

    @Getter
    @Setter
    private String fechaInicio;
    @Getter
    @Setter
    private String fechaFin;

    public PrestamosAutorizadosEFRQ() {
    }

    public PrestamosAutorizadosEFRQ(String fechaInicio, String fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

}
