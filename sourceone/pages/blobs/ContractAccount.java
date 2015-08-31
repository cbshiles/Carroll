package sourceone.pages.blobs;

import java.time.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;

public class ContractAccount extends CenterFile.Account{

    private boolean full;

    private Key r;

    private String date, summer, op, clz, tc = "Total Contract";
    
    public ContractAccount(boolean f){
	super("Contract Account", null);
	full=f;
	if (full){summer=tc; op=">"; date="Date Bought";}
	else{summer="Gross Amount"; op="<"; date="Paid Off";}
	r = Key.contractKey.just(new String[]{summer, "Amount of Payment", "Payments Made", date});
	clz = ""+sql(tc)+" "+op+" 0.01";
	loadBlobs(new Blob[]{new PurBlob()}); //payblub
    }

    public float getStart(LocalDate ld) throws Exception{
	Grid g = new Grid(r, new QueryIn(r,
					 "WHERE Date_Bought < '"+ld+"' AND ( Paid_Off IS NULL OR Paid_Off >= '"+ld
					 +"') AND "+clz));

	//new clearview, holds names and current balance
	View v = new View(new Key(new Cut[]{new FloatCut("Remaining balance")}), new Znt());
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

    public class PurBlob extends Blob implements Enterer{

	int ln, fn, sm, dt;
	public PurBlob(){
	    k = custKey.add(r.cuts);
	    ln = k.dex("Last Name");
	    fn = k.dex("First Name");
	    sm = k.dex(summer);
	    dt = k.dex(date);
	}

	public Enterer ent(){return this;}

	public Input in(LocalDate a, LocalDate z)throws Exception{
	    return new QueryIn
		(custKey, r,
		 "WHERE Contracts."+sql(date)+" >= '"+a+
		 "' AND Contracts."+sql(date)+" <= '"+z+
		 "' AND Contracts.Customer_ID = Customers.ID AND "+clz);
	}

	public Object[] editEntry(Object[] o){
	    
	    return new Object[] {
		o[dt],
		"New contract: "+o[ln]+", "+o[fn],
		o[sm],
		0f,
		0f
	    };
	}
    }

    public class PayBlob extends Blob implements Enterer{

	public Enterer ent(){return this;}
	
	public Input in(LocalDate a, LocalDate z)throws Exception{
	    return new QueryIn
		(Key.paymentKey,
		 "WHERE Day >= '"+a+"' AND Day <= '"+z);
	}

	public Object[] editEntry(Object[] o){return null;}
    }

}
