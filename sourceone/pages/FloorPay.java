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
	super("Floor Plan Payoffs");
	setSize(600,600);

	Key key = Key.floorKey.accept(new String[]{"VIN","Date Paid"});
	JTable jt;
	Grid g;
	try {
	    Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Cars WHERE Title<2");

	     g = new Grid(key, in);

	    View v = g.addView(new String[]{"Title"}, new Cut[]{new StringCut("Title"), new FloatCut("Daily Rate"), new IntCut("Days Active"),
						       new FloatCut("Accrued Interest"), new FloatCut("Fees"), new FloatCut("Sub total")},
		new Ent(key));
	    v.addTable();

	    jt = (JTable)g.go();
	    jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
 	catch (java.sql.SQLException x)
	{System.err.println(x); return;}
	catch (InputXcpt e)
	{System.err.println(e); return;}
	
	JPanel jp = new JPanel(new BorderLayout());
	jp.add(new JScrollPane(jt), BorderLayout.NORTH);
	
	JPanel cPan = new JPanel();

	sourceone.fields.TextField payDay;
	payDay = new sourceone.fields.TextField("Date paid:", BasicFormatter.cinvert(LocalDate.now()));
	cPan.add(payDay.getJP());

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

			SQLBot.bot.update("UPDATE Cars SET Title="+((int)o[tl]+2)+", Date_Paid='"+d+"' WHERE ID="+o[id]);
		    } catch (Exception ix) {new XcptDialog(FloorPay.this, ix);}
		}
	    });

	jp.add(cPan);
	
	setContentPane(jp);
	setVisible(true);
    }

    public static class Ent implements Enterer{

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
