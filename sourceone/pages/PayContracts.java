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
	    Key key = Key.contractKey.just(new int[] {0,1,2,5,6,7,8,9,10,13,14,15});
	    Input in = new QueryIn("SELECT "+key.sqlNames()+" FROM Contracts WHERE Next_Due < CURDATE()");

	    Grid g = new Grid(key, in);
	    g.pull();

	    g.clearView(new Key(
			    new String[]{"Customer Name", "Terms", "Start Date", "Due Date", "Remaining Balance",
					 "Payments Due", "Total Payment"},
			    new Kind[]{STRING, STRING, DATE, DATE, FLOAT, INT, FLOAT}).cuts,
			
			new Enterer(){
		    public Object[] editEntry(Object[] o){
			int nPays, freq = (int)o[5+1];
			LocalDate due = (LocalDate)o[10+1];
			float amt = (float)o[3+1];

			int tmp = numPays(freq, due);
			int maxPays = (int)o[2+1] - (int)o[8+1];
			nPays = (tmp<maxPays)?tmp:maxPays;

			return new Object[] {
			    (String)o[1+1]+", "+o[0+1],
			    terms((int)o[2+1], amt, freq),
			    o[7+1],
			    due,
			    (float)o[6+1] - (int)o[8+1]*amt,
			    nPays,
			    nPays*amt + (float)o[4+1] //Final payment
			};
		    }
		    public int numPays(int freq, LocalDate due){
			int pays = 0;
			LocalDate today = LocalDate.now();
			while (today.compareTo(due) > 0){
			    due = next(freq, due);
			    pays++;
			}
			return pays;
		    }
		    public LocalDate next(int freq, LocalDate ld){
			LocalDate due;
			if (freq == 30) due = ld.plusMonths(1);
			else due = ld.plusDays(freq);
			return due;
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
				//System.out.println(o[8+1]+" ~ "+o[10+1]);
				int pays = (int)o[8+1]+1;
				LocalDate due = next((int)o[5+1], (LocalDate)o[10+1]);
				//System.out.println("UPDATE Contracts SET Payments_made="+pays+", Next_Due="+due+" WHERE ID="+o[0]);
				SQLBot.bot.update("UPDATE Contracts SET Payments_made="+pays+", Next_Due='"+due+"' WHERE ID="+o[0]);
			    }

			} catch (Exception x)
			{System.err.println("YO: "+x.getCause()+x.getClass().getName());
			    System.err.println(x.getMessage());}
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
