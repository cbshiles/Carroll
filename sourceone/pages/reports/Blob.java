package sourceone.pages.reports;

import java.time.*;
import sourceone.key.*;

public abstract class Blob {

    static Key custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});
    protected static Key fi = Key.floorKey.just(new String[]{"Date Bought", "VIN", "Vehicle", "Item Cost", "Curtailed"});

    public Key k; //need to set k for all instantial subclasses
    
    protected abstract Input in(LocalDate a, LocalDate z)throws Exception;

    protected abstract Enterer ent();

    public Grid grid(LocalDate a, LocalDate z)throws Exception{
	return new Grid(k, in(a, z));
    }

    protected String sql(String b4){return b4.trim().replaceAll("\\s", "_");}
}
