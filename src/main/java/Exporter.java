import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Iterator;

public class Exporter {
    public XSSFWorkbook exportToExcelFormat(String[][] strings) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("Main page");
        XSSFSheet sheet = workbook.getSheet("Main page");
        Iterator<Row> rowIterator = sheet.iterator();
        for (int i = 0; i < strings.length; ++i) {
            if (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator <Cell> cellIterator = row.cellIterator();
                for (int j = 0; j < strings[0].length; ++i) {
                    System.out.println(j);
                    Cell cell = cellIterator.next();
                    cell.setCellValue(strings[i][j]);
                }
            }
        }
        return workbook;
    }
}
