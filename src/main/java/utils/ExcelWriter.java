package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelWriter {

    private static final String FILE_PATH = "./Reports/CarWashServices.xlsx";

    // Utility class → no object creation
    private ExcelWriter() {}

    public static void writeData(List<String> names,
                                 List<String> phones,
                                 List<String> ratings) {

        Workbook workbook = new XSSFWorkbook();          // Excel file
        Sheet sheet = workbook.createSheet("Car Wash");  // Excel sheet

        // --- Create header row ---
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("S.No");
        headerRow.createCell(1).setCellValue("Service Name");
        headerRow.createCell(2).setCellValue("Phone Number");
        headerRow.createCell(3).setCellValue("Rating");

        // --- Write data rows ---
        for (int i = 0; i < names.size(); i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(names.get(i));
            row.createCell(2).setCellValue(
                    i < phones.size() ? phones.get(i) : "N/A"
            );
            row.createCell(3).setCellValue(
                    i < ratings.size() ? ratings.get(i) : "N/A"
            );
        }

        // --- Auto-size columns ---
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        // --- Ensure Reports directory exists ---
        File dir = new File("./Reports");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // --- Write Excel file ---
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { workbook.close(); } catch (IOException ignored) {}
        }
    }
}