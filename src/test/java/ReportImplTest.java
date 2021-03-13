import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportImplTest {

    @Test
    void asBytes() throws IllegalAccessException, IOException {
        List<Car> list = Arrays.asList(new Car(1, "BMW", "228", 200, 125, 1),
                new Car(2, "BMW", "1", 200, 125, 2));
        ReportGeneratorImpl<Car> generator = new ReportGeneratorImpl<>();
        Report report = generator.generate(list);
        System.out.println(Arrays.toString(report.asBytes()));
    }

    @Test
    void renameFields() throws IllegalAccessException {
        List<Car> list = Arrays.asList(new Car(1, "BMW", "228", 200, 125, 1),
                new Car(2, "BMW", "1", 200, 125, 2));
        ReportGeneratorImpl<Car> generator = new ReportGeneratorImpl<>();
        ReportImpl report = generator.generate(list);
        assertEquals(report.strings[0][1], "brand");
        report.renameFields(new String[]{"Айди", "Бренд"});
        assertEquals(report.strings[0][1], "Бренд");
    }
}