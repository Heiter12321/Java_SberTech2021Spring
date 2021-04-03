import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Reader {
    public static void main(String[] args) {
        String inputFileName = "persons.json";
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get(inputFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
