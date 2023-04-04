package example;

import com.github.javafaker.Faker;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class TotalMarksObtainedDemo {
    private static final Faker FAKER = Faker.instance(Locale.ENGLISH);

    public static void main(String[] args) throws JRException {
        Subject subject1 = Subject.builder().subjectName("Java").marksObtained(80L).build();
        Subject subject2 = Subject.builder().subjectName("MySQL").marksObtained(70L).build();
        Subject subject3 = Subject.builder().subjectName("PHP").marksObtained(50L).build();
        Subject subject4 = Subject.builder().subjectName("MongoDB").marksObtained(40L).build();
        Subject subject5 = Subject.builder().subjectName("C++").marksObtained(60L).build();

        List<Subject> subjects = Arrays.asList(subject1, subject2, subject3, subject4, subject5);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(subjects);
        JRBeanCollectionDataSource chartDataSource = new JRBeanCollectionDataSource(subjects);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("studentName", "John");
        parameters.put("tableData", dataSource);
        parameters.put("subReport", getSubReport());
        parameters.put("subDataSource", getSubDataSource());

        InputStream resourceAsStream = TotalMarksObtainedDemo.class.getClassLoader().getResourceAsStream("student.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(resourceAsStream);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, chartDataSource);

        JasperExportManager.exportReportToPdfFile(print, "Student.pdf");
        JasperExportManager.exportReportToHtmlFile(print, "student.html");

        JRXlsxExporter jrXlsxExporter = new JRXlsxExporter();
        jrXlsxExporter.setExporterInput(new SimpleExporterInput(print));
        jrXlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput("student.xlsx"));
        jrXlsxExporter.exportReport();

        System.out.println("Report Created...");
    }

    public static JasperReport getSubReport(){
        InputStream resourceAsStream = TotalMarksObtainedDemo.class.getClassLoader().getResourceAsStream("FirstReport.jrxml");
        try {
            return JasperCompileManager.compileReport(resourceAsStream);
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
}