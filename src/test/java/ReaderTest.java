import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {
    @Test
    void firstMethodToCreateClient() {
        for (int i = 0; i < 1000; ++i) {
            String name = "anime";

            switch (i % 3) {
                case 0:
                    IndividualType client1 = (IndividualType) Reader.firstMethodToCreateClient(name, i, "INDIVIDUAL", true);
                    assert client1 != null;
                    assertEquals(i, client1.getInn());
                    assertEquals("INDIVIDUAL", client1.getClientType());
                    break;
                case 1:
                    LegalEntityType client2 = (LegalEntityType) Reader.firstMethodToCreateClient(name, i, "LEGAL_ENTITY", true);
                    assert client2 != null;
                    assertEquals(i, client2.getInn());
                    assertEquals("LEGAL_ENTITY", client2.getClientType());
                    break;
                case 2:
                    HoldingType client3 = (HoldingType) Reader.firstMethodToCreateClient(name, i, "HOLDING", true);
                    assert client3 != null;
                    assertEquals(i, client3.getInn());
                    assertEquals("HOLDING", client3.getClientType());
                    break;
            }
        }
    }
}