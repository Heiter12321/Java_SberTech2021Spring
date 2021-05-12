import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Iterator;

public class Exporter {
    public XSSFWorkbook exportToExcelFormat(Workbook book) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet(book.getSheetName(0));
        Iterator<Row> rowIterator = sheet.iterator();
        Iterator<Row> bookRowIterator = book.getSheetAt(0).rowIterator();

        while (bookRowIterator.hasNext()) {
            Iterator<Cell> cellIterator = rowIterator.next().cellIterator();
            Iterator<Cell> bookCellIterator = bookRowIterator.next().cellIterator();

            while(bookCellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                cell.setCellValue(bookCellIterator.next().getStringCellValue());
            }
        }

        return workbook;
    }
}
