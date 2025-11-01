/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author edgar.arenas
 */
public class CorreoAutorizacion extends BaseModel {

    @Getter
    @Setter
    private List<AdjuntoAutorizacion> adjuntos = new ArrayList<>();
    @Getter
    @Setter
    private List<String> correoPara = new ArrayList<>();
    @Getter
    @Setter
    private String asunto;
    @Getter
    @Setter
    private String remitente;
    @Getter
    @Setter
    private String cuerpoCorreo;
}
