package objectville.cells;

public interface ServiceConsumer {
    enum Service {SECURITY, HEALTH, EDUCATION}

    void receiveService(Service s);

    boolean hasService(Service s);
}
