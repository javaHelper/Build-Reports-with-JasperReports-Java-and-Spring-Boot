package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;

@SpringBootApplication
public class CustomReportApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CustomReportApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String filePath = ResourceUtils.getFile("classpath:Student.jrxml")
				.getAbsolutePath();

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(this.subjects());

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("studentName", "John");
		parameters.put("tableData", dataSource);

		JasperReport report = JasperCompileManager.compileReport(filePath);
		JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

		JasperExportManager.exportReportToPdfFile(print,"student.pdf");
		System.out.println("Report Created...");
	}

	private List<Subject> subjects(){
		List<Subject> list = new ArrayList<Subject>();
		list.add(new Subject("Java", 80));
		list.add(new Subject("MySQL", 70));
		list.add(new Subject("PHP", 50));
		list.add(new Subject("MongoDB", 40));
		list.add(new Subject("C++", 60));
		return list;
	}
}