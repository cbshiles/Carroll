package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import java.time.*;

public class MonthlyReport extends TablePage{

    Key fi = Key.floorKey.just(new String[]{"Date Bought", "VIN", "Vehicle", "Item Cost", "Curtailed"});
    
    public MonthlyReport(Page p) throws Exception{
	super("Monthly", p);
    }

    private class FloorIn extends Blob{
	public FloorIn(){super(fi);}

	int db, vin, veh, ic, cur;
	private class Ent implements Enterer{
	    public Ent(){
		db = key.dex("Date Bought");
		vin = key.dex("VIN");
		veh = key.dex("Vehicle");
		ic = key.dex("Item Cost");
		cur = key.dex("Curtailed");
	    }
	    public Object[] editEntry(Object[] o){
		return new Object[]{
		    o[db],
		    "Floor Purchase - "+o[vin],
		    
		    
	    }
	}
    }
}

    // private class FloorIn extends Blob{
    // 	public FloorIn(){super(k);}

    // 	private class Ent implements Enterer{
    // 	    public Ent(){

    // 	    }
    // 	    public Object[] editEntry(Object[] o){
    // 	    }
    // 	}
    // }
