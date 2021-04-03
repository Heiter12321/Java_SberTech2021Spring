public class IndividualType implements Client {
    private final String name;
    private final int inn;
    private final String clientType;
    private final boolean isSanctioned;

    public IndividualType(String[] args) {
        this.name = args[0];
        this.inn = Integer.parseInt(args[1]);
        this.clientType = args[2];
        this.isSanctioned = Boolean.parseBoolean(args[3]);
    }

    public String getName() {
        return name;
    }

    public int getInn() {
        return inn;
    }

    public String getClientType() {
        return clientType;
    }

    public boolean isSanctioned() {
        return isSanctioned;
    }
}
