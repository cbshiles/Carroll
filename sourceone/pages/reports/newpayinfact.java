package sourceone.pages.reports;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;

import java.time.*;
import java.util.ArrayList;

public class PayInFact{ //returns different inputs, but using same data

    private ArrayList<BuildIn> bins = new ArrayList();
    private BuildIn ptr;

    public Input in(LocalDate ao, LocalDate zo, boolean full)throws Exception{
	BuildIn b = find(ao, zo);
	if (b != null) return b.get(full);

	Grid g = new Grid(Key.paymentKey, new QueryIn(Key.paymentKey, "WHERE Day >= '"+ao+
						      "' AND Day <= '"+zo+"' ORDER BY Day"));
	g.pull();
	bins.add(ptr = new BuildIn(ao, zo));

	for (Object[] i: g.data){
	    Mint mi = new Mint(i);
	    if (! mi.noll)
		ptr.add(mi);
	    else ptr.load(mi);
	}
	ptr.flush();
	return ptr.get(full);
    }

    private BuildIn find(LocalDate a, LocalDate z){
	for (BuildIn bi: bins)
	    if (bi.timeMatch(a, z))
		return bi;
	return null;
    }

    public static class BuildIn{ //duality
	ArrayList<Mint> fint, pint;
	LocalDate a,z;
	View full, partial;

	public BuildIn(LocalDate ao, LocalDate zo){
	    a=ao; z=zo;
	    fint = new ArrayList<Mint>();
	    pint = new ArrayList<Mint>();
	}

	public boolean timeMatch(LocalDate ao, LocalDate zo){
	    return ao.equals(a) && zo.equals(z);
	}

	public void add(Mint m)throws Exception{ //add to list of payments (non-null batches) 
	    if (! (m.in(fint) || m.in(pint)))
		(m.full()?fint:pint).add(m);		
	}

	public void load(Mint m){ //add to the actual view
	    (m.full()?full:partial).chunk(m.stats());
	}

	public void flush(){
	    for (Mint m: fint)
		full.chunk(m.stats());
	    for (Mint m: pint)
		partial.chunk(m.stats());
	}

		// float tc = SQLBot.bot.query1Float("SELECT Total_Contract FROM Contracts WHERE ID="+cid);
		// ((tc > .01)?fint:pint).add(m);
    }

    public static class Mint{
	static int cid = Key.paymentKey.dex("Contract ID");
	static int day = Key.paymentKey.dex("Day");
	static int amt = Key.paymentKey.dex("Amount");
	static int bid = Key.paymentKey.dex("Batch ID");

	private Object[] src;
	public boolean noll;

	public Mint(Object[] o){
	    src = o;
	    noll = (o[bid] == null);
	    set();
	    }
	}

    private void set(){

    }
	public boolean in(ArrayList<Mint> arlzt){//must add if its found

	}
    }
}
    
