import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class ReportGeneratorImpl<T> implements ReportGenerator<T> {

    @Override
    public ReportImpl generate(List<T> entities) throws IllegalAccessException {
        var excel = new StringBuilder();
        Class<?> clazz = entities.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        boolean flag = true;
        String[][] strings = new String[entities.size() + 1][fields.length];
        int i = 0;
        for (Field field : fields) {
            strings[0][i] = field.getName();
            ++i;
        }
        i = 1;
        int j = 0;
        for (T elem : entities) {
            for (Field field : fields) {
                field.setAccessible(true);
                strings[i][j] = field.get(elem).toString();
                ++j;
            }
            ++i;
            j = 0;
        }

        for (String[] strings1 : strings) {
            for (String str : strings1) {
                System.out.print(str + " ");
            }
            System.out.println("\n");
        }

        return new ReportImpl(strings);
    }
}
