package sourceone.pages.blobs;

import java.time.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;

public class ContractAccount extends CenterFile.Account{

    private boolean full;

    private Key r;

    private String date, summer, op, clz, tc = "Total Contract";

    private PayInFact pif = new PayInFact();
    
    public ContractAccount(boolean f){
	super("Contract Account", null);
	full=f;
	if (full){summer=tc; op=">"; date="Date Bought";}
	else{summer="Gross Amount"; op="<"; date="Paid Off";}
	r = Key.contractKey.just(new String[]{summer, "Amount of Payment", "Payments Made", date});
	clz = ""+sql(tc)+" "+op+" 0.01";
	loadBlobs(new Blob[]{new PurBlob(), new PayBlob()});
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

	public PayBlob(){
	    k = Key.sumKey;
	}

	public Enterer ent(){return this;}
	
	public Input in(LocalDate a, LocalDate z)throws Exception{
	    return pif.in(a, z, full);
	}

	public Object[] editEntry(Object[] o){return null;}


    }

    private static class PayInFact{
	
	LocalDate a, z;
	BuildIn fullIn, partIn;
	
	public PayInFact(){
	    a=z=null;
	    fullIn=partIn=null;
	}

	public Input in(LocalDate ao, LocalDate zo, boolean full)throws Exception{//no nulls

	    Input ni = full?fullIn:partIn;
	    
	    if (!(ao.equals(a) && zo.equals(z))){
		a=ao; z=zo;
		Grid g = new Grid(Key.paymentKey, new QueryIn
				  (Key.paymentKey, "WHERE Day >= '"+a+"' AND Day <= '"+z+"'"));
		g.pull();

		fullIn = new BuildIn(true);
		partIn = new BuildIn(false);
		
		for (Object[] i: g.data){

		    String bid = i[4];
		    //#should never be a problem, but batch cant be someone's name
		    //bckwrds search quicker
		    System.out.println(i[4]);
		    //if it belongs to an already existing batch
		    //is it a full or part
		    //what data do we need
		    //put it where we need it
		    //in?.chunk();
		}
		
		
		//fill up the views		
	    }
	    return new ViewInput(ni);		
	}
	
    }

    private static class BuildIn extends View{
	boolean full;

	public BuildIn(boolean f){
	    super(Key.sumKey);
	    full = f;
	}

	public boolean add //try to had to an old one

	    public int has //check if the batch is already here

	    public void gnu //add a gnu one, takes a possibly null batch id

}
