/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.reports;

import com.sitron.persistence.entities.StudyCase;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 *
 * @author jorge
 */
public class ReportsManager {

    private static JasperReport jasperCompiledFile;

    public static byte[] createInformePdf(StudyCase caso) {
        JRProperties.setProperty("net.sf.jasperreports.awt.ignore.missing.font", true);
        try {
            if (jasperCompiledFile == null) {
                URL urlActa = ReportsManager.class.getClassLoader().getResource("informe.jasper");
                jasperCompiledFile = (JasperReport) JRLoader.loadObject(urlActa);
            }

            Map parameters = new HashMap();
            parameters.put("studyCase", caso);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperCompiledFile, parameters, new JREmptyDataSource());
            return createPdfFromJasper(jasperPrint);

//            InputStream in = new ByteArrayInputStream(bytes);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(ReportsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("ERROR ON JASPER REPORT CREATION!");
        return null;
    }



    private static byte[] createPdfFromJasper(JasperPrint jasperPrint) throws IOException, JRException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRExporter exporter = new JRPdfExporter();
        //exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "ISO-8859-15");
        exporter.setParameter(JRPdfExporterParameter.METADATA_TITLE, "Documento generado por Jonathan Loyola - www.godesk.cl");
        exporter.setParameter(JRPdfExporterParameter.METADATA_SUBJECT, "Documento generado por GoDesk - www.godesk.cl");
        exporter.setParameter(JRPdfExporterParameter.METADATA_KEYWORDS, "");
        exporter.setParameter(JRPdfExporterParameter.METADATA_CREATOR, "GoDesk - www.godesk.cl");
        exporter.setParameter(JRPdfExporterParameter.METADATA_AUTHOR, "GoDesk - www.godesk.cl");
        //exporter.setParameter(JRPdfExporterParameter.PERMISSIONS,new Integer(PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_COPY) );
        //TODO: ver propiedad para no copiar
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
        exporter.exportReport();
//        FileOutputStream output = new FileOutputStream(filename);
        return baos.toByteArray();
    }
}
