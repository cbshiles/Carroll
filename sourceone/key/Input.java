package sourceone.key;
public interface Input{

    boolean hasEntries(); //has atleast 1 entry(row) left

    String getString() throws InputXcpt;

    int getInt() throws InputXcpt;

    float getFloat() throws InputXcpt;

    java.time.LocalDate getDate() throws InputXcpt;
    
}
