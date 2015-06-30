package sourceone.pages;

import sourceone.fields.*;
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

	    Input in = new QueryIn("SELECT * FROM Contracts WHERE Next_Due < CURDATE()");

	Key key = new Key(
	    new String[]{"ID", "First Name", "Last Name", "Address", "Phone Number",
			 "Number of Payments", "Amount", "Final Payment",
			 "Payment Frequency", "Total of Payments", "Start Date", "Vehicle",
			 "VIN", "Payments made", "Paid off day", "Next Due"},
	    new Kind[]{INT, STRING, STRING, STRING, STRING,
		       INT, FLOAT, FLOAT,
		       INT, FLOAT, DATE, STRING,
		       STRING, INT, DATE, DATE});

	    Grid g = new Grid(key, in);
	    g.pull();
	    jt = g.getTable();

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
