package sourceone.pages.reports;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.time.*;

public class MonthlyReport extends Report{
    
    public MonthlyReport(Page p) throws Exception{
	super("Monthly", p, new Account[]{
		new PurchaseAccount(),
		new FPAccount(),
		new ReserveReport.ResAccount()});
	rKey =  Key.resKey;
	init();
    }

    public static class FPAccount extends Account{	
	FPAccount(){super("Floor Plan", new Blob[]{new FloorBlob(true), new FloorBlob(false)}, Key.flKey, false, false);}
	
	public float getStart(LocalDate ld) throws Exception{
	    Key r = Key.floorKey.just("Item Cost");
	    Grid g;
	    g = new Grid(r, new QueryIn(r,
					"WHERE Date_Bought < '"+ld+"' AND ( Date_Paid IS NULL OR Date_Paid >= '"+ld
					+"' );"));
	    g.pull();
	    return g.floatSum("Item Cost");
	}
	@Override
	public Object[] makeChunk(View tv, String m){
	    float deb = tv.floatSum("Purchase Amount");
	    float cred = tv.floatSum("Sale Amount");
	    return new Object[]{null, "Current Period Change: "+m, deb, cred, deb-cred};
	}
    }
}

