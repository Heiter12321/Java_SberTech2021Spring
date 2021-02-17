enum ClientType {
    INDIVIDUAL{
        public Object createClient(String name, int inn, String clientType, boolean isSanctioned) {
            return new IndividualType(name, inn,clientType, isSanctioned);
        }
    },
    LEGAL_ENTITY{
        public Object createClient(String name, int inn, String clientType, boolean isSanctioned) {
            return new LegalEntityType(name, inn,clientType, isSanctioned);
        }
    },
    HOLDING{
        public Object createClient(String name, int inn, String clientType, boolean isSanctioned) {
            return new HoldingType(name, inn,clientType, isSanctioned);
        }
    };

    public abstract Object createClient(String name, int inn, String clientType, boolean isSanctioned);
}