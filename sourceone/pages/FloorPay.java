package sourceone.pages;
import sourceone.csv.*;
import sourceone.key.*;
import sourceone.sql.*;
import static sourceone.key.Kind.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class FloorPay extends Page {

    public FloorPay(){
	super("Discotech");
	setSize(600,600);

	Key key = Key.floorKey.accept(new String[]{"ID","VIN","Date Paid"});
	JTable jt;
	
	try {
	    Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Cars WHERE Title<2");

	    Grid g = new Grid(key, in);

	    View v = g.addView(new String[]{"Title"}, new Cut[]{new StringCut("Title"), new FloatCut("Daily Rate"), new IntCut("Days Active"),
						       new FloatCut("Accrued Interest"), new FloatCut("Fees"), new FloatCut("Sub total")},
		new Ent(key));
	    v.addTable();

	    jt = (JTable)g.go();
	}
 	catch (java.sql.SQLException x)
	{System.err.println(x); return;}
	catch (InputXcpt e)
	{System.err.println(e); return;}


	
	JPanel jp = new JPanel(new BorderLayout());
	jp.add(new JScrollPane(jt), BorderLayout.NORTH);
	setContentPane(jp);
	setVisible(true);
    }

    private class Ent implements Enterer{

	int db, ic, tl;
	
	public Ent(Key k){
	    db = k.dex("Date Bought");
	    ic = k.dex("Item Cost");
	    tl = k.dex("Title");
	}

	public Object[] editEntry(Object[] o){
	    LocalDate bot = (LocalDate)o[db];
	    float cost = (float)o[ic];

	    float dRate = cost*.0007f; 
	    int days = (int)ChronoUnit.DAYS.between(bot, LocalDate.now());

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
