import java.util.List;

public interface ReportGenerator<T> {
    ReportImpl generate(List<T> entities) throws IllegalAccessException;
}
