/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.model.mclpe;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;

public class PeriodoNominaMclpeResponse  extends BaseModel{
    
    @Getter @Setter private Long id;
    @Getter @Setter private int numPeriodoNomina;
    
    @JsonDeserialize( using = CustomDateDeserializer.class )
    @Getter @Setter private Date fecInicioCaptura;
    
    @JsonDeserialize( using = CustomDateDeserializer.class )
    @Getter @Setter private Date fecFinCaptura;
     
    @JsonDeserialize( using = CustomDateDeserializer.class )
    @Getter @Setter private Date fecInicioEjecucion;
      
    @JsonDeserialize( using = CustomDateDeserializer.class )
    @Getter @Setter private Date fecFinEjecucion;
    
    @JsonDeserialize( using = CustomDateDeserializer.class )
    @Getter @Setter private Date altaRegistro;
        
    @JsonDeserialize( using = CustomDateDeserializer.class )
    @Getter @Setter private Date bajaRegistro;
    
    @Getter @Setter private Date actualizacionRegistro;   
    
}
