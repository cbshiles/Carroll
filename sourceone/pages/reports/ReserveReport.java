package sourceone.pages.reports;

import sourceone.key.*;
import sourceone.sql.*;
//import sourceone.csv.*;
import java.time.*;

import sourceone.pages.*;

//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;

public class ReserveReport extends Report{

    Grid go;
    View v;

    //# could use a blob for re3serve transactions, right now its aclawys taken from the top

    public ReserveReport(Page p) throws Exception{
	super("Reserve", p, new Account[]{new ResAccount(true), new ResAccount(false)});
	rKey =  Key.resKey;
	init();
    }

    public static class ResAccount extends Account{

	String columnName;
	Key tTable;
	
	public ResAccount(boolean ours){ // is this src1(ours) or l&k's
	    super("Reserve Account", null, Key.resKey);
	    if (ours){
		columnName = "srcreserve"; 
		tTable = Key.srcResKey;
	    } else {
		columnName = "lnkreserve"; 
		tTable = Key.LNKResKey;
	    }
	    loadBlobs(new Blob[]{new ResIn(columnName), new ResOut(tTable)});
	}

	public float getStart(LocalDate ld) throws Exception{//get beginning balance, starting at ld
	    Key r = Key.contractKey.just(columnName);
	    Grid g;
	    g = new Grid(r, new QueryIn(r,
					"WHERE Date_Bought < '"+ld+"'")); 
	    g.pull();
	    return resSum(ld)-g.floatSum(columnName);
	}

	private float resSum(LocalDate ld){
	    try{
		return SQLBot.bot.query1Float("SELECT SUM(Amount) FROM "+tTable.name+" WHERE Date < '"+ld+"'");
	    } catch (Exception e){System.out.println("#$%: "+e); return 0;}
	}

	@Override
	public Object[] makeChunk(View tv, String m){
	    float deb = tv.floatSum("Debit");
	    float cred = tv.floatSum("Credit");
	    return new Object[]{null, "Current Period Change: "+m, deb, cred, deb-cred};
	}
    }

    private static class ResIn extends Blob implements Enterer{

	private Key contKey;
	int ln, fn, res, dt;
	private String cName;
	
	public ResIn(String c){
	    cName=c;
	    contKey = Key.contractKey.just(new String[] {cName, "Date Bought"});
	    k = custKey.add(contKey.cuts);

	    ln = k.dex("Last Name");
	    fn = k.dex("First Name");
	    res = k.dex(cName);
	    dt = k.dex("Date Bought");
	}

	public Input in(LocalDate a, LocalDate z)throws Exception{
	    return new QueryIn
		(custKey, contKey,
		 "WHERE Contracts.Date_Bought >= '"+a+
		 "' AND Contracts.Date_Bought <= '"+z+
		 "' AND Contracts.Customer_ID = Customers.ID AND "+cName+" > 0.01;");
	}

	public Enterer ent(){return this;}

	public Object[] editEntry(Object[] o){
	    return new Object[] {
		o[dt],
		""+o[ln]+", "+o[fn],
		0f,
		o[res],
		0f
	    };}
	
    }

    private static class ResOut extends Blob implements Enterer{

	int id, td, dt, amt;
	    
	public ResOut(Key yek){
	    k = yek;

	    id = k.dex("ID");
	    td = k.dex("Trans Description");
	    dt = k.dex("Date");
	    amt = k.dex("Amount");

	}

	public Input in(LocalDate a, LocalDate z)throws Exception{
	    return new QueryIn
		(k,
		 "WHERE Date >= '"+a+
		 "' AND Date <= '"+z+"'");
	}

	
	public Enterer ent(){return this;}

	public Object[] editEntry(Object[] o){
/*
	    new IntCut("ID"),
	    new StringCut(63, "Trans Description"),
	    new DateCut("Date", "NOT NULL"),
	    new FloatCut("Amount", "NOT NULL")
 */
	    return new Object[] {
		o[dt],
		o[td],
		o[amt],
		0f,
		0f
	    };}

	    

    }
}
