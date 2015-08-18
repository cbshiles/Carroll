package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.fields.*;
import javax.swing.*;
//import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class OldFloor extends TablePage {
    TextField aDate, bDate;
    Key inKey;
    public OldFloor (Page p) throws Exception{
	super("Old Floor", p);

	inKey = Key.floorKey.just(new String[]{"Date Bought","VIN", "Vehicle", "Item Cost", "Title", "Date Paid", "Pay Off Amount", "Curtailed"}); //no id

	JPanel cPan = new JPanel();

	aDate = new TextField("Start Date");
	bDate = new TextField("End Date");

	cPan.add(aDate.getJP());
	cPan.add(bDate.getJP());

	FieldListener fl = new FieldListener() {
		public void dew() {
		    try {
			refresh();
		    } catch (InputXcpt ix) {;/*System.err.println("HGXB");*/}
		    catch (Exception e) {new XcptDialog(OldFloor.this, e);}
		}};

	aDate.addListener(fl);
	bDate.addListener(fl);
	    
	jp.add(cPan, java.awt.BorderLayout.SOUTH);

	refresh();

	tablePlace();
	setVisible(true);
    }

    private void refresh() throws Exception {
	Input in = new QueryIn(inKey, "WHERE Date_Paid IS NOT NULL"+getClause());
	g = new Grid(inKey, in);
	g.pull();
	g.clearView(new Cut[]{new DateCut("Date Bought", "NOT NULL"),
			      new StringCut(31, "VIN", "NOT NULL"),
			      new StringCut(127, "Vehicle", "NOT NULL"),
			      new FloatCut("Item Cost", "NOT NULL"),
			      new StringCut("Title"),
			      new DateCut("Date Paid"),
			      new FloatCut("Pay Off Amount"),
			      new FloatCut("Profit")},
	    new Ent(inKey));
	pushTable();
    }

    private class Ent implements Enterer{
	Key k;
	int db, vin, veh, ic, ttl, dp, poa, cd;

	public Ent(Key kk){
	    k = kk;
	    db = k.dex("Date Bought");
	    vin = k.dex("VIN");
	    veh = k.dex("Vehicle");
	    ic = k.dex("Item Cost");
	    ttl = k.dex("Title");
	    dp = k.dex("Date Paid");
	    poa = k.dex("Pay Off Amount");
	    cd = k.dex("Curtailed");
	}

	public String titleStatus(int t){
	    switch(t){
	     case 0: return "Pending";
	     case 1: return "Yes";
	     case 2: return "Never";
	     case 3: return "Nope";
	    }
	    return "Invalid code";
	}

	public Object[] editEntry(Object[] o){
	    float zic = ((int)o[cd] == 2)?0f:(float)o[ic];
	    return new Object[]{
		o[db], o[vin], o[veh],
		zic,
		titleStatus((int)o[ttl]),
		o[dp], o[poa],
		(float)o[poa] - zic
	    };
	}
    }

    public String getClause() {//get cher time clause
	LocalDate a = null, b = null;
	try{ a = StringIn.parseDate(aDate.text()); }
	catch (InputXcpt ix) {;}
	try{ b = StringIn.parseDate(bDate.text()); }
	catch (InputXcpt ix) {;}

	String str;

	if (a != null || b!= null) str = " AND ";
	else return "";

	String col = "Date_Paid";
		 
	if (a != null)
	    str += col+" >= '"+BasicFormatter.cxnvert(a)+"\'";
	
	if (a != null && b!= null) str += " AND ";

	if (b != null)
	    str += col+" <= '"+BasicFormatter.cxnvert(b)+"\'";
	return str;
    }
    
}
