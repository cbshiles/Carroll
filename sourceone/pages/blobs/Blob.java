package sourceone.pages.blobs;

import java.time.*;
import sourceone.key.*;

public abstract class Blob {

    static Key custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});
    protected static Key fi = Key.floorKey.just(new String[]{"Date Bought", "VIN", "Vehicle", "Item Cost", "Curtailed"});

    public Key k; //need to set k for all instantial subclasses
    
    public abstract Input in(LocalDate a, LocalDate z)throws Exception;

    public abstract Enterer ent();

    protected String sql(String b4){return b4.trim().replaceAll("\\s", "_");}
}
