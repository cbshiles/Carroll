package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.csv.*;
import static sourceone.key.Kind.*;

import java.time.*;
import java.time.temporal.ChronoUnit;
import javax.swing.*;
import java.awt.event.*;

public class CarReport extends TablePage{

    public CarReport(Page p){
	super("Create Car Report", p);

	Key inKey = Key.floorKey.just(new String[]{"Date Bought","VIN", "Vehicle", "Item Cost", "Title"}); 

	try {
	    Input in = new QueryIn(inKey, "WHERE Title<2;");
	    g = new Grid(inKey, in);
	    g.pull();
	    Key entKey = new Key("Cars",
				 new String[]{"Date Bought", "VIN", "Vehicle", "Daily Rate", "Title", "Item Cost", "Days active", "Accrued Interest", "Fees", "Subtotal"},
				 new Kind[]{DATE, STRING, STRING, FLOAT, STRING, FLOAT, INT, FLOAT, FLOAT, FLOAT});
	    g.clearView(entKey.cuts, new Ent(inKey));
	    pushTable();
	    jt.setRowSelectionAllowed(false);
	} catch (Exception e){System.err.println("***"+e); e.printStackTrace(); kill(); return;}

	JPanel cPan = new JPanel();
	
	JButton jb = new JButton("Print Report");
	jb.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae) {
		    try {
			g.view.addOut(new CSVOutput(g.view.key, SQLBot.bot.path+"Car_Report_"+LocalDate.now()+".csv"));
			g.view.push();
			kill();
		    } catch (Exception e){
			new XcptDialog(CarReport.this, e);
			e.printStackTrace();
		    }
		}});

	cPan.add(jb);
	jp.add(cPan);
	
	setSize(1000, 600);
	setVisible(true);
    }

    private class Ent implements Enterer{
	Key k;
	int db, vin, veh, ic, ttl;

	public Ent(Key kk){
	    k = kk;
	    db = k.dex("Date Bought");
	    vin = k.dex("VIN");
	    veh = k.dex("Vehicle");
	    ic = k.dex("Item Cost");
	    ttl = k.dex("Title");
//	    System.err.println(db+" "+ vin+" "+ veh+" "+ ic+" "+ ttl);
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
		o[db],
		o[vin],
		o[veh],
		dRate,
		((int)o[ttl] == 0) ? "Pending":"Yes",
		o[ic],
		days,
		interest,
		fees,
		cost+interest+fees
	    };
	}
    }
}
