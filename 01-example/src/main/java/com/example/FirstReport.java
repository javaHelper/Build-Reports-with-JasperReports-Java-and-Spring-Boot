package com.example;

import com.github.javafaker.Faker;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class FirstReport {
    private static final Faker FAKER = Faker.instance(Locale.ENGLISH);

    public static void main(String[] args) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("studentName", "Prateek");

            List<Student> students = LongStream.rangeClosed(1, 30)
                    .boxed()
                    .map(i -> Student.builder()
                            .id(i)
                            .firstName(FAKER.name().firstName())
                            .lastName(FAKER.name().lastName())
                            .street(FAKER.address().streetAddress())
                            .city(FAKER.address().city())
                            .build()
                    )
                    .collect(Collectors.toList());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(students);

            // Load this from classpath
            InputStream resourceAsStream = FirstReport.class.getClassLoader().getResourceAsStream("FirstReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(resourceAsStream);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            JasperExportManager.exportReportToPdfFile(print, "FirstReport.pdf");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}