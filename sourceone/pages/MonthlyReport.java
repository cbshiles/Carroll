package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.blobs.*;
import java.time.*;

public class MonthlyReport extends CenterFile{
    
    public MonthlyReport(Page p) throws Exception{
	super("Monthly", p, new Account[]{
		new PurchaseAccount(),
		new FPayAccount(),
		new ReserveReport.ResAccount()});

//	 PayInFact pif = new PayInFact();
//	 doPif(0, pif);
//	 System.out.println("OWWW");
//	 doPif(1, pif);
	dew();
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


	
    }
}

