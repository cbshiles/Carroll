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
	    g.clearView(new Cut[]{new DateCut("Date Bought", "NOT NULL"),
				  new StringCut(31, "VIN", "NOT NULL"),
				  new StringCut(127, "Vehicle", "NOT NULL"),
				  new FloatCut("Item Cost", "NOT NULL"),
				  new StringCut("Title"),
				  new DateCut("Date Paid"),
				  new FloatCut("Pay Off Amount"),
				  new FloatCut("Profit")},
		new Ent(inKey));
	    pushTable();
	} catch (Exception e){System.err.println("***"+e); e.printStackTrace(); kill(); return;}

	tablePlace();
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

	public String titleStatus(int t){
	    switch(t){
	     case 0: return "Pending";
	     case 1: return "Yes";
	     case 2: return "Never";
	     case 3: return "Nope";
	    }
	    return "Invalid code";
	}

	public Object[] editEntry(Object[] o){
	    return new Object[]{
		o[db], o[vin], o[veh], o[ic],
		titleStatus((int)o[ttl]),
		o[dp], o[poa],
		(float)o[poa] - (float)o[ic]
	    };
	}
    }
}
