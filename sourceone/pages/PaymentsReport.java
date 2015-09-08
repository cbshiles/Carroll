package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.blobs.*;
import java.time.*;

public class PaymentsReport extends CenterFile{
    
    public PaymentsReport(Page p) throws Exception{
	super("Payments", p, new Account[]{
		new PayAccount(true),
		new PayAccount(false)});
    }

    public static class PayAccount extends Account{	
	boolean full;
	Key r;
	String summer, date;
	PayBlob pb;

	
	PayAccount(boolean f){
	    super("Contract Payments", new Blob[]{new PayBlob(true), new PayBlob(false)}); 
	    full = f;
	    summer = "Net Amount";
	    date = "Date Bought";
	    r = Key.contractKey.just(new String[]{summer, "Amount of Payment", "Payments Made", date});
	}

	public float getStart(LocalDate ld) throws Exception{
	    Grid g = new Grid(r, new QueryIn(r,
					     "WHERE Date_Bought < '"+ld+"' AND ( Paid_Off IS NULL OR Paid_Off >= '"+ld
					     +"')"));  // AND "+clz));

	    //new clearview, holds names and current balance
	    View v = new View(new Key(new Cut[]{new FloatCut("Remaining balance")}), new Znt());
	    g.addOut(v);
	    g.go1();
	    return v.floatSum("Remaining balance");
	}

	public class Znt implements Enterer{
	    int tep, aop, pm;
	    public Znt(){
		tep = r.dex(summer);
		aop = r.dex("Amount of Payment");
		pm = r.dex("Payments Made");
	    }

	    public Object[] editEntry(Object[] o){
		return new Object[]{(float)o[tep] - (int)o[pm]*(float)o[aop]};
	    }
	}
    }
}

// public void doPif(int i, PayInFact pif){
// 	((ContractAccount)accounts[i]).addPif(pif);
// }

