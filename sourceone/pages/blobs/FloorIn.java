package sourceone.pages.blobs;

import java.time.*;
import sourceone.key.*;
import sourceone.sql.*;

private class FloorIn extends Blob{
    public FloorIn(){super(fi);}

    public Enterer ent(){return new Ent();}

    public Input in(LocalDate a, LocalDate z)throws Exception{
	return new QueryIn(fi, "WHERE Date_Paid >= '"+a+"' AND Date_Paid <= '"+z);
    }

    int db, vin, veh, ic, cur;
    private class Ent implements Enterer{
	public Ent(){
	    db = key.dex("Date Bought");
	    vin = key.dex("VIN");
	    veh = key.dex("Vehicle");
	    ic = key.dex("Item Cost");
	    cur = key.dex("Curtailed");
	}

	//# how to deal with curtailment??
	public Object[] editEntry(Object[] o){
	    return new Object[]{
		o[db],
		"Floor Purchase - "+o[vin],
		0f,
		o[ic],
		0f
	    };
	}
    }
