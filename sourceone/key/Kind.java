package sourceone.key;

/**
These are the types of data that can be processed by the Key system. 
Each {@link Kind} has a corresponding {@link Cut} subclass,
along with {@link Input} and {@link Output} methods.
 */

public enum Kind { 
    STRING, INT, FLOAT, DATE
}
