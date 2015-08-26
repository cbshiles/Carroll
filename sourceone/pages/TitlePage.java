package sourceone.pages;

import sourceone.fields.*;
import sourceone.csv.*;
import sourceone.key.*;
import sourceone.sql.*;
import static sourceone.key.Kind.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TitlePage extends TablePage {

    JButton jb;
    
    public TitlePage(Page p, String title, String whereClause, String bName, int status) throws InputXcpt{
	super(title, p);
	place(.9f/4, .1f, .45f, .7f);

	Key key = Key.floorKey.just(new String[]{"ID", "Date Bought", "VIN", "Vehicle", "Item Cost", "Date Paid"});

	try{
	    Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Cars WHERE "+whereClause);

	    g = new Grid(key, in);

	    g.pull();
	    g.sort("VIN", true);

	    g.addView(new String[]{"ID", "Date Paid"}, null, null);

	    pushTable();

	    jp.add(jb = new JButton(bName), BorderLayout.SOUTH);

	    setContentPane(jp);

	    jb.addActionListener(new ActionListener() {
	    	    public void actionPerformed(ActionEvent e) {
			try {
			    int[] dx = jt.getSelectedRows();
			    if (dx.length == 0) throw new InputXcpt("No car selected");

			    int id = g.key.dex("ID");
			    int dp = g.key.dex("Date Paid");
			    int vin = g.key.dex("VIN");
			    
			    for (int i : dx){
				Object[] datum = g.data.get(i);
				SQLBot.bot.update("UPDATE Cars SET Title="+status+" WHERE ID="+datum[id]);
				if (datum[dp] != null){
				    String s = datum[vin];
				    s += nameFromVIN(s)+'\n';
				}
				//collect paid off title info (VIN, cust name (if exists))
			    }
			    kill();
			} 	catch (Exception x)
			{new XcptDialog(TitlePage.this, x);}
		    }});
	}
	catch (java.sql.SQLException x)
	{System.err.println(x); return;}
	catch (InputXcpt e)
	{System.err.println(e); return;}
	setVisible(true);
    }

    String nameFromVIN(String vin){
//check if vin matches a customer account, if so add the name to s
//				    Key a, Key b, String rest
	new QueryIn(Key.customerKey.just(new String[] {"Last Name", "First Name"}),
		    Key.contractKey.just(new String[] {}),
	    "WHERE Contracts.VIN LIKE '"+vin+"' AND Contracts.Customer_ID = Customers.ID");
	Key k  = custKey.add(contKey.cuts);
	Grid g = new Grid(k, in);
	g.pull();
	if (g.data.
				    SELECT Customers.First_Name, Customers.Last_Name WHERE 
    }
}
