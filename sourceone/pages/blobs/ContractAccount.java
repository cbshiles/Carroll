package sourceone.pages.blobs;

import java.time.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.util.ArrayList;

public class ContractAccount extends CenterFile.Account{

    private boolean full;

    private Key r;

    private String date, summer, clz, tc = "Total Contract";
    
    public ContractAccount(boolean f){
	super((f?"Full":"Partial")+" Contract Account", null);

	String op;
	full=f;
	date="Date Bought";
	summer="Net Amount";
	if (full){/*summer=tc;*/ op=">";}
	else{/*summer="Gross Amount";*/ op="<";}
	r = Key.contractKey.just(new String[]{summer, "Amount of Payment", "Payments Made", date});
	clz = ""+sql(tc)+" "+op+" 0.01";
	loadBlobs(new Blob[]{new PurBlob(false), new PayBlob()});
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


    public class PayBlob extends Blob implements Enterer{

	public PayBlob(){
	    k = Key.sumKey;
	}

	public Enterer ent(){return this;}
	
	public Input in(LocalDate a, LocalDate z)throws Exception{
	    return pif.in(a, z, full);
	}

	public Object[] editEntry(Object[] o){return o;}
    }


    public static class BuildIn extends View{
	boolean full;
	ArrayList<Quail> ids=new ArrayList<Quail>();
	LocalDate a,z;

	public BuildIn(boolean f, LocalDate ao, LocalDate zo){
	    super(Key.sumKey);
	    full = f;
	    a=ao; z=zo;
	}

	public boolean sameTime(LocalDate ao, LocalDate zo){
	    return ao.equals(a) && zo.equals(z);
	}

	public boolean add(Quail q){ //try to had to an old one
	    int c = has(q);
	    boolean t = c > -1;
	    if (t) ids.get(c).add(q);
	    return t;
	}

	private int has(Quail q){ //check if the batch is already here
	    for (int x=ids.size()-1; x>=0; x--){
		if (q.match(ids.get(x))) return x;
	    }
	    return -1;
	}
	public void gnu(Quail q){ //add a gnu one, takes a NON NULL batch id
	    ids.add(q);
	}

	public void load(){
	    for (Quail q : ids){
		chunk(new Object[]{q.date, q.id, 0f, q.amount, 0f});
	    }
	}
    }

	public static class Quail{

	LocalDate date;
	String id;
	float amount;

	    public Quail(LocalDate d, float a, String i){
		amount=a;
		date=d;
		id=i;
	    }

	public boolean match(Quail b){
	    return id.equals(b.id);
	}


	public void add(Quail b){
	    amount += b.amount;
	}


    }
}
