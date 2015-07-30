package sourceone.pages;

import sourceone.fields.*;
import sourceone.csv.*;
import sourceone.key.*;
import sourceone.sql.*;
import static sourceone.key.Kind.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TitlePage extends Page {

    JTable jt;
    JPanel jp = new JPanel(new BorderLayout());
    JButton jb;
    
    public TitlePage(Page p){
	super("Pending Titles", p);
	place(.3f, .1f, .3f, .65f);

	Key key = Key.floorKey.just(new String[]{"ID", "Date Bought", "VIN", "Vehicle", "Item Cost"});

	try{
	    Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Cars WHERE Title=0");

	    Grid g = new Grid(key, in);

	    g.addTable();

	    jp.add(new JScrollPane(jt = (JTable)g.go()), BorderLayout.NORTH);

	    jp.add(jb = new JButton("Title Em"), BorderLayout.SOUTH);

	    setContentPane(jp);

	    jb.addActionListener(new ActionListener() {
	    	    public void actionPerformed(ActionEvent e) {
			try {
			    int[] dx = jt.getSelectedRows();
			    if (dx.length == 0) throw new InputXcpt("No car selected");
			    for (int i : dx){
				SQLBot.bot.update("UPDATE Cars SET Title=1 WHERE ID="+g.data.get(i)[0]);
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
    }
