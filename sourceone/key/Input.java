package sourceone.key;

/**
Interface for connecting with {@link Cut} and {@link Key}.
Requires a getX() method for each type of data enumerated in {@link Kind}.
Also requires a hasEntries method, to tell whether the Input has at least 1 more entry available.
An entry corresponds to a row in a spreadsheet. 
 */

public interface Input{

    boolean hasEntries(); //has atleast 1 entry(row) left

    String getString() throws InputXcpt;

    int getInt() throws InputXcpt;

    float getFloat() throws InputXcpt;

    java.time.LocalDate getDate() throws InputXcpt;
    
}
