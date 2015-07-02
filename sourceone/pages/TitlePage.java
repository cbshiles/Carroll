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

	    Key key = Key.floorKey.except(new int[]{5,6});

	    Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Cars WHERE Title=0");

	    Grid g = new Grid(key, in);
	    g.pull();

	    jt = g.getTable();

	    jp.add(new JScrollPane(jt), BorderLayout.NORTH);

	    jp.add(jb = new JButton("Title Em"), BorderLayout.SOUTH);

	    setContentPane(jp);

	    jb.addActionListener(new ActionListener() {
	    	    public void actionPerformed(ActionEvent e) {
			try {
			    for (int i : jt.getSelectedRows()){
				System.out.println("UPDATE Cars SET Title=1 WHERE ID="+g.data.get(i)[0]);
				SQLBot.bot.update("UPDATE Cars SET Title=1 WHERE ID="+g.data.get(i)[0]);
			    }
			} catch (Exception x)
			{System.err.println("YO: "+x.getCause()+x.getClass().getName());}
	    	    }
	    	});
	    
	} catch (Exception e)
	{System.err.println("YO!: "+e.getCause()+e.getClass().getName()+e.getMessage());}

	setVisible(true);
    }
}
