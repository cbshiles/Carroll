package sourceone.key;
public interface StringSource {

    boolean hasEntries();

    String get() throws InputXcpt;
}
