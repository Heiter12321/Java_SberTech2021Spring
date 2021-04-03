public class SwitchCaseClientFactory {
    private final String[] args;

    public SwitchCaseClientFactory(String[] args) {
        this.args = args;
    }

    public Client firstMethodToCreateClient() throws Exception {
        switch (args[2]) {
            case ("INDIVIDUAL"):
                return new IndividualType(args);
            case ("LEGAL_ENTITY"):
                return new LegalEntityType(args);
            case ("HOLDING"):
                return new HoldingType(args);
            default:
                throw new Exception("Invalid type");
        }
    }
}
