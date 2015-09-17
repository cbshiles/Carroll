package sourceone.pages.reports;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.time.*;

public class MonthlyReport extends Report{
    
    public MonthlyReport(Page p) throws Exception{
	super("Monthly", p, new Account[]{
		new PurchaseAccount(),
		new FPayAccount(),
		new ReserveReport.ResAccount()});
	rKey =  Key.resKey;
	init();
    }

    public static class FPayAccount extends Account{	
	FPayAccount(){super("Floor Plan Payoffs", new Blob[]{new FloorBlob(false)});}
	
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
	public Object[] makeChunk(View tv){
	    float deb = tv.floatSum("Debit");
	    float cred = tv.floatSum("Credit");
	    return new Object[]{null, "Current Period Change", deb, cred, deb-cred};
	}
    }
}

