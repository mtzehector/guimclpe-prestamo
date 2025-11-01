/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service.reporte.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import mx.gob.imss.dpes.interfaces.reportes.model.Conciliacion;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
import mx.gob.imss.dpes.interfaces.reportes.model.ReporteConciliacion;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.util.Log;

/**
 *
 * @author juan.garfias
 */
public class GenerarExcelConsiliacion {
        public String generaXLS(Reporte rc) throws FileNotFoundException, IOException {

        String stringXLS = "";

        Date date = new Date();
        //Caso 2: obtener la fecha y salida por pantalla con formato:
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //System.out.println("Fecha: " + dateFormat.format(date));

        // Creamos el archivo donde almacenaremos la hoja
        // de calculo, recuerde usar la extension correcta,
        // en este caso .xlsx
        //File archivo = new File("C:\\opt\\work\\reporte_test.xlsx");
        // Creamos el libro de trabajo de Excel formato OOXML
        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();

        Font font_10 = workbook.createFont();
        font_10.setFontHeightInPoints((short) 10);
        font_10.setFontName("Montserrat Regular");
        font_10.setItalic(false);
        font_10.setStrikeout(false);

        Font font_11 = workbook.createFont();
        font_11.setFontHeightInPoints((short) 11);
        font_11.setFontName("Montserrat Regular");
        font_11.setItalic(false);
        font_11.setStrikeout(false);

        Font font_12 = workbook.createFont();
        font_12.setFontHeightInPoints((short) 12);
        font_12.setFontName("Montserrat Regular");
        font_12.setItalic(false);
        font_12.setStrikeout(false);

        Font font_14 = workbook.createFont();
        font_14.setFontHeightInPoints((short) 14);
        font_14.setFontName("Montserrat Regular");
        font_14.setItalic(false);
        font_14.setStrikeout(false);
        // Applying font to the style  
        //style.setFont(font);  

        // La hoja donde pondremos los datos
        Sheet pagina = workbook.createSheet("Reporte de productos");

        // Creamos el estilo paga las celdas del encabezado
        CellStyle style = workbook.createCellStyle();
        CellStyle styleCurrency = workbook.createCellStyle();
        styleCurrency.setDataFormat(createHelper.createDataFormat().getFormat("$#,##0.00"));
        styleCurrency.setFont(font_10);
        CellStyle styleMiles = workbook.createCellStyle();
        styleMiles.setDataFormat(createHelper.createDataFormat().getFormat("#,##0"));
        styleMiles.setFont(font_10);
        CellStyle styleString = workbook.createCellStyle();
        styleString.setFont(font_10);

        styleString.setBorderBottom(BorderStyle.THIN);
        styleString.setBorderTop(BorderStyle.THIN);

        styleMiles.setBorderBottom(BorderStyle.THIN);
        styleMiles.setBorderTop(BorderStyle.THIN);

        styleCurrency.setBorderBottom(BorderStyle.THIN);
        styleCurrency.setBorderTop(BorderStyle.THIN);

        // Indicamos que tendra un fondo azul aqua
        // con patron solido del color indicado
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        style.setFont(font_11);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);

        CellStyle styleH0 = workbook.createCellStyle();
        styleH0.setVerticalAlignment(VerticalAlignment.CENTER);
        styleH0.setAlignment(HorizontalAlignment.RIGHT);
        styleH0.setWrapText(true);
        styleH0.setFont(font_10);

        CellStyle styleH1 = workbook.createCellStyle();
        styleH1.setVerticalAlignment(VerticalAlignment.CENTER);
        styleH1.setAlignment(HorizontalAlignment.CENTER);
        styleH1.setWrapText(true);
        styleH1.setFont(font_14);

        CellStyle styleH2 = workbook.createCellStyle();
        styleH2.setVerticalAlignment(VerticalAlignment.CENTER);
        styleH2.setAlignment(HorizontalAlignment.CENTER);
        styleH2.setWrapText(true);
        styleH2.setFont(font_12);

        HashMap<Integer, String> mapTitulos = new HashMap<>();
        mapTitulos.put(0, "Entidad Financiera");
        mapTitulos.put(1, "");
        mapTitulos.put(2, "");
        mapTitulos.put(3, "");
        mapTitulos.put(4, "");
        mapTitulos.put(5, "Descuentos y Reposiciones Emitidas");
        mapTitulos.put(6, "");
        mapTitulos.put(7, "Descuentos y Reposiciones Pagados.");
        mapTitulos.put(8, "");
        mapTitulos.put(9, "No pagado");
        mapTitulos.put(10, "");
        mapTitulos.put(11, "Total de Retenciones");
        mapTitulos.put(12, "Costo Administrativo");
        mapTitulos.put(13, "");
        mapTitulos.put(14, "Neto sin descuento Fallecidos");
        mapTitulos.put(15, "Fallecidos");
        mapTitulos.put(16, "");
        mapTitulos.put(17, "");
        mapTitulos.put(18, "Inconsistencias");
        mapTitulos.put(19, "");

        pagina.addMergedRegion(CellRangeAddress.valueOf("A1:J1"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("A3:J3"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("A4:E4"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("G4:J4"));

        pagina.addMergedRegion(CellRangeAddress.valueOf("A5:E5"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("F5:G5"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("H5:I5"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("J5:K5"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("M5:N5"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("P5:Q5"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("S5:T5"));

        Integer filaReporte = 0;
        // Creamos una fila en la hoja en la posicion filaReporte
        Row filaH0 = pagina.createRow(filaReporte);

        Cell celdaH0 = filaH0.createCell(0);

        // Indicamos el estilo que deseamos 
        // usar en la celda, en este caso el unico 
        // que hemos creado
        celdaH0.setCellStyle(styleH0);
        celdaH0.setCellValue("Dirección de Prestaciones Económicas y Sociales\n"
                + "Coordinación de Prestaciones Económicas\n"
                + "División de Pensiones\n"
                + "Préstamos a cuenta de pensión con Entidades Financieras");

        /*
        int pictureureIdx;
        InputStream inputStream = getClass().getResourceAsStream("/logoimss_gobierno.png");
        byte[] imageBytes = IOUtils.toByteArray(inputStream);
        pictureureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);

        //close the input stream
        inputStream.close();
        CreationHelper helper = workbook.getCreationHelper();

        Drawing drawing = pagina.createDrawingPatriarch();

        ClientAnchor anchor = helper.createClientAnchor();

        anchor.setCol1(0);
        anchor.setRow1(1);

        drawing.createPicture(anchor, pictureureIdx);

        Cell cell = pagina.createRow(1).createCell(0);
         */
        Integer w0 = 1600;
        filaH0.setHeight(w0.shortValue());
        filaReporte++;
        Row filaH1 = pagina.createRow(filaReporte);
        filaReporte++;
        Row filaH2 = pagina.createRow(filaReporte);
        Integer w3 = 750;
        filaH2.setHeight(w3.shortValue());

        Cell celdaH2_1 = filaH2.createCell(0);
        celdaH2_1.setCellStyle(styleH1);
        celdaH2_1.setCellValue("Reporte de Conciliación");

        filaReporte++;
        Row filaH3 = pagina.createRow(filaReporte);
        Integer w2 = 750;
        filaH3.setHeight(w2.shortValue());

        Cell celdaH3_1 = filaH3.createCell(0);
        celdaH3_1.setCellStyle(styleH2);
        celdaH3_1.setCellValue("Nómina: " + rc.getMesNominal() + "/" + rc.getAnioNominal());

        Cell celdaH3_2 = filaH3.createCell(6);
        celdaH3_2.setCellStyle(styleH2);
        celdaH3_2.setCellValue("Fecha de emisión: " + dateFormat.format(date));

        filaReporte++;
        Row fila = pagina.createRow(filaReporte);

        for (Map.Entry m : mapTitulos.entrySet()) {
            System.out.println(m.getKey() + " " + m.getValue());
            // Creamos el encabezado
            // Creamos una celda en esa fila, en la posicion 
            // indicada por el contador del ciclo
            Cell celda = fila.createCell(Integer.parseInt(m.getKey().toString()));

            // Indicamos el estilo que deseamos 
            // usar en la celda, en este caso el unico 
            // que hemos creado
            celda.setCellStyle(style);
            celda.setCellValue(m.getValue().toString());

        }
        filaReporte++;
        Row fila2 = pagina.createRow(filaReporte);
        String[] titulos2 = {
            "No.",
            "Id",
            "Razón Social",
            "No.Prov.",
            "Producto",
            "Casos",
            "Importe",
            "Casos",
            "Importe",
            "Casos",
            "Importe",
            "",
            "l (0.5%)",
            "IVA (16%)",
            "Importe",
            "Casos",
            "Importe",
            "Pago Real",
            "Casos",
            "Importe"
        };

        // Creamos el encabezado
        for (int i = 0; i < titulos2.length; i++) {
            // Creamos una celda en esa fila, en la posicion 
            // indicada por el contador del ciclo
            Cell celda = fila2.createCell(i);

            // Indicamos el estilo que deseamos 
            // usar en la celda, en este caso el unico 
            // que hemos creado
            celda.setCellStyle(style);
            celda.setCellValue(titulos2[i]);
        }

        pagina.setColumnWidth(2, 7000);
        pagina.setColumnWidth(4, 7000);
        Integer w = 4500;
        Integer i = 5;
        pagina.setColumnWidth(i, w);
        i++;
        pagina.setColumnWidth(i, w);
        i++;
        pagina.setColumnWidth(i, w);
        i++;
        pagina.setColumnWidth(i, w);
        i++;
        pagina.setColumnWidth(i, w);
        i++;
        pagina.setColumnWidth(i, w);
        i++;
        pagina.setColumnWidth(i, w);
        i++;
        pagina.setColumnWidth(i, w);
        i++;
        pagina.setColumnWidth(i, w);
        i++;
        pagina.setColumnWidth(i, w);
        i++;
        pagina.setColumnWidth(i, w);
        i++;
        pagina.setColumnWidth(i, w);
        i++;
        pagina.setColumnWidth(i, w);
        i++;
        pagina.setColumnWidth(i, w);
        i++;
        pagina.setColumnWidth(i, w);
        i++;

        filaReporte++;
        Integer contador = 1;
        for (Conciliacion c : rc.getReporteConciliacion().getLstConciliaciones()) {
            // Ahora creamos una fila en la posicion 1
            fila = pagina.createRow(filaReporte);

            Cell celda0 = fila.createCell(0, CellType.NUMERIC);
            celda0.setCellValue(contador);
            celda0.setCellStyle(styleString);
            contador++;
            // Y colocamos los datos en esa fila
            // Creamos una celda en esa fila, en la
            // posicion indicada por el contador del ciclo
            Integer col = 1;
            Cell celda1 = fila.createCell(col, CellType.STRING);
            celda1.setCellValue(c.getId_EF());
            celda1.setCellStyle(styleString);

            col++;
            Cell celda2 = fila.createCell(col, CellType.STRING);
            if (c.getRazonSocial_EF() == null) {
                celda2.setCellValue("");
            } else {
                celda2.setCellValue(c.getRazonSocial_EF());
            }
            celda2.setCellStyle(styleString);

            col++;
            Cell celda3 = fila.createCell(col, CellType.STRING);
            if (c.getNoProv_EF() == null) {
                celda3.setCellValue("");
            } else {
                celda3.setCellValue(c.getNoProv_EF());
            }
            celda3.setCellStyle(styleString);

            col++;
            Cell celda4 = fila.createCell(col, CellType.STRING);
            celda4.setCellValue(c.getProducto_EF());
            celda4.setCellStyle(styleString);

            col++;
            Cell celda5 = fila.createCell(col, CellType.NUMERIC);
            celda5.setCellValue(c.getDescYRepoEmit_casos());
            celda5.setCellStyle(styleMiles);

            col++;
            Cell celda6 = fila.createCell(col, CellType.NUMERIC);
            celda6.setCellValue(c.getDescYRepoEmit_importe());
            celda6.setCellStyle(styleCurrency);

            col++;
            Cell celda7 = fila.createCell(col, CellType.NUMERIC);
            celda7.setCellValue(c.getDescYRepoPagados_casos());
            celda7.setCellStyle(styleMiles);

            col++;
            Cell celda8 = fila.createCell(col, CellType.NUMERIC);
            celda8.setCellValue(c.getDescYRepoPagados_importe());
            celda8.setCellStyle(styleCurrency);

            col++;
            Cell celda9 = fila.createCell(col, CellType.NUMERIC);
            celda9.setCellValue(c.getNoPagado_casos());
            celda9.setCellStyle(styleMiles);

            col++;
            Cell celda10 = fila.createCell(col, CellType.NUMERIC);
            celda10.setCellValue(c.getNoPagado_importe());
            celda10.setCellStyle(styleCurrency);

            col++;
            Cell celda11 = fila.createCell(col, CellType.NUMERIC);
            celda11.setCellValue(c.getTotalRetenciones());
            celda11.setCellStyle(styleCurrency);

            col++;
            Cell celda12 = fila.createCell(col, CellType.NUMERIC);
            celda12.setCellValue(c.getCostoAdmin_i());
            celda12.setCellStyle(styleCurrency);

            col++;
            Cell celda13 = fila.createCell(col, CellType.NUMERIC);
            celda13.setCellValue(c.getCostoAdmin_iva());
            celda13.setCellStyle(styleCurrency);

            col++;
            Cell celda14 = fila.createCell(col, CellType.NUMERIC);
            celda14.setCellValue(c.getNetoSinDescFallecidos_importe());
            celda14.setCellStyle(styleCurrency);

            col++;
            Cell celda15 = fila.createCell(col, CellType.NUMERIC);
            celda15.setCellValue(c.getFallecidos_casos());
            celda15.setCellStyle(styleMiles);

            col++;
            Cell celda16 = fila.createCell(col, CellType.NUMERIC);
            celda16.setCellValue(c.getFallecidos_importe());
            celda16.setCellStyle(styleCurrency);

            col++;
            Cell celda17 = fila.createCell(col, CellType.NUMERIC);
            celda17.setCellValue(c.getPagoReal());
            celda17.setCellStyle(styleCurrency);

            col++;
            Cell celda18 = fila.createCell(col, CellType.NUMERIC);
            celda18.setCellValue(c.getInconsistencias_casos());
            celda18.setCellStyle(styleMiles);

            col++;
            Cell celda19 = fila.createCell(col, CellType.NUMERIC);
            celda19.setCellValue(c.getInconsistencias_importe());
            celda19.setCellStyle(styleCurrency);

            filaReporte++;
        }
        fila = pagina.createRow(filaReporte);

        Integer col = 5;

        Cell celda5 = fila.createCell(col, CellType.NUMERIC);
        celda5.setCellValue(rc.getReporteConciliacion().getTotal_descYRepoEmit_casos());
        celda5.setCellStyle(styleMiles);
        col++;
        Cell celda6 = fila.createCell(col, CellType.NUMERIC);
        celda6.setCellValue(rc.getReporteConciliacion().getTotal_descYRepoEmit_importe());
        celda6.setCellStyle(styleCurrency);
        col++;
        Cell celda7 = fila.createCell(col, CellType.NUMERIC);
        celda7.setCellValue(rc.getReporteConciliacion().getTotal_descYRepoPagados_casos());
        celda7.setCellStyle(styleMiles);
        col++;
        Cell celda8 = fila.createCell(col, CellType.NUMERIC);
        celda8.setCellValue(rc.getReporteConciliacion().getTotal_descYRepoPagados_importe());
        celda8.setCellStyle(styleCurrency);
        col++;
        Cell celda9 = fila.createCell(col, CellType.NUMERIC);
        celda9.setCellValue(rc.getReporteConciliacion().getTotal_noPagado_casos());
        celda9.setCellStyle(styleMiles);
        col++;
        Cell celda10 = fila.createCell(col, CellType.NUMERIC);
        celda10.setCellValue(rc.getReporteConciliacion().getTotal_noPagado_importe());
        celda10.setCellStyle(styleCurrency);
        col++;
        Cell celda11 = fila.createCell(col, CellType.NUMERIC);
        celda11.setCellValue(rc.getReporteConciliacion().getTotal_totalRetenciones());
        celda11.setCellStyle(styleCurrency);
        col++;
        Cell celda12 = fila.createCell(col, CellType.NUMERIC);
        celda12.setCellValue(rc.getReporteConciliacion().getTotal_costoAdmin_i());
        celda12.setCellStyle(styleCurrency);
        col++;
        Cell celda13 = fila.createCell(col, CellType.NUMERIC);
        celda13.setCellValue(rc.getReporteConciliacion().getTotal_costoAdmin_iva());
        celda13.setCellStyle(styleCurrency);
        col++;
        Cell celda14 = fila.createCell(col, CellType.NUMERIC);
        celda14.setCellValue(rc.getReporteConciliacion().getTotal_netoSinDescFallecidos_importe());
        celda14.setCellStyle(styleCurrency);
        col++;
        Cell celda15 = fila.createCell(col, CellType.NUMERIC);
        celda15.setCellValue(rc.getReporteConciliacion().getTotal_fallecidos_casos());
        celda15.setCellStyle(styleMiles);
        col++;
        Cell celda16 = fila.createCell(col, CellType.NUMERIC);
        celda16.setCellValue(rc.getReporteConciliacion().getTotal_fallecidos_importe());
        celda16.setCellStyle(styleCurrency);
        col++;
        Cell celda17 = fila.createCell(col, CellType.NUMERIC);
        celda17.setCellValue(rc.getReporteConciliacion().getTotal_pagoReal());
        celda17.setCellStyle(styleCurrency);
        col++;
        Cell celda18 = fila.createCell(col, CellType.NUMERIC);
        celda18.setCellValue(rc.getReporteConciliacion().getTotal_inconsistencias_casos());
        celda18.setCellStyle(styleMiles);
        col++;
        Cell celda19 = fila.createCell(col, CellType.NUMERIC);
        celda19.setCellValue(rc.getReporteConciliacion().getTotal_inconsistencias_importe());
        celda19.setCellStyle(styleCurrency);
        // Ahora guardaremos el archivo
        try {
            // Creamos el flujo de salida de datos,
            // apuntando al archivo donde queremos 
            // almacenar el libro de Excel
            //FileOutputStream salida = new FileOutputStream(archivo);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // Almacenamos el libro de 
            // Excel via ese 
            // flujo de datos
            workbook.write(out);
            stringXLS = Base64.encodeBase64String(out.toByteArray());
            // Cerramos el libro para concluir operaciones
            workbook.close();

            //log.log(Level.INFO, "Archivo creado existosamente en {0}", archivo.getAbsolutePath());
            //log.log(Level.INFO, "Archivo XLSX creado existosamente en : {0}", stringXLS);
        } catch (FileNotFoundException ex) {
            Log.info("Archivo no localizable en sistema de archivos");
        } catch (IOException ex) {
            Log.info("Error de entrada/salida");
        }

        return stringXLS;
    }
        public ReporteConciliacion testXLS() {

        ReporteConciliacion rc = new ReporteConciliacion();

        rc.setTotal_costoAdmin_i(123.33);
        rc.setTotal_costoAdmin_iva(123.33);
        rc.setTotal_descYRepoEmit_casos(567);
        rc.setTotal_descYRepoPagados_casos(234);
        rc.setTotal_descYRepoPagados_importe(34563.34);
        rc.setTotal_descYRepoEmit_importe(1234.432);
        rc.setTotal_fallecidos_casos(2452);
        rc.setTotal_fallecidos_importe(1245.324);
        rc.setTotal_inconsistencias_casos(24534);
        rc.setTotal_inconsistencias_importe(2345.234);
        rc.setTotal_netoSinDescFallecidos_importe(1234.234);
        rc.setTotal_noPagado_casos(73563);
        rc.setTotal_noPagado_importe(23568.123);
        rc.setTotal_pagoReal(12341.123);
        rc.setTotal_totalRetenciones(1324.653);

        List<Conciliacion> lstConciliaciones = new ArrayList<>();

        Conciliacion c1 = new Conciliacion();

        c1.setCostoAdmin_i(98754.87);
        c1.setCostoAdmin_iva(9876.876);
        c1.setRazonSocial_EF("TEST TEsT test");
        c1.setProducto_EF("BaqnuitosTEST");
        c1.setTotalRetenciones(123123.123);
        c1.setDescYRepoEmit_casos(3463);
        c1.setDescYRepoEmit_importe(26345.23);
        c1.setDescYRepoPagados_casos(24234);
        c1.setDescYRepoPagados_importe(85643.43);
        c1.setFallecidos_casos(1232);
        c1.setFallecidos_importe(35634.56);
        c1.setId_EF("1234123");
        c1.setInconsistencias_casos(12341);
        c1.setInconsistencias_importe(2345243.34);
        c1.setNetoSinDescFallecidos_importe(1234123.234);
        c1.setNoPagado_casos(12341);
        c1.setNoPagado_importe(2345234.23);
        c1.setNoProv_EF(1223L);
        c1.setPagoReal(1234.43);

        Conciliacion c2 = c1;
        Conciliacion c3 = c1;

        lstConciliaciones.add(c1);
        lstConciliaciones.add(c2);
        lstConciliaciones.add(c3);

        rc.setLstConciliaciones(lstConciliaciones);
        return rc;

    }
}
