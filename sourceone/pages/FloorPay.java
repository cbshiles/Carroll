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

    FloorCalc ent;
    View v;

    public void getTable(LocalDate ld){
	ent.setDay(ld);
	v = g.addView(new String[]{"ID", "Title", "Curtailed"}, new Cut[]{new StringCut("Title"), new FloatCut("Daily Rate"), new IntCut("Days Active"),
							    new FloatCut("Accrued Interest"), new FloatCut("Fees"), new FloatCut("Sub total")},
	    ent);

	pushTable();
	jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public FloorPay(Page p){
	super("Floor Plan Payoffs", p);
	tablePlace();

	Key key = Key.floorKey.just(new String[]{"ID", "VIN", "Date Bought", "Vehicle", "Item Cost", "Title", "Curtailed"});
	try {
	    Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Cars WHERE Pay_Off_Amount IS NULL");

	    g = new Grid(key, in);
	    g.pull(); 
	    
	    ent = new FloorCalc(key, null, true); //null since itll be rewritten anywho

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
			System.err.println("UPDATE Cars SET Date_Paid='"+d+"', Pay_Off_Amount="+amt+" WHERE ID="+o[id]);
			kill();
		    } catch (Exception ix) {new XcptDialog(FloorPay.this, ix);}
		}
	    });

	jp.add(cPan);
	
	setVisible(true);
    }
}
