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
import java.util.Date;
import java.util.logging.Level;
import mx.gob.imss.dpes.common.service.BaseService;
import mx.gob.imss.dpes.interfaces.reportes.model.PrestamoAutorizadoDetalleDelegacion;
import mx.gob.imss.dpes.interfaces.reportes.model.PrestamoAutorizadoDetallePorEF;
import mx.gob.imss.dpes.interfaces.reportes.model.PrestamoAutorizadoPorEF;
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
public class GenerarExcelReportesEstadisticos extends BaseService {

    Workbook workbook = new XSSFWorkbook();
    Font font_10;
    CreationHelper createHelper = workbook.getCreationHelper();
    CellStyle styleCurrency = workbook.createCellStyle();
    CellStyle styleMiles = workbook.createCellStyle();
    CellStyle styleString = workbook.createCellStyle();
    CellStyle style = workbook.createCellStyle();
    Font font_11 = workbook.createFont();
    Font font_12 = workbook.createFont();
    Font font_14 = workbook.createFont();
    Sheet pagina;
    Integer filaReporte = 0;
    Row fila = null;

    public GenerarExcelReportesEstadisticos() {

        font_10 = workbook.createFont();
        font_10.setFontHeightInPoints((short) 10);
        font_10.setFontName("Montserrat Regular");
        font_10.setItalic(false);
        font_10.setStrikeout(false);

        styleCurrency.setDataFormat(createHelper.createDataFormat().getFormat("$#,##0.00"));
        styleCurrency.setFont(font_10);

        styleMiles.setDataFormat(createHelper.createDataFormat().getFormat("#,##0"));
        styleMiles.setFont(font_10);

        styleString.setFont(font_10);

        styleString.setBorderBottom(BorderStyle.THIN);
        styleString.setBorderTop(BorderStyle.THIN);

        styleMiles.setBorderBottom(BorderStyle.THIN);
        styleMiles.setBorderTop(BorderStyle.THIN);

        styleCurrency.setBorderBottom(BorderStyle.THIN);
        styleCurrency.setBorderTop(BorderStyle.THIN);

        pagina = workbook.createSheet("Reporte");
        font_11.setFontHeightInPoints((short) 11);
        font_11.setFontName("Montserrat Regular");
        font_11.setItalic(false);
        font_11.setStrikeout(false);

        font_12.setFontHeightInPoints((short) 12);
        font_12.setFontName("Montserrat Regular");
        font_12.setItalic(false);
        font_12.setStrikeout(false);

        font_14.setFontHeightInPoints((short) 14);
        font_14.setFontName("Montserrat Regular");
        font_14.setItalic(false);
        font_14.setStrikeout(false);

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
    }

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
        // Applying font to the style  
        //style.setFont(font);  
        // La hoja donde pondremos los datos
        // Creamos el estilo paga las celdas del encabezado
        // Indicamos que tendra un fondo azul aqua
        // con patron solido del color indicado
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

        pagina.addMergedRegion(CellRangeAddress.valueOf("A1:J1"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("A3:J3"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("A4:E4"));
        pagina.addMergedRegion(CellRangeAddress.valueOf("G4:J4"));

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

        switch (rc.getTipoReporte().intValue()) {
            case 5:
                celdaH2_1.setCellValue("Reporte Prestamos Autorizados por EF");
                break;
            case 6:
                celdaH2_1.setCellValue("Reporte Prestamos Autorizados Detalle por EF");
                break;
            case 7:
                celdaH2_1.setCellValue("Reporte Préstamos por Delegación");
                break;
        }

        filaReporte++;
        Row filaH3 = pagina.createRow(filaReporte);
        Integer w2 = 750;
        filaH3.setHeight(w2.shortValue());

        Cell celdaH3_1 = filaH3.createCell(0);
        celdaH3_1.setCellStyle(styleH2);
        celdaH3_1.setCellValue("Periodo de : " + dateFormat.format(rc.getFechaDesde())
                + " hasta: " + dateFormat.format(rc.getFechaHasta()));

        Cell celdaH3_2 = filaH3.createCell(6);
        celdaH3_2.setCellStyle(styleH2);
        celdaH3_2.setCellValue("Fecha de emisión: " + dateFormat.format(date));

        filaReporte++;
        fila = pagina.createRow(filaReporte);

        filaReporte++;
        Row fila2 = pagina.createRow(filaReporte);
        String[] titulos2 = getTitulos(rc.getTipoReporte().intValue());
        log.log(Level.INFO, "Reportes Estadisticos generaXLS 3: {0}", rc);

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
        for (int i = 1; i < titulos2.length; i++) {
            pagina.setColumnWidth(i, w);
        }

        filaReporte++;

        fillReport(rc);

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
        log.log(Level.INFO, "GenerarExcelComprasDeCarteraEF generaXLS 9 stringXLS: {0}", stringXLS);

        return stringXLS;
    }

    private String[] getTitulos(Integer tipoReporte) {
        String[] titulos = null;
        switch (tipoReporte) {
            case 5:
                titulos = new String[]{
                    "No.",
                    "Delegación",
                    "Subdelegación",
                    "Entidad Financiera",
                    "Total de préstamos autorizados",
                    "Importe promedio del préstamo",
                    "CAT promedio",
                    "Descuento mensual promedio",
                    "Plazo promedio mensual"
                };
                return titulos;

            case 6:
                titulos = new String[]{
                    "No.",
                    "Entidad Financiera",
                    "Total de préstamos autorizados",
                    "Importe promedio de los préstamos autorizados",
                    "CAT promedio",
                    "Descuento mensual promedio",
                    "Plazo promedio",
                    "Total de préstamos por simulación nuevo",
                    "Total de préstamos por simulación compra de cartera",
                    "Total de préstamos por simulación renovación",
                    "Total de préstamos por promotor nuevo",
                    "Total de préstamos por promotor compra de cartera",
                    "Total de préstamos por promotor renovación",
                    "Total de casos por sexo Hombre",
                    "Total de caos por sexo Mujer",
                    "Edad promedio del pensionado Hombre",
                    "Edad promedio del pensionado mujer"
                };
                return titulos;
            case 7:
                titulos = new String[]{
                    "No.",
                    "Delegación",
                    "Subdelegación",
                    "Total de créditos",
                    "Total de créditos en Recuperación",
                    "Total de créditos liquidados renovaciones",
                    "Total de créditos liquidados",
                    "Total de créditos liquidados Compra de cartera",
                    "Total de créditos liquidados termino de plazo",
                    "Total de préstamos dados de baja por Entidad Financiera",
                    "Total de créditos Fallecimiento",
                    "Subtotales de créditos"
                };
                return titulos;
        }

        return titulos;
    }

    private void fillReport(Reporte rc) {
        Integer contador = 1;

        switch (rc.getTipoReporte().intValue()) {
            case 5:
                // TODO: fill report
                for (PrestamoAutorizadoPorEF c : rc.getReportePrestamosAutorizadosPorEF().getRows()) {
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
                    celda1.setCellValue(c.getDelegacion());
                    celda1.setCellStyle(styleString);

                    col++;
                    Cell celda2 = fila.createCell(col, CellType.STRING);
                    if (c.getSubDelegacion() == null) {
                        celda2.setCellValue("");
                    } else {
                        celda2.setCellValue(c.getSubDelegacion());
                    }
                    celda2.setCellStyle(styleString);

                    col++;
                    Cell celda3 = fila.createCell(col, CellType.STRING);
                    if (c.getEntidadFinanciera() == null) {
                        celda3.setCellValue("");
                    } else {
                        celda3.setCellValue(c.getEntidadFinanciera());
                    }
                    celda3.setCellStyle(styleString);

                    col++;
                    Cell celda4 = fila.createCell(col, CellType.NUMERIC);
                    celda4.setCellValue(c.getTotalAutorizados());
                    celda4.setCellStyle(styleMiles);

                    col++;
                    Cell celda5 = fila.createCell(col, CellType.NUMERIC);
                    celda5.setCellValue(c.getImportePromedio());
                    celda5.setCellStyle(styleCurrency);

                    col++;
                    Cell celda6 = fila.createCell(col, CellType.NUMERIC);
                    celda6.setCellValue(c.getCatPromedio());
                    celda6.setCellStyle(styleMiles);

                    col++;
                    Cell celda7 = fila.createCell(col, CellType.NUMERIC);
                    celda7.setCellValue(c.getDescuentoPromedio());
                    celda7.setCellStyle(styleMiles);

                    col++;
                    Cell celda8 = fila.createCell(col, CellType.NUMERIC);
                    celda8.setCellValue(c.getMesesPromedio());
                    celda8.setCellStyle(styleMiles);

                    filaReporte++;
                }
                break;
            case 6:
                // TODO: fill report
                for (PrestamoAutorizadoDetallePorEF c : rc.getReportePrestamosAutorizadosDetallePorEF().getRows()) {
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
                    celda1.setCellValue(c.getEntidadFinanciera());
                    celda1.setCellStyle(styleString);

                    col++;
                    Cell celda2 = fila.createCell(col, CellType.NUMERIC);
                    celda2.setCellValue(c.getTotalAutorizados());
                    celda2.setCellStyle(styleMiles);

                    col++;
                    Cell celda3 = fila.createCell(col, CellType.NUMERIC);
                    celda3.setCellValue(c.getImportePromedio());
                    celda3.setCellStyle(styleCurrency);

                    col++;
                    Cell celda4 = fila.createCell(col, CellType.NUMERIC);
                    celda4.setCellValue(c.getCatPromedio());
                    celda4.setCellStyle(styleMiles);

                    col++;
                    Cell celda5 = fila.createCell(col, CellType.NUMERIC);
                    celda5.setCellValue(c.getDescuentoPromedio());
                    celda5.setCellStyle(styleCurrency);

                    col++;
                    Cell celda6 = fila.createCell(col, CellType.NUMERIC);
                    celda6.setCellValue(c.getMesesPromedio());
                    celda6.setCellStyle(styleMiles);

                    col++;
                    Cell celda7 = fila.createCell(col, CellType.NUMERIC);
                    celda7.setCellValue(c.getSimulacionNuevos());
                    celda7.setCellStyle(styleMiles);

                    col++;
                    Cell celda8 = fila.createCell(col, CellType.NUMERIC);
                    celda8.setCellValue(c.getSimulacionCartera());
                    celda8.setCellStyle(styleMiles);

                    col++;
                    Cell celda9 = fila.createCell(col, CellType.NUMERIC);
                    celda9.setCellValue(c.getSimulacionRenovacion());
                    celda9.setCellStyle(styleMiles);

                    col++;
                    Cell celda10 = fila.createCell(col, CellType.NUMERIC);
                    celda10.setCellValue(c.getPromotorNuevos());
                    celda10.setCellStyle(styleMiles);

                    col++;
                    Cell celda11 = fila.createCell(col, CellType.NUMERIC);
                    celda11.setCellValue(c.getPromotorCartera());
                    celda11.setCellStyle(styleMiles);

                    col++;
                    Cell celda12 = fila.createCell(col, CellType.NUMERIC);
                    celda12.setCellValue(c.getPromotorRenovacion());
                    celda12.setCellStyle(styleMiles);

                    col++;
                    Cell celda13 = fila.createCell(col, CellType.NUMERIC);
                    celda13.setCellValue(c.getTotalHombres());
                    celda13.setCellStyle(styleMiles);

                    col++;
                    Cell celda14 = fila.createCell(col, CellType.NUMERIC);
                    celda14.setCellValue(c.getTotalMujeres());
                    celda14.setCellStyle(styleMiles);

                    col++;
                    Cell celda15 = fila.createCell(col, CellType.NUMERIC);
                    celda15.setCellValue(c.getEdadPromedioHombres());
                    celda15.setCellStyle(styleMiles);

                    col++;
                    Cell celda16 = fila.createCell(col, CellType.NUMERIC);
                    celda16.setCellValue(c.getEdadPromedioMujeres());
                    celda16.setCellStyle(styleMiles);

                    filaReporte++;
                }
                break;
                case 7:
                // TODO: fill report
                for (PrestamoAutorizadoDetalleDelegacion c : rc.getReportePrestamosAutorizadosDetalleDelegacion().getRows()) {
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
                    celda1.setCellValue(c.getDelegacion());
                    celda1.setCellStyle(styleString);

                    col++;
                    Cell celda2 = fila.createCell(col, CellType.STRING);
                    celda2.setCellValue(c.getSubDelegacion());
                    celda2.setCellStyle(styleString);

                    col++;
                    Cell celda3 = fila.createCell(col, CellType.NUMERIC);
                    celda3.setCellValue(c.getTotalCreditos());
                    celda3.setCellStyle(styleMiles);

                    col++;
                    Cell celda4 = fila.createCell(col, CellType.NUMERIC);
                    celda4.setCellValue(c.getTotalRecuperacion());
                    celda4.setCellStyle(styleMiles);

                    col++;
                    Cell celda5 = fila.createCell(col, CellType.NUMERIC);
                    celda5.setCellValue(c.getLiquidadosRenovados());
                    celda5.setCellStyle(styleMiles);

                    col++;
                    Cell celda6 = fila.createCell(col, CellType.NUMERIC);
                    celda6.setCellValue(c.getTotalLiquidados());
                    celda6.setCellStyle(styleMiles);

                    col++;
                    Cell celda7 = fila.createCell(col, CellType.NUMERIC);
                    celda7.setCellValue(c.getLiquidadosCartera());
                    celda7.setCellStyle(styleMiles);

                    col++;
                    Cell celda8 = fila.createCell(col, CellType.NUMERIC);
                    celda8.setCellValue(c.getLiquidadosEnPlazo());
                    celda8.setCellStyle(styleMiles);

                    col++;
                    Cell celda9 = fila.createCell(col, CellType.NUMERIC);
                    celda9.setCellValue(c.getBajasPorEF());
                    celda9.setCellStyle(styleMiles);

                    col++;
                    Cell celda10 = fila.createCell(col, CellType.NUMERIC);
                    celda10.setCellValue(c.getBajasPorDefuncion());
                    celda10.setCellStyle(styleMiles);

                    col++;
                    Cell celda11 = fila.createCell(col, CellType.NUMERIC);
                    celda11.setCellValue(c.getSubtotalCredito());
                    celda11.setCellStyle(styleMiles);

                    filaReporte++;
                }
                break;
        }

    }
}
