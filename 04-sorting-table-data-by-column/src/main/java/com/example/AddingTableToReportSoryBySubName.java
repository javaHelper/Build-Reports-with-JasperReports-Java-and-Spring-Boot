package com.example;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddingTableToReportSoryBySubName {

    public static void main(String[] args) throws JRException {
        Subject subject1 = Subject.builder().subjectName("Java").marksObtained(80L).build();
        Subject subject2 = Subject.builder().subjectName("MySQL").marksObtained(70L).build();
        Subject subject3 = Subject.builder().subjectName("PHP").marksObtained(50L).build();
        Subject subject4 = Subject.builder().subjectName("MongoDB").marksObtained(90L).build();
        Subject subject5 = Subject.builder().subjectName("C++").marksObtained(86L).build();

        List<Subject> subjects = Arrays.asList(subject1, subject2, subject3, subject4, subject5);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(subjects);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("studentName", "John");
        parameters.put("tableData", dataSource);

        InputStream resourceAsStream = AddingTableToReportSoryBySubName.class.getClassLoader().getResourceAsStream("studentBySortingColumn.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(resourceAsStream);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        JasperExportManager.exportReportToPdfFile(print, "Student.pdf");
        System.out.println("Report Created...");
    }
}