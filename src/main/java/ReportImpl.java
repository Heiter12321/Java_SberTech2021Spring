import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ReportImpl implements Report{
    public String[][] getStrings() {
        return strings;
    }

    public final String[][] strings;

    public ReportImpl(String[][] strings) {
        this.strings = strings;
    }

    @Override
    public byte[] asBytes() throws IOException {
        Exporter exporter = new Exporter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exporter.exportToExcelFormat(strings).write(baos);
        return baos.toByteArray();
    }

    @Override
    public void writeTo(OutputStream os) throws IOException {
        os.write(asBytes());
    }

    public void renameFields(String[] strs) {
        System.arraycopy(strs, 0, strings[0], 0, strs.length);
    }
}
