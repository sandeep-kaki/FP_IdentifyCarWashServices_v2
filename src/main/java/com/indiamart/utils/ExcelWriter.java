package com.indiamart.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * ExcelWriter – Utility class for writing test output data to Excel.
 *
 * PURPOSE:
 *   TC01 requires the top 3 car wash service details to be saved.
 *   This class creates ./Reports/CarWashServices.xlsx with:
 *     Column A – S.No
 *     Column B – Service Name
 *     Column C – Phone Number  (from detail page; "Hidden" if locked)
 *     Column D – Rating        (from search results; "N/A" if not shown)
 *
 * LIBRARY: Apache POI (poi-ooxml dependency in pom.xml)
 * This is the same library used for Excel READING in data-driven frameworks.
 * Here we use it for WRITING (output report).
 */
public class ExcelWriter {

    private static final Logger log    = LogManager.getLogger(ExcelWriter.class);
    private static final String OUTPUT = "./Reports/CarWashServices.xlsx";

    // Prevent instantiation
    private ExcelWriter() {}

    /**
     * Creates/overwrites the Excel report with service data.
     *
     * @param names   service names  (max 3)
     * @param phones  phone numbers  (parallel to names)
     * @param ratings ratings        (parallel to names; "N/A" if not available)
     */
    public static void writeCarWashData(List<String> names,
                                        List<String> phones,
                                        List<String> ratings) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Car Wash – Chennai");

        // ── Header style (dark blue background, white bold text) ──────────
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // ── Header row ────────────────────────────────────────────────────
        String[] headers = { "S.No", "Service Name", "Phone Number", "Rating" };
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // ── Data rows ─────────────────────────────────────────────────────
        for (int i = 0; i < names.size(); i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(names.get(i));
            row.createCell(2).setCellValue(i < phones.size()  ? phones.get(i)  : "N/A");
            row.createCell(3).setCellValue(i < ratings.size() ? ratings.get(i) : "N/A");
        }

        // ── Auto-size columns ─────────────────────────────────────────────
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // ── Ensure Reports directory exists ───────────────────────────────
        File reportsDir = new File("./Reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        // ── Write to disk ─────────────────────────────────────────────────
        try (FileOutputStream fos = new FileOutputStream(OUTPUT)) {
            workbook.write(fos);
            log.info("Excel report saved → {}", OUTPUT);
        } catch (IOException e) {
            log.error("Failed to write Excel report: {}", e.getMessage());
        } finally {
            try { workbook.close(); } catch (IOException ignored) {}
        }
    }
}
