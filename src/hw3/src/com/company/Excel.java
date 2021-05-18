package com.company;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Excel {
    public static void CreateExcelSheet(String[] column_names, List<String> values, String name) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("first_sheet");

        Row row = sheet.createRow(0);
        row.setHeightInPoints(22);
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setItalic(true);
        font.setFontHeightInPoints((short) 18);
        font.setColor(IndexedColors.DARK_RED.index);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        for (int i = 0; i < column_names.length; ++i) {
            Cell cell = row.createCell(i);
            cell.setCellValue(column_names[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
            sheet.createFreezePane(0, 1);
        }

        for (int i = 1; i <= values.size() / column_names.length; ++i) {
            Row row2 = sheet.createRow(i);
            row.setHeightInPoints(18);
            HSSFFont font2 = workbook.createFont();
            font.setFontHeightInPoints((short) 16);
            HSSFCellStyle style2 = workbook.createCellStyle();
            style2.setFont(font2);
            for (int j = 0; j < column_names.length; ++j) {
                Cell cell2 = row2.createCell(j);
                cell2.setCellValue(values.get(column_names.length * (i - 1) + j));
                cell2.setCellStyle(style2);
                sheet.autoSizeColumn(j);
            }
        }

        workbook.write(new FileOutputStream(name));
        workbook.close();
    }
}
