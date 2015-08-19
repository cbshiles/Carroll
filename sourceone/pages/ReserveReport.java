package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
//import sourceone.csv.*;
import java.time.*;

//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;

public class ReserveReport extends CenterFile{

    Key custKey, contOKey, contNKey, ko, kn;
    Grid go;
    View v;

    public ReserveReport(Page p) throws Exception{
	super("Reserve", p);

	custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});
	contOKey = Key.contractKey.just(new String[] {"Reserve", "Paid Off"});
	contNKey = Key.contractKey.just(new String[] {"Reserve", "Date Bought"});
	
	ko = custKey.add(contOKey.cuts);
	kn = custKey.add(contNKey.cuts);

	blobs.add(new BotTrax());
	blobs.add(new SoldTrax());

	dew(5, 2015, 4);
    }

    public float getStart(LocalDate ld) {//get beginning balance, starting at ld
	Key r = Key.contractKey.just("Reserve");
	Grid g;
	try {
	    g = new Grid(r, new QueryIn(r,
					"WHERE Date_Bought < '"+ld+"' AND ( Paid_Off IS NULL OR Paid_Off >= '"+ld
					+"' );"));
	    g.pull();
	} catch (Exception e) {new XcptDialog(this, e); return .1337f;}
	return -g.floatSum("Reserve");
    }

    private class BotTrax extends Blob{//bought contracts
	public BotTrax(){super(kn);}
	Enterer ent(){return new Nnt(kn);}
	Input in(LocalDate a, LocalDate z)throws Exception{
	    return new QueryIn
		(custKey, contNKey,
		 "WHERE Contracts.Date_Bought >= '"+a+
		 "' AND Contracts.Date_Bought <= '"+z+
		 "' AND Contracts.Customer_ID = Customers.ID AND Reserve > 0.01;");
	}}

    private class SoldTrax extends Blob{//bought contracts
	public SoldTrax(){super(ko);}
    	Enterer ent(){return new Ont(ko);}
	Input in(LocalDate a, LocalDate z)throws Exception{
	    return new QueryIn
		(custKey, contOKey,
		 "WHERE Contracts.Paid_Off >= '"+a+
		 "' AND Contracts.Paid_Off <= '"+z+
		 "' AND Contracts.Customer_ID = Customers.ID AND Reserve > 0.01;");
	}}

    public static class Ont implements Enterer{
	int ln, fn, res, po;
	
	public Ont(Key k){
	    ln = k.dex("Last Name");
	    fn = k.dex("First Name");
	    res = k.dex("Reserve");
	    po = k.dex("Paid Off");
	}
	
	public Object[] editEntry(Object[] o){

	    return new Object[] {
		o[po],
		""+o[ln]+", "+o[fn],
		o[res],
		0f,
		0f
	    };
	}
    }
    public static class Nnt implements Enterer{
	int ln, fn, res, sd;
	
	public Nnt(Key k){
	    ln = k.dex("Last Name");
	    fn = k.dex("First Name");
	    res = k.dex("Reserve");
	    sd = k.dex("Date Bought");
	}
	
	public Object[] editEntry(Object[] o){

	    return new Object[] {
		o[sd],
		""+o[ln]+", "+o[fn],
		0f,
		o[res],
		0f
	    };
	}
    }
}
