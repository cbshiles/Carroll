package sourceone.pages.blobs;

import java.time.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;

public class FloorAccount extends CenterFile.Account{

    public FloorAccount(){super("Floor Account", new Blob[]{new FBlob(true), new FBlob(false)});}

    public float getStart(LocalDate ld) throws Exception{
	Key r = Key.floorKey.just("Item Cost");
	Grid g;
	g = new Grid(r, new QueryIn(r,
				    "WHERE Date_Bought < '"+ld+"' AND ( Date_Paid IS NULL OR Date_Paid >= '"+ld
				    +"' );"));
	g.pull();
	return g.floatSum("Item Cost");
    }

    public static class FBlob extends Blob{
	private boolean incoming; //true if blob is dealing with new entries
	private String summer, dater;
	public FBlob(boolean inc){
	    incoming = inc;
	    if (inc){
		summer = "Item Cost";
		dater = "Date Bought";
	    } else {
		summer = "Pay Off Amount";
		dater = "Date Paid";
	    }
	    k = Key.floorKey.just(new String[]{"VIN", summer, dater});
	}

	public Input in(LocalDate a, LocalDate z)throws Exception{
	    return new QueryIn(k, "WHERE "+sql(dater)+" >= '"+a+
			       "' AND "+sql(dater)+" <= '"+z+"'");
	}
	
	public Enterer ent(){
	    return new Fnt();
	}

	private class Fnt implements Enterer{
	    int sm, dt, vin;

	    public Fnt(){
		sm = k.dex(summer);
		dt = k.dex(dater);
		vin = k.dex("VIN");
	    }

	    public Object[] editEntry(Object[] o){

		String desc = incoming?"purchase":"sale";
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
}
