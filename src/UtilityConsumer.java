public interface UtilityConsumer {
    enum Utility {ELECTRICITY, WATER, INTERNET}

    int demandFor(Utility u);

    void receiveUtility(Utility u, int amount);
}
