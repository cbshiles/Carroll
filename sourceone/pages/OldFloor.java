package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;

public class OldFloor extends TablePage {
    public OldFloor (Page p){
	super("Old Floor", p);

	Key inKey = Key.floorKey.just(new String[]{"Date Bought","VIN", "Vehicle", "Item Cost", "Title", "Date Paid", "Pay Off Amount"}); //no id

	try {
	    Input in = new QueryIn(inKey, "WHERE Date_Paid IS NOT NULL;");
	    g = new Grid(inKey, in);
	    g.pull();
	    g.addView(null, new Cut[]{new FloatCut("Profit")}, new Ent(inKey));
	    pushTable();
	} catch (Exception e){System.err.println("***"+e); e.printStackTrace(); kill(); return;}

	setSize(1000, 600);
	setVisible(true);
    }

    private class Ent implements Enterer{
	Key k;
	int db, vin, veh, ic, ttl, dp, poa;

	public Ent(Key kk){
	    k = kk;
	    db = k.dex("Date Bought");
	    vin = k.dex("VIN");
	    veh = k.dex("Vehicle");
	    ic = k.dex("Item Cost");
	    ttl = k.dex("Title");
	    dp = k.dex("Date Paid");
	    poa = k.dex("Pay Off Amount");
	}

	public Object[] editEntry(Object[] o){
	    return new Object[]{
		(float)o[poa] - (float)o[ic]
	    };
	}
    }
}
