package sourceone.pages.reports;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.time.*;

public class PayAccount extends Account{	
	boolean full;
	Key r;
	String summer, date, clz;
	PayBlob pb;
	
	public PayAccount(boolean f){
	    super((f?"Full":"Partial")+" Contract Payments", new Blob[]{new PayBlob(f)}, Key.payKey);
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

    Object[] makeChunk(View tv, String month) {
	float tt = tv.floatSum("Inventory Reduction");
	return new Object[]{null, "End of "+month, tv.floatSum("Principle"), tv.floatSum("Interest"), tv.floatSum("Total Amount"), tt, total = -tt};
    }
}
