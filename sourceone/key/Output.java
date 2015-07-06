package sourceone.key;

/**
   Interface for connecting with {@link Cut} and {@link Key}.
   Requires a put() method, overloaded for each type of data enumerated in {@link Kind}.
   Also requires an endEntry method, to be executed when a Key is done loading an entry into the Output.
   The close method is called to indicate the whole series of entries is done.
*/

public interface Output{
    
    void put(String x);
    
    void put(int x);
    
    void put(float x);
    
    void put(java.time.LocalDate x);

    void endEntry();

    Object close();
}
