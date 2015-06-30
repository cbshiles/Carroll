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
    
    public TitlePage(){
	super("Pending Titles");
	setSize(400, 600);
	try{

	    Input in = new QueryIn("SELECT * FROM Cars WHERE Title=0");

	    Key key = new Key(
		new String[]{"ID", "Date Bought", "Vehicle", "Item_ID", "Item_Cost", "Title", "Date Paid"},
		new Kind[]{INT, DATE, STRING, STRING, FLOAT, INT, DATE});

	    Grid g = new Grid(key, in);
	    g.pull();
	    jt = g.getTable();

	    jp.add(new JScrollPane(jt), BorderLayout.NORTH);

	    
	    jp.add(jb = new JButton("tietle emmm"), BorderLayout.SOUTH);

	    setContentPane(jp);

	    jb.addActionListener(new ActionListener() {
	    	    public void actionPerformed(ActionEvent e) {
			try {
			    for (int i : jt.getSelectedRows())
				SQLBot.bot.update("UPDATE Cars SET Title=1 WHERE ID="+g.data.get(i)[0]);
			} catch (Exception x)
			{System.err.println("YO: "+x.getCause()+x.getClass().getName());}
	    	    }
	    	});
	    
	} catch (Exception e)
	{System.err.println("YO!: "+e.getCause()+e.getClass().getName()+e.getMessage());}

	setVisible(true);
    }
}
