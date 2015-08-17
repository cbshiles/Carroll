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
    
    public TitlePage(Page p, String title, String whereClause, String bName, int status){
	super(title, p);
	place(.9f/4, .1f, .45f, .7f);

	Key key = Key.floorKey.just(new String[]{"ID", "Date Bought", "VIN", "Vehicle", "Item Cost"});

	try{
	    Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Cars WHERE "+whereClause);

	    g = new Grid(key, in);

	    g.pull();
	    g.sort("VIN", true);

	    g.addView(new String[]{"ID"}, null, null);

	    pushTable();

	    jp.add(jb = new JButton(bName), BorderLayout.SOUTH);

	    setContentPane(jp);

	    jb.addActionListener(new ActionListener() {
	    	    public void actionPerformed(ActionEvent e) {
			try {
			    int[] dx = jt.getSelectedRows();
			    if (dx.length == 0) throw new InputXcpt("No car selected");

			    int id = g.key.dex("ID");
			    
			    for (int i : dx){
				Object[] datum = g.data.get(i);
				SQLBot.bot.update("UPDATE Cars SET Title="+status+" WHERE ID="+datum[id]);
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
