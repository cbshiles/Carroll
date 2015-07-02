package sourceone.pages;

import sourceone.csv.*;
import sourceone.key.*;
import sourceone.sql.*;
import static sourceone.key.Kind.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class PayContracts extends Page {

    JTable jt;
    JPanel jp = new JPanel(new BorderLayout());
    JButton jb;
    
    public PayContracts(){
	super("Pay Contracts");
	setSize(400, 600);
	try{
	    Key key = Key.contractKey.just(new int[] {1,2,5,6,7,8,9,10,13,14,15});
	    Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Contracts WHERE Next_Due < CURDATE()");

	    Grid g = new Grid(key, in);
	    g.pull();

	    g.clearView(new Key(
			    new String[]{"Customer Name", "Terms", "Start Date", "Due Date", "Remaining Balance",
					 "Payments Due", "Total Payment"},
			    new Kind[]{STRING, STRING, DATE, DATE, FLOAT, INT, FLOAT}).cuts,
			
			new Enterer(){
		    public Object[] editEntry(Object[] o){
			return new Object[] {
			    (String)o[1]+", "+o[0],
			    terms((int)o[2], (float)o[3], (int)o[5]),
			    o[7],
			    o[10],
			    (float)o[6] - (int)o[8]*(float)o[3],
			    "???",
			    "???"
			};
		    }

		    public String terms(int num, float amt, int freq){
			char c;
			if (freq==7) c='W';
			else if (freq ==14) c='B';
			else c='M';
			return ""+num+" "+c+" @ "+amt;
		    }
		});

	    g.push();

	    jt = g.view.getTable();

	    jp.add(new JScrollPane(jt), BorderLayout.NORTH);

	    
	    jp.add(jb = new JButton("pay emmm"), BorderLayout.SOUTH);

	    setContentPane(jp);

	    jb.addActionListener(new ActionListener() {
	    	    public void actionPerformed(ActionEvent e) {
			try {

			    for (int i : jt.getSelectedRows()){
				Object[] o = g.data.get(i);
				System.out.println(o[13]+" ~ "+o[15]);
				int pays = (int)o[13]+1;
				LocalDate due = next((int)o[8], (LocalDate)o[15]);
				System.out.println(pays+" ~ "+due);
			    }

//				SQLBot.bot.update("UPDATE Cars SET Payments_made="+()+" WHERE ID="+g.data.get(i)[0]);
			} catch (Exception x)
			{System.err.println("YO: "+x.getCause()+x.getClass().getName());}
	    	    }

		    public LocalDate next(int freq, LocalDate ld){
			LocalDate due;
			if (freq == 30) due = ld.plusMonths(1);
			else due = ld.plusDays(freq);
			return due;
		    }

		    
	    	});
	    
	} catch (Exception e)
	{System.err.println("YO!: "+e.getCause()+e.getClass().getName()+e.getMessage());}

	setVisible(true);
    }
}
