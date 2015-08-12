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
    
    public TitlePage(Page p){
	super("Pending Titles", p);
	place(.3f, .1f, .3f, .65f);

	Key key = Key.floorKey.just(new String[]{"ID", "Date Bought", "VIN", "Vehicle", "Item Cost"});

	try{
	    Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Cars WHERE Title=0");

	    g = new Grid(key, in);

	    g.addTable();
	    g.pull();
	    g.sort("Date Bought", true);

	    pushTable(false);

	    jp.add(jb = new JButton("Title Em"), BorderLayout.SOUTH);

	    setContentPane(jp);

	    jb.addActionListener(new ActionListener() {
	    	    public void actionPerformed(ActionEvent e) {
			try {
			    int[] dx = jt.getSelectedRows();
			    if (dx.length == 0) throw new InputXcpt("No car selected");

			    int id = g.key.dex("ID");
			    
			    for (int i : dx){
				Object[] datum = g.data.get(i);
				SQLBot.bot.update("UPDATE Cars SET Title=1 WHERE ID="+datum[id]);
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
