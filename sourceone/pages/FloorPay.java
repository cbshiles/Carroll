package sourceone.pages;
import sourceone.csv.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.fields.*;
import static sourceone.key.Kind.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class FloorPay extends TablePage {

    Ent ent;
    View v;

    public void getTable(LocalDate ld){
	ent.setDay(ld);
	v = g.addView(new String[]{"Title"}, new Cut[]{new StringCut("Title"), new FloatCut("Daily Rate"), new IntCut("Days Active"),
							    new FloatCut("Accrued Interest"), new FloatCut("Fees"), new FloatCut("Sub total")},
	    ent);

	pushTable();
	jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public FloorPay(Page p){
	super("Floor Plan Payoffs", p);
	tablePlace();

	Key key = Key.floorKey.just(new String[]{"ID", "Date Bought", "Vehicle", "Item Cost", "Title"});
	try {
	    Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Cars WHERE Pay_Off_Amount IS NULL");

	    g = new Grid(key, in);
	    g.pull(); 
	    
	    ent = new Ent(key);

	    getTable(LocalDate.now());

	}
 	catch (java.sql.SQLException x)
	{System.err.println(x); return;}
	catch (InputXcpt x)
	{System.err.println(x); return;}
	
	JPanel cPan = new JPanel();

	sourceone.fields.TextField payDay;
	payDay = new sourceone.fields.TextField("Date paid:", BasicFormatter.cinvert(LocalDate.now()));
	cPan.add(payDay.getJP());

	payDay.addListener(new FieldListener() {
		public void dew() {
		    try {
			LocalDate d = StringIn.parseDate(payDay.text());
			getTable(d);
		    } catch (InputXcpt ix) {;/*System.err.println("HGXB");*/}
		}});


	JButton jb = new JButton("Pay Car Off");
	cPan.add(jb, BorderLayout.SOUTH);

	jb.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    int tl = key.dex("Title");
		    int id = key.dex("ID");
		    try {
			LocalDate d = StringIn.parseDate(payDay.text());

			int[] dx = jt.getSelectedRows();
			if (dx.length == 0) throw new InputXcpt("No car selected");
			Object[] o = g.data.get(dx[0]);

			float amt = (float)v.get("Sub total", dx[0]);

			//! removed Title="+((int)o[tl]+2)+",
			//System.err.println("UPDATE Cars SET Title="+((int)o[tl]+2)+", Date_Paid='"+d+"', Pay_Off_Amount="+amt+" WHERE ID="+o[id]);
			SQLBot.bot.update("UPDATE Cars SET Date_Paid='"+d+"', Pay_Off_Amount="+amt+" WHERE ID="+o[id]);
			kill();
		    } catch (Exception ix) {new XcptDialog(FloorPay.this, ix);}
		}
	    });

	jp.add(cPan);
	
	setVisible(true);
    }

    public static class Ent implements Enterer{

	int db, ic, tl;
	public LocalDate payday;

	public Ent(Key k, LocalDate pd){
	    this(k);
	    payday = pd;
	}
	
	public Ent(Key k){
	    db = k.dex("Date Bought");
	    ic = k.dex("Item Cost");
	    tl = k.dex("Title");
	}

	public void setDay(LocalDate pd){
	    payday = pd;
	}

	public Object[] editEntry(Object[] o){

	    LocalDate bot = (LocalDate)o[db];
	    float cost = (float)o[ic];

	    float dRate = View.rnd(cost*.0007f); 
	    int days = (int)ChronoUnit.DAYS.between(bot, payday);

	    int min = (cost >= 5000)?65:35;
	    float tmp = dRate*days;
	    float interest = tmp>min?tmp:min;
	    float fees = 25;
	    return new Object[]{
		((int)o[tl] == 0) ? "Pending":"Yes",
		dRate,
		days,
		interest,
		fees,
		cost+interest+fees
	    };

	}
    }
}
