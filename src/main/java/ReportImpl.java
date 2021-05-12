import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class ReportImpl implements Report{
    public final Workbook workbook;

    public ReportImpl(Workbook workbook) {
        this.workbook = workbook;
    }

    @Override
    public byte[] asBytes() throws IOException {
        Exporter exporter = new Exporter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exporter.exportToExcelFormat(workbook).write(baos);
        return baos.toByteArray();
    }

    @Override
    public void writeTo(OutputStream os) throws IOException {
        os.write(asBytes());
    }

    @Target(value=ElementType.FIELD)
    public @interface RenameFields {
        String value();
    }
}
