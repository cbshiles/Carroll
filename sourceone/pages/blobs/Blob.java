package sourceone.pages.blobs;

import java.time.*;
import sourceone.key.*;

public abstract class Blob {

    static Key custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});
    protected static Key contNKey = Key.contractKey.just(new String[] {"Reserve", "Date Bought"});
    protected static Key kn = Blob.custKey.add(contNKey.cuts);
    protected static Key contOKey = Key.contractKey.just(new String[] {"Reserve", "Paid Off"});
    protected static Key ko = Blob.custKey.add(contOKey.cuts);
    protected static Key fi = Key.floorKey.just(new String[]{"Date Bought", "VIN", "Vehicle", "Item Cost", "Curtailed"});
    
    public Key key;
    public Blob(Key k){key=k;}

    public abstract Input in(LocalDate a, LocalDate z)throws Exception;

    public abstract Enterer ent();

    public abstract float getStart(LocalDate ld);
}
