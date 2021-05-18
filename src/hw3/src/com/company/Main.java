package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        CreateDatabase();
        FillDatabase();
        String[] dates = new String[] {"01.08.2017", "15.08.2017"};

        String[] data = new String[] {
                "228322",                                          // book_ref
                "2017-09-10 09:50:00+03",                          // book_date
                "1488",                                            // total_amount
                "2283221488228",                                   // ticket_no
                "1185",                                            // flight_id
                "Economy",                                         // fare_conditions
                "228",                                             // amount
                "1488 228322",                                     // passenger_id
                "Vasya Pupkin",                                    // passenger_name
                "{\"email\": \"kukuruznik228@phystech.edu\"}",     // contact_data
                "12A"                                              // seat_no
        };

        Queries.FirstQuery();
        Queries.SecondQuery();
        Queries.ThirdQuery();
        Queries.FourthQuery();
        Queries.FifthQuery();
        Queries.SixthQuery("Боинг 777-300");
        Queries.SeventhQuery(dates);
        Queries.EightQuery(data);

        DropDatabase();
    }

    public static void CreateDatabase() {
        Connection c;
        Statement stmt;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/airtrans", "postgres", "123");
            c.setAutoCommit(true);
            String sql;

            stmt = c.createStatement();
            c.setAutoCommit(false);

            sql = "CREATE TABLE aircrafts                        " +
                    "(aircraft_code     CHAR(3)        NOT NULL, " +
                    " model             JSONB          NOT NULL, " +
                    " range             INTEGER        NOT NULL);" +
                  "CREATE TABLE airports                         " +
                    "(airport_code      CHAR(3)        NOT NULL, " +
                    " airport_name      JSONB          NOT NULL, " +
                    " city              JSONB          NOT NULL, " +
                    " coordinates       POINT          NOT NULL, " +
                    " timezone          TEXT           NOT NULL);" +
                  "CREATE TABLE boarding_passes                  " +
                    "(ticket_no         CHAR(13)       NOT NULL, " +
                    " flight_id         INTEGER        NOT NULL, " +
                    " boarding_no       INTEGER        NOT NULL, " +
                    " seat_no           VARCHAR(4)     NOT NULL);" +
                  "CREATE TABLE bookings                         " +
                    "(book_ref          CHAR(6)        NOT NULL, " +
                    " book_date         TIMESTAMPTZ    NOT NULL, " +
                    " total_amount      NUMERIC(10, 2) NOT NULL);" +
                  "CREATE TABLE flights                          " +
                    "(flight_id         SERIAL         NOT NULL, " +
                    " flight_no         CHAR(6)        NOT NULL, " +
                    " scheduled_departure TIMESTAMPTZ  NOT NULL, " +
                    " scheduled_arrival   TIMESTAMPTZ  NOT NULL, " +
                    " departure_airport CHAR(3)        NOT NULL, " +
                    " arrival_airport   CHAR(3)        NOT NULL, " +
                    " status            VARCHAR(20)    NOT NULL, " +
                    " aircraft_code     CHAR(3)        NOT NULL, " +
                    " actual_departure  TIMESTAMPTZ            , " +
                    " actual_arrival    TIMESTAMPTZ            );" +
                  "CREATE TABLE seats                            " +
                    "(aircraft_code     CHAR(3)        NOT NULL, " +
                    " seat_no           VARCHAR(4)     NOT NULL, " +
                    " fare_conditions   VARCHAR(10)    NOT NULL);" +
                  "CREATE TABLE ticket_flights                   " +
                    "(ticket_no         CHAR(13)       NOT NULL, " +
                    " flight_id         INTEGER        NOT NULL, " +
                    " fare_conditions   VARCHAR(10)    NOT NULL, " +
                    " amount            NUMERIC(10, 2) NOT NULL);" +
                  "CREATE TABLE tickets                          " +
                    "(ticket_no         CHAR(13)       NOT NULL, " +
                    " book_ref          CHAR(6)        NOT NULL, " +
                    " passenger_id      VARCHAR(20)    NOT NULL, " +
                    " passenger_name    TEXT           NOT NULL, " +
                    " contact_data      JSONB                  );";

            stmt.executeUpdate(sql);
            System.out.println("create airtrans");

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void DropDatabase() {
        Connection c;
        Statement stmt;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/airtrans", "postgres", "123");
            c.setAutoCommit(true);
            String sql;

            stmt = c.createStatement();
            c.setAutoCommit(false);

            sql =   "DROP TABLE aircrafts;          " +
                    "DROP TABLE airports;           " +
                    "DROP TABLE boarding_passes;    " +
                    "DROP TABLE bookings;           " +
                    "DROP TABLE flights;            " +
                    "DROP TABLE seats;              " +
                    "DROP TABLE ticket_flights;     " +
                    "DROP TABLE tickets;            ";

            stmt.executeUpdate(sql);
            System.out.println("DROP all tables");

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void FillDatabase() {
        Connection c;
        Statement stmt;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/airtrans", "postgres", "123");
            c.setAutoCommit(true);
            String sql;

            stmt = c.createStatement();
            c.setAutoCommit(false);

            BufferedReader br = new BufferedReader(new FileReader("aircrafts_data.csv"));

            String[] cols;
            while(br.ready()){
                cols = br.readLine().split(",");

                sql = "INSERT INTO aircrafts VALUES (?, ?::JSON, ?)";
                PreparedStatement preparedStatement = c.prepareStatement(sql);

                String json = cols[1] + ", " + cols[2];
                json = json.replace("\"\"", "\"");
                json = json.replace("\"{" , "{");
                json = json.replace("}\"" , "}");

                preparedStatement.setString(1, cols[0]);
                preparedStatement.setObject(2, json);
                preparedStatement.setInt(3, Integer.parseInt(cols[3]));

                preparedStatement.executeUpdate();
            }
            c.commit();

            System.out.println("END aircrafts");

            br = new BufferedReader(new FileReader("airports_data.csv"));

            while(br.ready()){
                cols = br.readLine().split(",");

                sql = "INSERT INTO airports VALUES (?, ?::JSON, ?::JSON, ?::POINT, ?)";
                PreparedStatement preparedStatement = c.prepareStatement(sql);

                String json1 = cols[1] + ", " + cols[2];
                String json2 = cols[3] + ", " + cols[4];
                String point = (cols[5] + ", " + cols[6]);
                point = point.replace("\"\"", "\"");
                point = point.replace("\"(", "(");
                point = point.replace(")\"", ")");


                json1 = json1.replace("\"\"", "\"");
                json1 = json1.replace("\"{" , "{");
                json1 = json1.replace("}\"" , "}");
                json2 = json2.replace("\"\"", "\"");
                json2 = json2.replace("\"{" , "{");
                json2 = json2.replace("}\"" , "}");

                preparedStatement.setString(1, cols[0]);
                preparedStatement.setObject(2, json1);
                preparedStatement.setObject(3, json2);
                preparedStatement.setObject(4, point);
                preparedStatement.setString(5, cols[7]);

                preparedStatement.executeUpdate();
            }

            c.commit();

            System.out.println("END airports");

            br = new BufferedReader(new FileReader("boarding_passes.csv"));
            while(br.ready()){
                cols = br.readLine().split(",");


                sql = "INSERT INTO boarding_passes VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = c.prepareStatement(sql);

                preparedStatement.setString(1, cols[0]);
                preparedStatement.setInt(2, Integer.parseInt(cols[1]));
                preparedStatement.setInt(3, Integer.parseInt(cols[2]));
                preparedStatement.setString(4, cols[3]);

                preparedStatement.executeUpdate();
            }

            System.out.println("END boarding_passes");

            c.commit();

            br = new BufferedReader(new FileReader("bookings.csv"));

            while(br.ready()){
                cols = br.readLine().split(",");

                sql = "INSERT INTO bookings VALUES (?, ?::TIMESTAMPTZ, ?::NUMERIC(10, 2))";
                PreparedStatement preparedStatement = c.prepareStatement(sql);

                TimeZone tz = TimeZone.getTimeZone(cols[1]);

                char[] chr = new char[cols[1].length() - 3];
                cols[1].getChars(0, cols[1].length() - 4, chr, 0);
                String str = new String(chr);

                Timestamp time = Timestamp.valueOf(str);
                preparedStatement.setString(1, cols[0]);
                preparedStatement.setTimestamp(2, time, Calendar.getInstance(tz));
                preparedStatement.setDouble(3, Double.parseDouble(cols[2]));

                preparedStatement.executeUpdate();
            }

            c.commit();
            System.out.println("END bookings");

            br = new BufferedReader(new FileReader("flights.csv"));

            while(br.ready()){
                cols = br.readLine().split(",");

                sql = "INSERT INTO flights VALUES (?, ?, ?::TIMESTAMPTZ, ?::TIMESTAMPTZ, ?, ?, ?, ?, ?::TIMESTAMPTZ, ?::TIMESTAMPTZ)";
                PreparedStatement preparedStatement = c.prepareStatement(sql);

                preparedStatement.setInt(1, Integer.parseInt(cols[0]));
                preparedStatement.setString(2, cols[1]);

                TimeZone tz = TimeZone.getTimeZone(cols[2]);
                char[] chr = new char[cols[2].length() - 3];
                cols[2].getChars(0, cols[2].length() - 4, chr, 0);
                String str = new String(chr);
                Timestamp time = Timestamp.valueOf(str);
                preparedStatement.setTimestamp(3, time, Calendar.getInstance(tz));

                tz = TimeZone.getTimeZone(cols[3]);
                chr = new char[cols[3].length() - 3];
                cols[3].getChars(0, cols[3].length() - 4, chr, 0);
                str = new String(chr);
                time = Timestamp.valueOf(str);
                preparedStatement.setTimestamp(4, time, Calendar.getInstance(tz));

                preparedStatement.setString(5, cols[4]);
                preparedStatement.setString(6, cols[5]);
                preparedStatement.setString(7, cols[6]);
                preparedStatement.setString(8, cols[7]);

                if (cols.length == 10) {
                    tz = TimeZone.getTimeZone(cols[8]);
                    chr = new char[cols[8].length() - 3];
                    cols[8].getChars(0, cols[8].length() - 4, chr, 0);
                    str = new String(chr);
                    time = Timestamp.valueOf(str);
                    preparedStatement.setTimestamp(9, time, Calendar.getInstance(tz));

                    tz = TimeZone.getTimeZone(cols[9]);
                    chr = new char[cols[9].length() - 3];
                    cols[9].getChars(0, cols[9].length() - 4, chr, 0);
                    str = new String(chr);
                    time = Timestamp.valueOf(str);
                    preparedStatement.setTimestamp(10, time, Calendar.getInstance(tz));
                } else {
                    preparedStatement.setNull(9, Types.NULL);
                    preparedStatement.setNull(10, Types.NULL);

                }

                preparedStatement.executeUpdate();
            }

            System.out.println("END flights");

            c.commit();
            br = new BufferedReader(new FileReader("seats.csv"));

            while(br.ready()){
                cols = br.readLine().split(",");

                sql = "INSERT INTO seats VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = c.prepareStatement(sql);

                preparedStatement.setString(1, cols[0]);
                preparedStatement.setString(2, cols[1]);
                preparedStatement.setString(3, cols[2]);

                preparedStatement.executeUpdate();
            }

            c.commit();

            System.out.println("END seats");

            br = new BufferedReader(new FileReader("ticket_flights.csv"));

            while(br.ready()){
                cols = br.readLine().split(",");

                sql = "INSERT INTO ticket_flights VALUES (?, ?, ?, ?::NUMERIC(10, 2))";
                PreparedStatement preparedStatement = c.prepareStatement(sql);

                preparedStatement.setString(1, cols[0]);
                preparedStatement.setInt(2, Integer.parseInt(cols[1]));
                preparedStatement.setString(3, cols[2]);
                preparedStatement.setDouble(4, Double.parseDouble(cols[3]));

                preparedStatement.executeUpdate();
            }

            c.commit();

            System.out.println("END ticket_flights");

            br = new BufferedReader(new FileReader("tickets.csv"));

            while(br.ready()){
                cols = br.readLine().split(",");

                sql = "INSERT INTO tickets VALUES (?, ?, ?, ?, ?::JSON)";
                PreparedStatement preparedStatement = c.prepareStatement(sql);

                preparedStatement.setString(1, cols[0]);
                preparedStatement.setString(2, cols[1]);
                preparedStatement.setString(3, cols[2]);
                preparedStatement.setString(4, cols[3]);

                String json = "";

                if (cols.length > 4) {
                    json = cols[4];
                }

                for (int i = 5; i < cols.length; ++i) {
                    json += ", " + cols[i];
                }

                json = json.replace("\"\"", "\"");
                json = json.replace("\"{" , "{");
                json = json.replace("}\"" , "}");

                preparedStatement.setObject(5, json);

                preparedStatement.executeUpdate();
            }

            c.commit();

            System.out.println("END tickets");

            br.close();

            System.out.println("FILL all tables");

            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
