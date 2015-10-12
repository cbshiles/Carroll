package sourceone.pages.reports;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;

import java.time.*;
import java.util.ArrayList;

public class PayInFact{ //returns different inputs, but using same data
//src1, bebe
    //~ GOT TO REFACTOR THISSSSS
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


    //only thing called outside class
    public Input in(LocalDate ao, LocalDate zo, boolean full)throws Exception{//no nulls
	BuildIn b = find(ao, zo, full);
	if (b != null) {/*System.out.println("reuse!");*/ return new ViewInput(b);}
	else {
	    Grid g = new Grid(Key.paymentKey, new QueryIn(Key.paymentKey, "WHERE Day >= '"+ao+"' AND Day <= '"+zo+"' ORDER BY Day"));
	    g.pull();

	    fullIn = new BuildIn(true,ao,zo);
	    partIn = new BuildIn(false,ao,zo);
		
	    for (Object[] i: g.data){

		//#should never be a problem, but batch cant be someone's name
		//bckwrds search quicker
		Quail qq = new Quail(i);
		int z = (int)i[1];
		if (i[4] != null){ //~ need to force ID
		    if (! fullIn.add(qq)){
			if (! partIn.add(qq)){
			    
			    which(z).gnu(qq);
			}
		    }
		} else {
		    String zename = SQLBot.bot.query1Name("SELECT Customers.First_Name, Customers.Last_Name FROM Customers, Contracts WHERE Contracts.ID="+(int)i[1]+" AND Customers.ID=Contracts.Customer_ID");
		    which(z).chunk(new Object[]{(LocalDate)i[2], zename+" - Payoff", qq.principle, qq.interest, qq.amount, qq.discount});
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
	    super(Key.zumKey);
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
		chunk(new Object[]{q.date, q.batch_id, q.principle, q.interest, q.amount, q.amount});
	    }
	}
    }

    public static class Quail{ //aka Batch

	LocalDate date;
	String batch_id;
	int contract_id;
	float amount, principle, interest, discount=0f;
	int tc,ga,pm,aop;

	Key dk = Key.contractKey.just(new String[]{"Total Contract", "Gross Amount", "Payments Made", "Amount of Payment"});
	
	private float getDiscount(){
		try {
		Grid gr = new Grid(dk, new QueryIn(dk, "WHERE ID="+contract_id));
		gr.pull();
		Object[] o = gr.data.get(0);
		float tep; //total expected to pay
		float tcO = (float)o[tc];
		if (tcO < .001)
		    tep = (float)o[ga];
		else
		    tep = tcO;

		return tep - (float)o[aop]*(int)o[pm];
		
		} catch (Exception e) {System.out.println("diss errorr "+contract_id+' '+e); return 0;}
//			"FROM Contracts SELECT Total Contract, Gross Amount, Net Amount 
	}

	public Quail(Object[] i){ //getrs a payment entry //LocalDate d, float a, String i){

	    /*discount init*/
	    tc = dk.dex("Total Contract");
	    ga = dk.dex("Gross Amount");
	    pm = dk.dex("Payments Made");
	    aop = dk.dex("Amount of Payment");

	    contract_id = (int)i[1];
	    date=(LocalDate)i[2];
	    amount=(float)i[3];
	    batch_id=(String)i[4];
	    if (batch_id == null) {//payoff
		principle = amount;
		interest = 0f;
		discount = getDiscount();
	    } else { //group
//lookup up contract, find
		Key k = Key.contractKey.just(new String[]{"Total Contract", "Gross Amount", "Net Amount"});
		try {
		Grid gr = new Grid(k, new QueryIn(k, "WHERE ID="+contract_id));
		gr.pull();
		float tot, gro, net, per;
		Object[] fo = gr.data.get(0);
		tot = (float)fo[0];
		gro = (float)fo[1];
		net = (float)fo[2];

		per = net/((tot > .01)?tot:gro);
		principle = amount*per;
		interest = amount*(1f-per);
		} catch (Exception e) {System.out.println("gnu errorr "+contract_id+' '+e);}
//			"FROM Contracts SELECT Total Contract, Gross Amount, Net Amount 
	    }

	}

	public boolean match(Quail b){
	    return batch_id.equals(b.batch_id);
	}


	public void add(Quail b){//this must be changed to principle and intrest
	    principle += b.principle;
	    interest += b.interest;
	    amount += b.amount;
	}


    }	
}
