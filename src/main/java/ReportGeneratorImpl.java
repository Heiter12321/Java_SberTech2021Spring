import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ReportGeneratorImpl<T> implements ReportGenerator<T> {
    private final Class<?> clazz;

    public ReportGeneratorImpl (Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public ReportImpl generate(List<T> entities) throws IllegalAccessException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Main page");
        Iterator<Row> rowIterator = sheet.iterator();

        Field[] fields = clazz.getDeclaredFields();

        Row row = rowIterator.next();
        Iterator <Cell> cellIterator = row.cellIterator();
        if (clazz.isAnnotationPresent(ReportImpl.RenameFields.class)) {
            Annotation[] names = clazz.getAnnotations();
            for (Annotation name : names) {
                Cell cell = cellIterator.next();
                cell.setCellValue(name.toString());
            }
        } else {
            for (Field value : fields) {
                Cell cell = cellIterator.next();
                cell.setCellValue(value.getName());
            }
        }

        for (int i = 1; i < entities.size() + 1; ++i) {
            row = rowIterator.next();
            cellIterator = row.cellIterator();
            for (int j = 0; j < fields.length; ++j) {
                Cell cell = cellIterator.next();
                fields[j].setAccessible(true);
                cell.setCellValue(fields[j].get(entities.get(i - 1)).toString());
            }
        }

        return new ReportImpl(workbook);
    }
}
