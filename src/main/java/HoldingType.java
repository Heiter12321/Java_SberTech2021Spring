public class HoldingType {
    private final String name;
    private final int inn;
    private final String clientType;
    private final boolean isSanctioned;

    public HoldingType(String name, int inn, String clientType, boolean isSanctioned) {
        this.name = name;
        this.inn = inn;
        this.clientType = clientType;
        this.isSanctioned = isSanctioned;
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
