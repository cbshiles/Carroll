package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.blobs.*;
//import static sourceone.pages.blobs.ContractAccount.*;
import java.time.*;
import java.util.ArrayList;
public class PayInFact{ //returns different inputs, but using same data
//src1, bebe	
    ArrayList<BuildIn[]> bins = new ArrayList();
    BuildIn fullIn, partIn;
	
    public PayInFact(){
	fullIn = partIn = null;
    }

    BuildIn which(int cid)throws Exception{
	float tc = SQLBot.bot.query1Float("SELECT Total_Contract FROM Contracts WHERE ID="+cid);
	return (tc > .01)?fullIn:partIn;
    }

    public BuildIn find(LocalDate a, LocalDate z, boolean full){
	for(int x=bins.size()-1; x>=0; x--){
	    if (bins.get(x)[0].sameTime(a, z)){
		return bins.get(x)[full?0:1];
	    }
	}
	return null;
    }

    public Input in(LocalDate ao, LocalDate zo, boolean full)throws Exception{//no nulls
	BuildIn b = find(ao, zo, full);
	if (b != null) return new ViewInput(b);
	else {
	    Grid g = new Grid(Key.paymentKey, new QueryIn
			      (Key.paymentKey, "WHERE Day >= '"+ao+"' AND Day <= '"+zo+"' ORDER BY Day"));
	    g.pull();

	    fullIn = new BuildIn(true,ao,zo);
	    partIn = new BuildIn(false,ao,zo);
		
	    for (Object[] i: g.data){

		//#should never be a problem, but batch cant be someone's name
		//bckwrds search quicker

		Quail qq = new Quail((LocalDate)i[2], (float)i[3], (String)i[4]);
		if (i[4] != null){
		    if (! fullIn.add(qq)){
			if (! partIn.add(qq)){
			    which((int)i[1]).gnu(qq);
			}
		    }
		} else {
		    String zename = SQLBot.bot.query1Name("SELECT Customers.First_Name, Customers.Last_Name FROM Customers, Contracts WHERE Contracts.ID="+(int)i[1]+" AND Customers.ID=Contracts.Customer_ID");
		    qq.figure();
		    which((int)i[1]).chunk(new Object[]{(LocalDate)i[2], zename+" - Payoff", qq.principle, qq.interest, 0f});
		}

	    }
	    fullIn.load(); partIn.load();
	    bins.add(new BuildIn[]{fullIn, partIn});
	}
	return new ViewInput(full?fullIn:partIn);		
    }
    public static class BuildIn extends View{//instead of credit debit, principle intrest
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
		q.figure();
		chunk(new Object[]{q.date, q.id, q.principle, q.interest, 0f});
	    }
	}
    }

    public static class Quail{ //aka Batch

	LocalDate date;
	String id;
	float amount, principle, interest;

	public Quail(LocalDate d, float a, String i){
	    amount=a;
	    date=d;
	    id=i;
	}

	public void figure(){
//	    if (id == "")
	}

	public boolean match(Quail b){
	    return id.equals(b.id);
	}


	public void add(Quail b){//this must be changed to principle and intrest
	    amount += b.amount;
	}


    }	
}
