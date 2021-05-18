Написать генератор отчетов в excel формат. Генератор должен в первой строке написать имена полей сущности, на следующих строчках значения полей.  Также должна быть возможность задать значения колонок в явном виде (Читаемые названия вместо имен полей). При реализации надо использовать Reflection. Используя данный генератор, создать отчет по аналитике по автомобилям из второго задания (аналитика на ваш выбор, можно взять методы из класса Garage, который вы реализовали в прошлом задании).
Excel отчеты генерировать с помощью библиотеки Apache POI. Подключить ее через Gradle. Написать Unit тесты на всю логику.

import java.io.OutputStream;
import java.util.List;

interface ReportGenerator<T> {
    Report generate(List<T> entities);
}

interface Report {
    byte[] asBytes();

    void writeTo(OutputStream os);
}


пример 
class Owner {
    String name;
    int age;
}
отчет:
name,age
Alex,20
Ivan, 25

Или
Имя,Возраст
Alex,20
Ivan, 25
