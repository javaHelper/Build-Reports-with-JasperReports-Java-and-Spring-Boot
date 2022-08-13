package com.example.demo;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.base.JRBaseTextField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class AddingLabelTextImgJasperReportApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AddingLabelTextImgJasperReportApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("studentName", "John");

		String filePath = ResourceUtils.getFile("classpath:Adding-Page-Footer-To-Display-Page-Count.jrxml")
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
		students.add(Student.builder().id(1L).firstName("John").lastName("Doe").city("Nagpur").street("AAA Street").build());
		students.add(Student.builder().id(2L).firstName("Jane").lastName("Doe").city("Yavatmal").street("BBB Street").build());
		students.add(Student.builder().id(3L).firstName("Mike").lastName("Doe").city("Akola").street("CCC Street").build());
		students.add(Student.builder().id(4L).firstName("Matt").lastName("Doe").city("Amravati").street("DDD Street").build());
		students.add(Student.builder().id(5L).firstName("Victoria").lastName("Doe").city("Wardha").street("EEE Street").build());
		return students;
	}
}