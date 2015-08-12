package sourceone.pages;

import sourceone.fields.*;
import sourceone.csv.*;
import sourceone.key.*;
import sourceone.sql.*;
import static sourceone.key.Kind.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TitleDone extends TablePage {

    JButton jb;
    
    public TitleDone(Page p){
	super("Titles We Have:", p);
	place(.3f, .1f, .3f, .65f);

	Key key = Key.floorKey.just(new String[]{"ID", "Date Bought", "VIN", "Vehicle", "Item Cost"});

	try{
	    Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Cars WHERE Title=1 AND Pay_Off_Amount IS NOT NULL");

	    g = new Grid(key, in);

	    g.addTable();
	    g.pull();
	    g.sort("VIN", true);

	    pushTable(false);

	    jp.add(jb = new JButton("Title Em"), BorderLayout.SOUTH);

	    setContentPane(jp);

	    jb.addActionListener(new ActionListener() {
	    	    public void actionPerformed(ActionEvent e) {
			try {
			    int[] dx = jt.getSelectedRows();
			    if (dx.length == 0) throw new InputXcpt("No car selected");
			    for (int i : dx){
				Object[] datum = g.data.get(i);
				SQLBot.bot.update("UPDATE Cars SET Title=3 WHERE ID="+datum[0]);
			    }
			    kill();
			} 	catch (Exception x)
			{new XcptDialog(TitleDone.this, x);}
		    }});
	}
	catch (java.sql.SQLException x)
	{System.err.println(x); return;}
	catch (InputXcpt e)
	{System.err.println(e); return;}
	setVisible(true);
	}
    }
