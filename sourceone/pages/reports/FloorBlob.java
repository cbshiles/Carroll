package sourceone.pages.reports;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.time.*;

public class FloorBlob extends Blob{
    private boolean incoming; //true if blob is dealing with new entries
    private String summer, dater;
    public FloorBlob(boolean inc){
	incoming = inc;
	if (inc){
	    summer = "Item Cost";
	    dater = "Date Bought";
	} else {
	    summer = "Pay Off Amount";
	    dater = "Date Paid";
	}
	k = Key.floorKey.just(new String[]{"VIN", summer, dater, "Vehicle"});
    }

    public Input in(LocalDate a, LocalDate z)throws Exception{
	return new QueryIn(k, "WHERE "+sql(dater)+" >= '"+a+
			   "' AND "+sql(dater)+" <= '"+z+"'");
    }
	
    public Enterer ent(){
	return new Fnt();
    }

    private class Fnt implements Enterer{
	int sm, dt, vin, veh;

	public Fnt(){
	    sm = k.dex(summer);
	    dt = k.dex(dater);
	    vin = k.dex("VIN");
	    
	    veh = k.dex("Vehicle");
		// cur = key.dex("Curtailed");
	}

	public Object[] editEntry(Object[] o){

	    String desc = (incoming?"p":"s")+" - "+o[veh];
	    return new Object[] {
		o[dt],
		""+o[vin]+" - "+desc,
		incoming?0f:o[sm],
		incoming?o[sm]:0f,
		0f
	    };
	}

    }
	
}
