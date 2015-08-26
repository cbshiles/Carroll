package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.csv.*;
import static sourceone.key.Kind.*;

import java.time.*;
import java.time.temporal.ChronoUnit;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class CarReport extends TablePage{

    String jcost, jtotal;

    public CarReport(Page p){
	super("Create Car Report", p);

	Key inKey = Key.floorKey.just(new String[]{"Date Bought","VIN", "Vehicle", "Item Cost", "Title", "Curtailed"}); 

	try {
	    Input in = new QueryIn(inKey, "WHERE Pay_Off_Amount IS NULL ORDER BY Date_Bought"); //"WHERE Title<2;"
	    g = new Grid(inKey, in);
	    g.pull();
	    Key entKey = new Key("Cars",
				 new String[]{"Date Bought", "VIN", "Vehicle", "Daily Rate", "Title", "Item Cost", "Days active", "Accrued Interest", "Fees", "Subtotal"},
				 new Kind[]{DATE, STRING, STRING, FLOAT, STRING, FLOAT, INT, FLOAT, FLOAT, FLOAT});
	    g.clearView(entKey.cuts, new Ent(inKey));
	    pushTable();

	    jcost = frm(g.view.floatSum("Item Cost"));
	    jtotal = frm(g.view.floatSum("Subtotal"));
//	    jt.setRowSelectionAllowed(false);
	} catch (Exception e){System.err.println("***"+e); e.printStackTrace(); kill(); return;}


	
	JButton jb = new JButton("Create");
	jb.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae) {
		    try {
			g.view.addOut(new CSVOutput(g.view.key, SQLBot.bot.path+"Car_Report_"+LocalDate.now()+".csv",
						    "~~~~~"+jcost+"~~~~"+jtotal));
			g.view.push();

			kill();
		    } catch (Exception e){
			new XcptDialog(CarReport.this, e);
			e.printStackTrace();
		    }
		}});
	
	JPanel cPan = new JPanel();
	cPan.setLayout(new GridLayout(2, 10));
	
	addEmpties(5, cPan);
	cPan.add(new JTextField(jcost));

	addEmpties(3, cPan);	
	cPan.add(new JTextField(jtotal));

	addEmpties(5, cPan);
	cPan.add(jb);

	addEmpties(4, cPan);
	
	jp.add(cPan);
	
	tablePlace();
	setVisible(true);
    }

    private class Ent implements Enterer{
	Key k;
	int db, vin, veh, ic, ttl, cd;

	public Ent(Key kk){
	    k = kk;
	    db = k.dex("Date Bought");
	    vin = k.dex("VIN");
	    veh = k.dex("Vehicle");
	    ic = k.dex("Item Cost");
	    ttl = k.dex("Title");
	    cd = k.dex("Curtailed");
//	    System.err.println(db+" "+ vin+" "+ veh+" "+ ic+" "+ ttl);
	}

	public Object[] editEntry(Object[] o){
	    LocalDate bot = (LocalDate)o[db];
	    float cost = (float)o[ic];

	    float dRate = View.rnd(cost*.0007f); 
	    int days = (int)ChronoUnit.DAYS.between(bot, LocalDate.now());

	    float tmp = dRate*days;
	    float interest, fees;
	    if ((int)o[cd] == 1){ //curtailed entry
		interest = tmp;
		fees = 0f;
	    } else {
		int min = (cost >= 5000)?65:35;
		interest = tmp>min?tmp:min;
		fees = 25f;
	    }

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

    private java.text.DecimalFormat myFormatter = new java.text.DecimalFormat("#0.00");
    private String frm(float ff) {return myFormatter.format(ff);}
}
