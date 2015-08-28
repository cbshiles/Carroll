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
    QueryIn in;
    boolean shouldKill = true;
    
    public TitlePage(Page p, String title, String whereClause, String bName, int status) throws Exception{
	super(title, p);
	place(.9f/4, .1f, .45f, .7f);

	Key key = Key.floorKey.just(new String[]{"ID", "Date Bought", "VIN", "Vehicle", "Item Cost", "Date Paid"});

	try{
	    in = new QueryIn("SELECT "+key.sqlNames()+" FROM Cars WHERE "+whereClause);

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

				if (datum[dp] != null){ //if its paid off, inform user
				    String s = ""+datum[vin];
				    s += nameFromVIN(s)+'\n';
				    new AlertDialog(TitlePage.this, s);
				}
				//collect paid off title info (VIN, cust name (if exists))
			    }
			    if (shouldKill) kill();
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

    String nameFromVIN(String vin) throws Exception{
	//# should this onyly be for active accounts???
//check if vin matches a customer account, if so add the name to s
//				    Key a, Key b, String rest
	Key custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});
	Key contKey = Key.contractKey.just(new String[] {"ID"});
	Input nin = new QueryIn(custKey, contKey,
	    "WHERE Contracts.VIN LIKE '"+vin+"' AND Contracts.Customer_ID = Customers.ID AND Contracts.Paid_Off IS NULL");
	Key k  = custKey.add(contKey.cuts);
	Grid g = new Grid(k, nin);
	g.pull();
	if (g.data.size() == 1){
	    Object[] arr = g.data.get(0);
	    return " "+arr[1]+" "+arr[0];
	}
	else if (g.data.size() > 1){throw new InputXcpt("ERR - multiple contracts matching VIN");}
	else return "";
    }

    public class AlertDialog extends JDialog implements ActionListener {

	public AlertDialog(Window f, String s){
	    super(f, "Cars OFF Floor Plan",  Dialog.ModalityType.APPLICATION_MODAL);
	    shouldKill = false;
	    maker(s);
	}

	private void maker(String addon){
	    setBounds(500,500,500,350);

	    Container c = getContentPane();
	    JPanel jp = new JPanel();
	    c.add(jp);
	    JTextArea jta;
	    jta = new JTextArea(addon, 5, 20);
	    jta.setEditable(false);
	    jta.setLineWrap(true);
	    jta.setWrapStyleWord(true);

	    jp.add(new JScrollPane(jta));
	
	    JButton cb = new JButton("Ok");
	    cb.addActionListener(this);
	    jp.add(cb);
	    setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
	    dispose();
	    TitlePage.this.kill();
	}
    }

}
