/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.prestamofront.service.reporte.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import mx.gob.imss.dpes.common.service.BaseService;
import mx.gob.imss.dpes.interfaces.reportes.model.PrestamoRecuperacionCompraCarteraEF;
import mx.gob.imss.dpes.interfaces.reportes.model.Reporte;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.util.Log;
import org.apache.poi.ss.util.CellRangeAddress;


/**
 *
 * @author juan.garfias
 */
public class GenerarExcelComprasDeCarteraEF extends BaseService {

    public String generaXLS(Reporte rc) throws FileNotFoundException, IOException {
        log.log(Level.INFO, "GenerarExcelComprasDeCarteraEF generaXLS 1: {0}", rc);

        String stringXLS = "";

        Date date = new Date();
        //Caso 2: obtener la fecha y salida por pantalla con formato:
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //System.out.println("Fecha: " + dateFormat.format(date));

        // Creamos el archivo donde almacenaremos la hoja
        // de calculo, recuerde usar la extension correcta,
        // en este caso .xlsx
        log.log(Level.INFO, "GenerarExcelComprasDeCarteraEF generaXLS 2: {0}", rc);

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

        /*
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
*/
        pagina.addMergedRegion(CellRangeAddress.valueOf("A1:J1"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("A3:J3"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("A4:E4"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("G4:J4"));
/*
        pagina.addMergedRegion(CellRangeAddress.valueOf("A5:E5"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("F5:G5"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("H5:I5"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("J5:K5"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("M5:N5"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("P5:Q5"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("S5:T5"));
         */
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
        celdaH2_1.setCellValue("Reporte de Compra de Cartera EF");

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
        /*
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
         */

        filaReporte++;
        Row fila2 = pagina.createRow(filaReporte);
        String[] titulos2 = {
            "No.",
            "Folio",
            "NSS",
            "CURP",
            "Nombre Completo",
            "Importe del Préstamo",
            "Descuento Mensual",
            "Número de Descuento",
            "Importe Liquidado",
            "CAT"
        };
        log.log(Level.INFO, "GenerarExcelComprasDeCarteraEF generaXLS 3: {0}", rc);

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

        Integer w = 4600;
        Integer i = 1;
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
        for (PrestamoRecuperacionCompraCarteraEF c : rc.getReporteCompraCarteraEF().getPrestamosCompraCarteraEF()) {
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
            celda1.setCellValue(c.getFolio());
            celda1.setCellStyle(styleString);

            col++;
            Cell celda2 = fila.createCell(col, CellType.STRING);
            if (c.getNss() == null) {
                celda2.setCellValue("");
            } else {
                celda2.setCellValue(c.getNss());
            }
            celda2.setCellStyle(styleString);

            col++;
            Cell celda3 = fila.createCell(col, CellType.STRING);
            if (c.getCurp() == null) {
                celda3.setCellValue("");
            } else {
                celda3.setCellValue(c.getCurp());
            }
            celda3.setCellStyle(styleString);

            col++;
            Cell celda4 = fila.createCell(col, CellType.STRING);
            celda4.setCellValue(c.getNombreCompleto());
            celda4.setCellStyle(styleString);

            col++;
            Cell celda5 = fila.createCell(col, CellType.NUMERIC);
            celda5.setCellValue(c.getImporte());
            celda5.setCellStyle(styleCurrency);

            col++;
            Cell celda6 = fila.createCell(col, CellType.NUMERIC);
            celda6.setCellValue(c.getDescuento());
            celda6.setCellStyle(styleCurrency);

            col++;
            Cell celda7 = fila.createCell(col, CellType.NUMERIC);
            celda7.setCellValue(c.getNumDescuento());
            celda7.setCellStyle(styleMiles);

            col++;
            Cell celda8 = fila.createCell(col, CellType.NUMERIC);
            celda8.setCellValue(c.getImporteLiquidado());
            celda8.setCellStyle(styleCurrency);

            col++;
            Cell celda9 = fila.createCell(col, CellType.NUMERIC);
            celda9.setCellValue(c.getCat());
            celda9.setCellStyle(styleMiles);

            filaReporte++;
        }

        // Ahora guardaremos el archivo
        try {
            // Creamos el flujo de salida de datos,
            // apuntando al archivo donde queremos 
            // almacenar el libro de Excel
            log.log(Level.INFO, "GenerarExcelComprasDeCarteraEF generaXLS 4 ");

            //FileOutputStream salida = new FileOutputStream(archivo);
            log.log(Level.INFO, "GenerarExcelComprasDeCarteraEF generaXLS 5 ");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            log.log(Level.INFO, "GenerarExcelComprasDeCarteraEF generaXLS 6 ");

            // Almacenamos el libro de 
            // Excel via ese 
            // flujo de datos
            workbook.write(out);
            log.log(Level.INFO, "GenerarExcelComprasDeCarteraEF generaXLS 7 ");

            stringXLS = Base64.encodeBase64String(out.toByteArray());
            log.log(Level.INFO, "GenerarExcelComprasDeCarteraEF generaXLS 8 stringXLS");

            // Cerramos el libro para concluir operaciones
            workbook.close();

            //log.log(Level.INFO, "Archivo creado existosamente en {0}", archivo.getAbsolutePath());
            //log.log(Level.INFO, "Archivo XLSX creado existosamente en : {0}", stringXLS);
        } catch (FileNotFoundException ex) {
            Log.info("Archivo no localizable en sistema de archivos");
            log.log(Level.INFO, "GenerarExcelComprasDeCarteraEF generaXLS 5: {0}", ex);

        } catch (IOException ex) {
            Log.info("Error de entrada/salida");
            log.log(Level.INFO, "GenerarExcelComprasDeCarteraEF generaXLS 6: {0}", ex);

        }
            log.log(Level.INFO, "GenerarExcelComprasDeCarteraEF generaXLS 9 stringXLS: {0}",stringXLS);

        return stringXLS;
    }

}
