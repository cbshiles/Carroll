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

    	@Override
	public Key sendKey(){ return new Key(new Cut[]{new StringCut("Date"), new StringCut("Transaction"), new StringCut("Principle"), new StringCut("Interest"), new StringCut("Total Amount")});}


    public static class PayAccount extends Account{	
	boolean full;
	Key r;
	String summer, date, clz;
	PayBlob pb;
	
	PayAccount(boolean f){
	    super((f?"Full":"Partial")+" Contract Payments", new Blob[]{new PayBlob(f)});
	    full = f;
	    summer = "Net Amount";
	    date = "Date Bought";
	    r = Key.contractKey.just(new String[]{summer, "Amount of Payment", "Payments Made", date});
	    String op = full?">":"<";
	    clz = "Total_Contract "+op+" 0.01";
	}

	public float getStart(LocalDate ld) throws Exception{
	    Grid g = new Grid(r, new QueryIn(r,
					     "WHERE Date_Bought < '"+ld+"' AND ( Paid_Off IS NULL OR Paid_Off >= '"+ld
					     +"') AND "+clz));

	    //new clearview, holds names and current balance
	    View v = new View(new Key(new Cut[]{new FloatCut("Remaining balance")}), new Znt());
	    g.addOut(v);
	    g.go1();
	    return v.floatSum("Remaining balance");
	}

	public class Znt implements Enterer{ //just for figuring remaining balance, could be replaced with for loop
	    int tep, aop, pm;
	    public Znt(){
		tep = r.dex(summer);
		aop = r.dex("Amount of Payment");
		pm = r.dex("Payments Made");
	    }

	    public Object[] editEntry(Object[] o){
		float rbal = (float)o[tep] - (int)o[pm]*(float)o[aop];
		return new Object[]{rbal};
	    }
	}

	@Override
	public View span(LocalDate a, LocalDate z) throws Exception{//can return null
	    if (a.isAfter(z)) return null;

	    View v = new View(Key.sumKey, null, null);

	    //v.chunk(new Object[]{a, "Beginning Balance", 0f, 0f, getStart(a)});
	
	    for(LocalDate n=a; n.isBefore(z); n = n.plusMonths(1)){
		LocalDate nz = n.plusDays(n.getMonth().maxLength() - 1);
		nz = nz.isBefore(z)?nz:z;

		View vi = new View(Key.sumKey);
		vi.addOut(v);
		for (Blob b: blobs){// tip: you need to use an input before making a new one
		    Grid g = new Grid(b.k, b.in(n, nz));
		    g.addOut(vi);
		    vi.switchEnts(b.ent());
		    g.go1();
		}
		vi.sort("Date", true);
		vi.push1();
		
		float deb = vi.floatSum("Debit Amt");
		float cred = vi.floatSum("Credit Amt");
//		v.chunk(new Object[]{null, "Current Period Change", deb, cred, deb+cred});
		v.chunk(new Object[]{null, "End of "+n.getMonth(), 0f, 0f, 0f});
	    }
	    v.chunk(new Object[]{z, "Ending Balance", v.floatSum("Debit Amt"), v.floatSum("Credit Amt"), v.floatSum("Balance")});

	    return v;
	}

    }
}

// public void doPif(int i, PayInFact pif){
// 	((ContractAccount)accounts[i]).addPif(pif);
// }

