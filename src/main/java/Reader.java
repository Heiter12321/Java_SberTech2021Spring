import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Reader {
    public static void main(String[] args) throws IOException {
        String inputFileName = "persons.json";
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get(inputFileName));
            JsonObject parser = JsonParser.parseReader(reader).getAsJsonObject();

            for (int i = 0; i < 3; ++i) {
                JsonObject person = (JsonObject) parser.get((i + 1) + " person");
                String name = person.get("name").getAsString();
                int inn = person.get("inn").getAsInt();
                String clientType = person.get("clientType").getAsString();
                boolean isSanctioned = person.get("isSanctioned").getAsBoolean();

                // first method

                Object client = firstMethodToCreateClient(name, inn, clientType, isSanctioned);

                // second method

                Object newClient = ClientType.valueOf(clientType).createClient(name, inn, clientType, isSanctioned);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object firstMethodToCreateClient(String name, int inn, String clientType, boolean isSanctioned) {
        switch (clientType) {
            case ("INDIVIDUAL"):
                return new IndividualType(name, inn, clientType, isSanctioned);
            case ("LEGAL_ENTITY"):
                return new LegalEntityType(name, inn, clientType, isSanctioned);
            case ("HOLDING"):
                return new HoldingType(name, inn, clientType, isSanctioned);
            default:
                return null;
        }
    }


}
