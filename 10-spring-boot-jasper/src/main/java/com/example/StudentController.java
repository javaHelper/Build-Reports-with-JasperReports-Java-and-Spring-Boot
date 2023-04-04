package com.example;

import com.github.javafaker.Faker;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    private static final Faker FAKER = Faker.instance(Locale.ENGLISH);

    @GetMapping("/report")
    public ResponseEntity<byte[]> getReport() throws FileNotFoundException, JRException {
        Subject subject1 = Subject.builder().subjectName("Java").marksObtained(80L).build();
        Subject subject2 = Subject.builder().subjectName("MySQL").marksObtained(70L).build();
        Subject subject3 = Subject.builder().subjectName("PHP").marksObtained(50L).build();
        Subject subject4 = Subject.builder().subjectName("MongoDB").marksObtained(40L).build();
        Subject subject5 = Subject.builder().subjectName("C++").marksObtained(60L).build();

        List<Subject> subjects = Arrays.asList(subject1, subject2, subject3, subject4, subject5);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(subjects);
        JRBeanCollectionDataSource chartDataSource = new JRBeanCollectionDataSource(subjects);

        String path = ResourceUtils.getFile("classpath:Student.jrxml").getAbsolutePath();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("studentName", "John");
        parameters.put("tableData", dataSource);
        parameters.put("subReport", getSubReport());
        parameters.put("subDataSource", getSubDataSource());
        parameters.put("subParameters", getSubParameters());

        JasperReport jasperReport = JasperCompileManager.compileReport(path);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, chartDataSource);

        byte[] bytes = JasperExportManager.exportReportToPdf(print);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_PDF);
        httpHeaders.setContentDispositionFormData("fileName","student.pdf");

        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }

    public static JasperReport getSubReport() throws FileNotFoundException {
        String path = ResourceUtils.getFile("classpath:FirstReport.jrxml").getAbsolutePath();
        try {
            return JasperCompileManager.compileReport(path);
        } catch (JRException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static JRBeanCollectionDataSource getSubDataSource(){
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

        return new JRBeanCollectionDataSource(students);
    }

    public static Map<String, Object> getSubParameters(){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("studentName", "Prateek");
        return parameters;
    }
}