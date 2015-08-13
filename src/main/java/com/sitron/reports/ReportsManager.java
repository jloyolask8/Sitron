/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.reports;

import com.sitron.persistence.entities.Instance;
import com.sitron.persistence.entities.StudyCase;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
import org.apache.commons.lang3.StringUtils;
import org.olap4j.impl.Base64;

/**
 *
 * @author jorge
 */
public class ReportsManager {

    private static JasperReport jasperCompiledFile;
    private static JasperReport jasperCompiledFileP2;

    public static byte[] createInformePdf(StudyCase caso) {

        /**
         * Solo para poder ver el reporte de 2 paginas
         */
//        if ((caso.getImages().size() < 3) && (caso.getImages().size() > 0)) {
//            while (caso.getImages().size() < 8) {
//                caso.getImages().add(caso.getFirstImage());
//            }
//        }
        JRProperties.setProperty("net.sf.jasperreports.awt.ignore.missing.font", true);
        try {
            if (jasperCompiledFile == null) {
                URL urlActa = ReportsManager.class.getClassLoader().getResource("informe.jasper");
                jasperCompiledFile = (JasperReport) JRLoader.loadObject(urlActa);
            }

            Map parameters = new HashMap();
            parameters.put("studyCase", caso);
            parameters.put("images_total", caso.getImages().size());
            for (int i = 0; i < caso.getImages().size(); i++) {
                if (i >= 2) {
                    break;
                }
                try {
                    parameters.put("image" + i, getImageInputStream(caso.getImages().get(i)));
                } catch (IOException ex) {
                    Logger.getLogger(ReportsManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (caso.getImages().size() <= 2) {
                try {
                    if (caso.getAssignee() != null && !StringUtils.isEmpty(caso.getAssignee().getSignature())) {
                        final String split = caso.getAssignee().getSignature().split(",")[1];
                        final byte[] decoded = Base64.decode(split);
                        parameters.put("signature", new ByteArrayInputStream(decoded));
                    }
                } catch (Exception e) {
                    System.out.println("PROBLEMA CON LA FIRMA");
                    e.printStackTrace();
                }
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperCompiledFile, parameters, new JREmptyDataSource());

            if (caso.getImages().size() > 2) {
                if (jasperCompiledFileP2 == null) {
                    URL urlP2 = ReportsManager.class.getClassLoader().getResource("informe_p2.jasper");
                    jasperCompiledFileP2 = (JasperReport) JRLoader.loadObject(urlP2);
                }

                parameters = new HashMap();
                parameters.put("studyCase", caso);
                parameters.put("images_total", caso.getImages().size());
                try {
                    for (int i = 2; i < caso.getImages().size(); i++) {
                        parameters.put("image" + i, getImageInputStream(caso.getImages().get(i)));
                    }
                    final String split = caso.getAssignee().getSignature().split(",")[1];
                    final byte[] decoded = Base64.decode(split);
                    parameters.put("signature", new ByteArrayInputStream(decoded));
                } catch (IOException ex) {
                    Logger.getLogger(ReportsManager.class.getName()).log(Level.SEVERE, null, ex);
                }

                JasperPrint jasperPrintP2 = JasperFillManager.fillReport(jasperCompiledFileP2, parameters, new JREmptyDataSource());
                jasperPrint.addPage(jasperPrintP2.getPages().get(0));
            }
            return createPdfFromJasper(jasperPrint, caso.getPatientFk().getPatPName(), caso.getSubject());

//            InputStream in = new ByteArrayInputStream(bytes);
        } catch (JRException | IOException ex) {
            Logger.getLogger(ReportsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("ERROR ON JASPER REPORT CREATION!");
        return null;
    }

    private static InputStream getImageInputStream(Instance image) throws IOException, MalformedURLException {
        //for (Instance image : caso.getImages()) {
        String strUrl = "http://www.godesk.cl:8089/dcm4chee-arc/wado/DCM4CHEE?requestType=WADO&studyUID="
                + image.getSeriesFk().getStudyFk().getStudyIuid() + "&seriesUID="
                + image.getSeriesFk().getSeriesIuid() + "&objectUID=" + image.getSopIuid();
        URL url = new URL(strUrl);
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1 != (n = in.read(buf))) {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] response = out.toByteArray();
        InputStream inpict = new ByteArrayInputStream(response);
        return inpict;
    }

    private static byte[] createPdfFromJasper(JasperPrint jasperPrint, String patientName, String subject) throws IOException, JRException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRExporter exporter = new JRPdfExporter();
        //exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "ISO-8859-15");
        exporter.setParameter(JRPdfExporterParameter.METADATA_TITLE, "Informe radiologico - " + patientName);
        exporter.setParameter(JRPdfExporterParameter.METADATA_SUBJECT, subject);
        exporter.setParameter(JRPdfExporterParameter.METADATA_KEYWORDS, "");
        exporter.setParameter(JRPdfExporterParameter.METADATA_CREATOR, "Sitron ver. 1.0 - powered by www.godesk.cl");
        exporter.setParameter(JRPdfExporterParameter.METADATA_AUTHOR, "Sitron ver. 1.0 - powered by www.godesk.cl");
        //exporter.setParameter(JRPdfExporterParameter.PERMISSIONS,new Integer(PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_COPY) );
        //TODO: ver propiedad para no copiar
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
        exporter.exportReport();
//        FileOutputStream output = new FileOutputStream(filename);
        return baos.toByteArray();
    }
}
