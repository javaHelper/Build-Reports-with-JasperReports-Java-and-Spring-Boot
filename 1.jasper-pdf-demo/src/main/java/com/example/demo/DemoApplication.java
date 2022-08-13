package com.example.demo;

import com.example.demo.model.Student;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("studentName", "John");

		String filePath = ResourceUtils.getFile("classpath:FirstReport.jrxml")
				.getAbsolutePath();

		// A data source implementation that wraps a collection of JavaBean objects.
		//It is common to access application data through object persistence layers like EJB, Hibernate, or JDO.
		// Such applications may need to generate reports using data they already have available as arrays or
		// collections of in-memory JavaBean objects.
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(this.students());
		JasperReport report = JasperCompileManager.compileReport(filePath);

		JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);

		JasperExportManager.exportReportToPdfFile(print, "FirstReport.pdf");
		System.out.println("Report Created...");
	}

	private List<Student> students(){
		List<Student> students = new ArrayList<>();
		students.add(Student.builder().id(1L).firstName("John").lastName("Doe").street("Achalpur Street").city("Achalpur").build());
		students.add(Student.builder().id(2L).firstName("Jane").lastName("Doe").street("Paratwada Street").city("Paratwada").build());
		students.add(Student.builder().id(3L).firstName("Karan").lastName("Malhotra").street("West Street").city("Nagpur").build());
		students.add(Student.builder().id(4L).firstName("Shrutika").lastName("Ninawe").street("Abhyankar Street").city("Amaravati").build());
		return students;
	}
}