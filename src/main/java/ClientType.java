enum ClientType {
    INDIVIDUAL{
        public Client createClient(String[] args) {
            return new IndividualType(args);
        }
    },
    LEGAL_ENTITY{
        public Client createClient(String[] args) {
            return new LegalEntityType(args);
        }
    },
    HOLDING{
        public Client createClient(String[] args) {
            return new HoldingType(args);
        }
    };

    public abstract Client createClient(String[] args);
}