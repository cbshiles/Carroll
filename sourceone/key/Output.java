package sourceone.key;
public interface Output{
    
    void put(String x);
    
    void put(int x);
    
    void put(float x);
    
    void put(java.time.LocalDate x);

    void endEntry();

    void close();
}
