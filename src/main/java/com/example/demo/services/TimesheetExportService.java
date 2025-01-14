package com.example.demo.services;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class TimesheetExportService {

	private static final String TEMPLATE_PATH = "templates/template.xlsx";

	public byte[] createExcelFile(List<Object> data) throws IOException {

		// 1. Đọc file template
		ClassPathResource templateResource = new ClassPathResource(TEMPLATE_PATH);
		FileInputStream inputStream = new FileInputStream(templateResource.getFile());
		Workbook workbook = new XSSFWorkbook(inputStream);

		// 2. Lấy sheet cần chỉnh sửa
		Sheet sheet = workbook.getSheetAt(0); // Hoặc `workbook.getSheet("Tên Sheet")`

		// 3. Thêm dữ liệu vào sheet
		int startRow = 1; // Ví dụ: Bắt đầu từ dòng thứ 2 (0-indexed)

		for (Object item : data) {

			Row row = sheet.createRow(startRow++);

			// Ví dụ: Giả sử mỗi item là một mảng Object[]
			Object[] rowData = (Object[])item;

			for (int colIndex = 0; colIndex < rowData.length; colIndex++) {

				Cell cell = row.createCell(colIndex);

				if (rowData[colIndex] instanceof String) {

					cell.setCellValue((String)rowData[colIndex]);

				} else if (rowData[colIndex] instanceof Integer) {

					cell.setCellValue((Integer)rowData[colIndex]);

				} else if (rowData[colIndex] instanceof Double) {

					cell.setCellValue((Double)rowData[colIndex]);

				}

				// Optional: Thêm style cho cell nếu cần
			}

		}

		// 4. Ghi workbook ra byte array
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		workbook.close();

		return out.toByteArray();

	}
}
