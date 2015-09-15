package sourceone.pages.blobs;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.time.*;

public class PayAccount extends CenterFile.Account{	
	boolean full;
	Key r;
	String summer, date, clz;
	PayBlob pb;
	
	public PayAccount(boolean f){
	    super((f?"Full":"Partial")+" Contract Payments", new Blob[]{new PayBlob(f)});
	    full = f;
	    summer = "Net Amount";
	    date = "Date Bought";
	    r = Key.contractKey.just(new String[]{"Total Contract", "Gross Amount", summer, "Amount of Payment", "Payments Made", date});
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
	    int tzp, aop, pm, tc, ga;
	    public Znt(){
		tc = r.dex("Total Contract");
		ga = r.dex("Gross Amount");
		tzp = r.dex(summer);
		aop = r.dex("Amount of Payment");
		pm = r.dex("Payments Made");
	    }

	    public Object[] editEntry(Object[] o){
		float tot = (float)o[tc];
		float tep = (tot > .01)?tot:(float)o[ga];
		float rbal = tep - (int)o[pm]*(float)o[aop];
		return new Object[]{rbal};
	    }
	}

	@Override
	public View span(LocalDate a, LocalDate z) throws Exception{//can return null
	    if (a.isAfter(z)) return null;

	    View v = new View(InvReport.cba, null, null);
	    System.out.println(getStart(a));
	    
	    v.chunk(new Object[]{a, "Beginning Balance", 0f, 0f, 0f, 0f, getStart(a)});
	
	    for(LocalDate n=a; n.isBefore(z); n = n.plusMonths(1)){
		LocalDate nz = n.plusDays(n.getMonth().maxLength() - 1);
		nz = nz.isBefore(z)?nz:z;

		View vi = new View(InvReport.cba);
		vi.addOut(v);
		for (Blob b: blobs){// tip: you need to use an input before making a new one
		    Grid g = new Grid(b.k, b.in(n, nz));
		    g.addOut(vi);
		    vi.switchEnts(b.ent());
		    g.go1();
		}
		vi.sort("Date", true);
		vi.push1();
		
		float deb = vi.floatSum("Principle");
		float cred = vi.floatSum("Interest");
//		v.chunk(new Object[]{null, "Current Period Change", deb, cred, deb+cred});
		v.chunk(new Object[]{null, "End of "+n.getMonth(), 0f, 0f, 0f, 0f, 0f}); //# not sure wat tado here
	    }
	    v.chunk(new Object[]{z, "Ending Balance", v.floatSum("Principle"), v.floatSum("Interest"), v.floatSum("Total Amount"), 0f ,0f});

	    return v;
	}

    }
