import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {
    @Test
    void createIndividualTypeClient() throws Exception {
        String[] args = new String[]{"individual", "1", "INDIVIDUAL", "true"};

        SwitchCaseClientFactory switchCaseClientFactory = new SwitchCaseClientFactory(args);

        assertEquals(IndividualType.class.getName(), switchCaseClientFactory.firstMethodToCreateClient().getClass().getName());
    }

    @Test
    void createHoldingTypeClient() throws Exception {
        String[] args = new String[]{"holding", "1", "HOLDING", "true"};

        SwitchCaseClientFactory switchCaseClientFactory = new SwitchCaseClientFactory(args);

        assertEquals(HoldingType.class.getName(), switchCaseClientFactory.firstMethodToCreateClient().getClass().getName());
    }

    @Test
    void createLegalEntityTypeClient() throws Exception {
        String[] args = new String[]{"legal_entity", "1", "LEGAL_ENTITY", "false"};

        SwitchCaseClientFactory switchCaseClientFactory = new SwitchCaseClientFactory(args);

        assertEquals(LegalEntityType.class.getName(), switchCaseClientFactory.firstMethodToCreateClient().getClass().getName());
    }
}