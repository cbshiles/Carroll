package sourceone.pages.reports;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.time.*;

public class PurchaseAccount extends Account{

    public PurchaseAccount(){super("Contract Purchases", new Blob[]{/*new FloorBlob(true),*/ new PurBlob(true), new PurBlob(false)}, Key.purKey);} 

    public float getStart(LocalDate ld)throws Exception{
	return 0f;
    }

	@Override
	public Object[] makeChunk(View tv, String m){
	    float deb = tv.floatSum("Debit (Purchase Amount)");
	    float cred = tv.floatSum("Credit");
	    return new Object[]{null, "Current Period Change: "+m, deb, cred, deb-cred};
	}
}
