package com.company;

import org.jfree.chart.demo.BarChartDemo1;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.time.Year;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;

public class Queries {
    public static void FirstQuery() throws SQLException, ClassNotFoundException, IOException {
        Connection c;
        Statement stmt = null;
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/airtrans", "postgres", "123");
        String sql =    "select city->'ru', airport_code from airports " +
                        "where city in ("                          +
                            "select city from airports "           +
                            "group by city "                       +
                            "having count(airport_code) > 1)";

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        String[] columnNames = new String[] {"Город", "Список аэропортов"};
        List<String> values = new ArrayList<String>();

        String currentCity = "";
        String codes = "";

        while(rs.next()) {
            if (currentCity.equals("")) {
                currentCity = rs.getString(1);
                codes = rs.getString(2);
            } else {
                if (currentCity.equals(rs.getString(1))) {
                    codes += ", " + rs.getString(2);
                } else {
                    currentCity = currentCity.replace("\"", "");
                    values.add(currentCity);
                    values.add(codes);
                    currentCity = rs.getString(1);
                    codes = rs.getString(2);
                }
            }
        }
        currentCity = currentCity.replace("\"", "");
        values.add(currentCity);
        values.add(codes);
        Excel.CreateExcelSheet(columnNames, values, "Query1.xls");
    }

    public static void SecondQuery() throws ClassNotFoundException, SQLException, IOException {
        Connection c;
        Statement stmt = null;
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/airtrans", "postgres", "123");
        String sql =    "select city->'ru' as \"Город\", count(city) as \"Количество отмененных рейсов\" " +
                        " from flights join airports " +
                        " on flights.departure_airport = airports.airport_code " +
                        " where status = 'Cancelled' " +
                        " group by city " +
                        " order by \"Количество отмененных рейсов\" desc";

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        String[] columnNames = new String[] {"Город", "Количество отмененных рейсов"};
        List<String> values = new ArrayList<String>();

        while(rs.next()) {
            values.add(rs.getString(1));
            values.add(String.valueOf(rs.getInt(2)));
        }
        Excel.CreateExcelSheet(columnNames, values, "Query2.xls");
    }
    
    public static void ThirdQuery() throws SQLException, ClassNotFoundException, IOException {
        Connection c;
        Statement stmt = null;
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/airtrans", "postgres", "123");
        String sql =    "select f1.city1, f2.city2, f1.minimum from " +
                "(select first.city1, min(first.average) as minimum from ( " +
                "    select city->'ru' as city1, city2, AVG(actual_arrival - actual_departure) as average from " +
                "    flights join airports on flights.departure_airport = airports.airport_code " +
                "    join (select city->'ru' as city2, airport_code as code from airports) as air " +
                "    on flights.arrival_airport = air.code " +
                "    where actual_arrival is not null " +
                "    group by city->'ru', city2 " +
                "    order by city->'ru', AVG(actual_arrival - actual_departure)) as first " +
                "group by first.city1 " +
                "order by first.city1) as f1 join " +
                "(select city->'ru' as city1, city2, AVG(actual_arrival - actual_departure) as average from " +
                "    flights join airports on flights.departure_airport = airports.airport_code " +
                "    join (select city->'ru' as city2, airport_code as code from airports) as air " +
                "    on flights.arrival_airport = air.code " +
                "    where actual_arrival is not null " +
                "    group by city->'ru', city2 " +
                "    order by city->'ru', AVG(actual_arrival - actual_departure)) as f2 on " +
                "f1.city1 = f2.city1 and f1.minimum = f2.average";

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        String[] columnNames = new String[] {"Город", "Пункт прибытия", "Средняя продолжительность полёта"};
        List<String> values = new ArrayList<String>();

        while(rs.next()) {
            values.add(rs.getString(1));
            values.add(rs.getString(2));
            values.add((rs.getTime(3)).toString());
        }
        Excel.CreateExcelSheet(columnNames, values, "Query3.xls");
    }

    public static void FourthQuery() throws ClassNotFoundException, SQLException, IOException {
        Connection c;
        Statement stmt = null;
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/airtrans", "postgres", "123");
        String sql =    "select date_part, count(date_part) from ( " +
                "select extract(month from cast(scheduled_departure as date)), status from flights " +
                "where status = 'Cancelled') as f " +
                "group by date_part;";

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        String[] columnNames = new String[] {"Месяц", "Количество отмен"};
        List<String> values = new ArrayList<String>();
        String[] months = new String[] {
                "Январь",
                "Февраль",
                "Март",
                "Апрель",
                "Май",
                "Июнь",
                "Июль",
                "Август",
                "Сентябрь",
                "Октябрь",
                "Ноябрь",
                "Декабрь"
        };

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        int k = 1;
        while(rs.next()) {
            values.add(months[rs.getInt(1) - 1]);
            values.add(rs.getString(2));
            dataset.addValue(Double.parseDouble(rs.getString(2)), (Comparable) k, (Comparable) months[rs.getInt(1) - 1]);
            ++k;
        }
        Excel.CreateExcelSheet(columnNames, values, "Query4.xls");
        Chart chart = new Chart();
        chart.CreateDemoPanelForFourthTask(dataset, "Количество отмен рейсов по месяцам", "Количество рейсов");
    }

    public static void FifthQuery() throws ClassNotFoundException, SQLException, IOException {
        Connection c;
        Statement stmt = null;
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/airtrans", "postgres", "123");
        String sql = "select f1.day, f1.cnt, f2.cnt from " +
                "(select extract(dow from scheduled_departure) as day, count(extract(dow from scheduled_departure)) as cnt " +
                "from flights join airports on flights.departure_airport = airports.airport_code " +
                "where airports.city->'ru' = $$\"Москва\"$$ " +
                "group by extract(dow from scheduled_departure) " +
                "order by extract(dow from scheduled_departure)) as f1 join " +
                "(select extract(dow from scheduled_arrival) as day, count(extract(dow from scheduled_arrival)) as cnt " +
                "from flights join airports on flights.departure_airport = airports.airport_code " +
                "where airports.city->'ru' = $$\"Москва\"$$ " +
                "group by extract(dow from scheduled_arrival) " +
                "order by extract(dow from scheduled_arrival)) as f2 on " +
                "f1.day = f2.day";

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        String[] weekDays = new String[] {
                "Воскресенье",
                "Понедельник",
                "Вторник",
                "Среда",
                "Четверг",
                "Пятница",
                "Суббота"
        };

        String[] columnNames = new String[] {"День недели", "Количество рейсов в Москву", "Количество рейсов из Москвы"};
        List<String> values = new ArrayList<String>();
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();

        int k = 1;
        while(rs.next()) {
            values.add(weekDays[rs.getInt(1)]);
            values.add(String.valueOf(rs.getInt(2)));
            values.add(String.valueOf(rs.getInt(3)));
            dataset1.addValue(Double.parseDouble(String.valueOf(rs.getInt(2))),
                    (Comparable) k, (Comparable) weekDays[rs.getInt(1)]);
            dataset2.addValue(Double.parseDouble(String.valueOf(rs.getInt(3))),
                    (Comparable) k, (Comparable) weekDays[rs.getInt(1)]);
            ++k;
        }
        Excel.CreateExcelSheet(columnNames, values, "Query5.xls");
        Chart chart1 = new Chart();
        chart1.CreateDemoPanelForFifthTask(dataset1, "Количество рейсов в Москву", "Количество рейсов");
        Chart chart2 = new Chart();
        chart2.CreateDemoPanelForFifthTask(dataset2, "Количество рейсов из Москвы", "Количество рейсов");
    }

    public static void SixthQuery(String model) throws ClassNotFoundException, SQLException {
        Connection c;
        Statement stmt = null;
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/airtrans", "postgres", "123");

        String sql = "delete from tickets where ticket_no in " +
                "(select ticket_no from ticket_flights where flight_id in " +
                "(select flight_id from flights where aircraft_code in " +
                "(select aircraft_code from aircrafts where model->'ru' " +
                "= $$ \"" +
                model +
                "\" $$)));";
        stmt = c.createStatement();
        stmt.executeUpdate(sql);

        sql = "delete from ticket_flights where flight_id in " +
                "(select flight_id from flights where aircraft_code in " +
                "(select aircraft_code from aircrafts where model->'ru' " +
                "= $$ \"" +
                model +
                "\" $$));";

        stmt = c.createStatement();
        stmt.executeUpdate(sql);

        sql = "delete from flights where aircraft_code in " +
                "(select aircraft_code from aircrafts where model->'ru' " +
                "= $$ \"" +
                model +
                "\"$$);";

        stmt = c.createStatement();
        stmt.executeUpdate(sql);

        stmt.close();
    }

    public static void SeventhQuery(String[] dates) throws ClassNotFoundException, SQLException {
        Connection c;
        Statement stmt = null;
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/airtrans", "postgres", "123");

        String[] numbers = dates[0].split("\\.");

        Calendar calendar = new GregorianCalendar(Integer.parseInt(numbers[2]), Integer.parseInt(numbers[1]) - 1,
                Integer.parseInt(numbers[0]));

        Date date = calendar.getTime();

        Timestamp time_start = new Timestamp(date.getTime());

        numbers = dates[1].split("\\.");

        calendar = new GregorianCalendar(Integer.parseInt(numbers[2]), Integer.parseInt(numbers[1]) - 1,
                Integer.parseInt(numbers[0]) + 1);

        date = calendar.getTime();

        Timestamp time_finish = new Timestamp(date.getTime());

        String sql = "update flights set status = 'Cancelled' where flight_id in " +
                "(select flight_id from flights join airports on flights.departure_airport = airports.airport_code " +
                "where " +
                "city->'ru' = $$ \"Москва\" $$ and " +
                "scheduled_departure >= timestamp '" +
                time_start.toString() + "' and " +
                "scheduled_arrival < timestamp '" + time_finish.toString() + "')";

        stmt = c.createStatement();
        stmt.executeUpdate(sql);

        sql = "update flights set status = 'Cancelled' where flight_id in " +
                "(select flight_id from flights join airports on flights.arrival_airport = airports.airport_code " +
                "where " +
                "city->'ru' = $$ \"Москва\" $$ and " +
                "scheduled_departure >= timestamp '" +
                time_start.toString() + "' and " +
                "scheduled_arrival < timestamp '" + time_finish.toString() + "')";

        stmt = c.createStatement();
        stmt.executeUpdate(sql);
        
        sql = "select extract(day from cast(scheduled_departure as date)), sum(amount) from flights join airports on " +
                "flights.departure_airport = airports.airport_code " +
                "join ticket_flights on flights.flight_id = ticket_flights.flight_id " +
                "where " +
                "city->'ru' = $$ \"Москва\" $$ and " +
                "scheduled_departure >= timestamp '2017-08-01 00:00:00.0' and " +
                "scheduled_arrival < timestamp '2017-08-16 00:00:00.0' " +
                "group by extract(day from cast(scheduled_departure as date));";

        stmt = c.createStatement();
        ResultSet rs1 = stmt.executeQuery(sql);
        sql = "select extract(day from cast(scheduled_departure as date)), sum(amount) from flights join airports on " +
                "flights.arrival_airport = airports.airport_code " +
                "join ticket_flights on flights.flight_id = ticket_flights.flight_id " +
                "where " +
                "city->'ru' = $$ \"Москва\" $$ and " +
                "scheduled_departure >= timestamp '2017-08-01 00:00:00.0' and " +
                "scheduled_arrival < timestamp '2017-08-16 00:00:00.0' " +
                "group by extract(day from cast(scheduled_departure as date));";
        stmt = c.createStatement();
        ResultSet rs2 = stmt.executeQuery(sql);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        int k = 1;
        while(rs1.next() && rs2.next()) {
            //System.out.println(rs1.getInt(1) + "    " + (rs1.getInt(2) + rs2.getInt(2)));
            dataset.addValue((double) (rs1.getInt(2) + rs2.getInt(2)),
                    (Comparable) k, (Comparable) rs1.getInt(1));
        }
        Chart chart = new Chart();
        chart.CreateDemoPanelForFourthTask(dataset, "Убыток, который теряют компании-перевозчики по дням",
                "Убыток");
    }

    public static void EightQuery(String[] data) throws ClassNotFoundException, SQLException {
        Connection c;
        Statement stmt = null;
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/airtrans", "postgres", "123");

        String sql = "select * from flights where flight_id = " + data[4];
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        c.setAutoCommit(false);

        String air_code = "";
        int flag = 0;

        char[] date = data[1].toCharArray();
        date[date.length - 3] = '.';
        char[] newDate = new char[date.length - 1];
        System.arraycopy(date, 0, newDate, 0, newDate.length);
        data[1] = new String(newDate);
        while (rs.next()) {
            try {
                Timestamp timestamp = (Timestamp) rs.getObject(3);
                if (timestamp.toString().equals(data[1]) && rs.getString(7).equals("Scheduled")) {
                    flag = 1;
                    air_code = rs.getString(8);
                    break;
                }
            } catch (Exception ignored) {
            }
        }

        stmt.close();
        if (flag == 1) {
            sql = "select * from seats where aircraft_code = $$" + air_code + "$$";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            flag = 0;
            while(rs.next()) {
                if (data[10].equals(rs.getString(2)) && data[5].equals(rs.getString(3))) {
                    flag = 1;
                    break;
                }
            }
        } else {
            System.out.println("I can`t insert(");
            System.exit(0);
        }

        if (flag == 0) {
            System.out.println("I can`t insert(");
            System.exit(0);
        } else {
            sql = "INSERT INTO bookings VALUES (?, ?::TIMESTAMPTZ, ?::NUMERIC(10, 2))";
            PreparedStatement preparedStatement = c.prepareStatement(sql);

            preparedStatement.setString(1, data[0]);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(data[1]));
            preparedStatement.setDouble(3, Double.parseDouble(data[2]));

            preparedStatement.executeUpdate();

            sql = "INSERT INTO ticket_flights VALUES (?, ?, ?, ?::NUMERIC(10, 2))";
            preparedStatement = c.prepareStatement(sql);

            preparedStatement.setString(1, data[3]);
            preparedStatement.setInt(2, Integer.parseInt(data[4]));
            preparedStatement.setString(3, data[5]);
            preparedStatement.setDouble(4, Double.parseDouble(data[6]));

            preparedStatement.executeUpdate();

            sql = "INSERT INTO tickets VALUES (?, ?, ?, ?, ?::JSON)";
            preparedStatement = c.prepareStatement(sql);

            preparedStatement.setString(1, data[3]);
            preparedStatement.setString(2, data[0]);
            preparedStatement.setString(3, data[7]);
            preparedStatement.setString(4, data[8]);

            String json = data[9];

            json = json.replace("\"\"", "\"");
            json = json.replace("\"{" , "{");
            json = json.replace("}\"" , "}");

            preparedStatement.setObject(5, json);

            preparedStatement.executeUpdate();
            System.out.println("insert successful");
        }
        c.commit();
    }
}

