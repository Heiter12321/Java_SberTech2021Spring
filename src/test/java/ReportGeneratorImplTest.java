import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportGeneratorImplTest {

    @org.junit.jupiter.api.Test
    void generate() throws IllegalAccessException {
        List<Car> list = Arrays.asList(new Car(1, "BMW", "228", 200, 125, 1),
                new Car(2, "BMW", "1", 200, 125, 2));
        ReportGeneratorImpl<Car> generator = new ReportGeneratorImpl<>(Car.class);
        ReportImpl report = generator.generate(list);
    }
}