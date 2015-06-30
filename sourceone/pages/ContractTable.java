package sourceone.pages;

import static sourceone.key.Kind.*;
import sourceone.key.*;
import sourceone.fields.*;
import sourceone.sql.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class ContractTable extends Page {

    JTable jt;
    JPanel jp = new JPanel(new BorderLayout());
    JButton jb;
    
    public ContractTable() throws Exception{
	super("Contract");
	setSize(400, 600);
	setLocation(200, 100);


	Key key = new Key(
	    new String[]{"ID", "First Name", "Last Name", "Address", "Phone Number",
			 "Number of Payments", "Amount", "Final Payment",
			 "Payment Frequency", "Total of Payments", "Start Date", "Vehicle",
			 "VIN", "Payments made", "Paid off day"},
	    new Kind[]{INT, STRING, STRING, STRING, STRING,
		       INT, FLOAT, FLOAT,
		       INT, FLOAT, DATE, STRING,
		       STRING, INT, DATE});

	Input i = new QueryIn("SELECT * FROM Contracts");

	Grid g = new Grid(key , i);
	g.pull();

	View v = g.addView(new int[]{0, 1, 2, 3, 4, 11, 12}, 
			   new Cut[]{new StringCut("Name"), new DateCut("Next Due"), new IntCut("Payments due"),
				     new FloatCut("Reserve"), new FloatCut("Gross Amount"), new FloatCut("Net Amount")},
			   new Enterer(){
			   	   public Object[] editEntry(Object[] o){
				       Object[] ans = new Object[6];

				       ans[0] = (String)o[1]+", "+o[2];
				       
				       LocalDate start = (LocalDate)o[10];
				       int f = (int)o[8];
				       ans[1] = next(f, start);

				       int pays = 0; LocalDate di = start;
				       LocalDate today = LocalDate.now();
				       while (today.compareTo(di) > 0){
					   di = next(f, di);
					   pays++;
				       }
				       ans[2] = pays;
				       float cost = (float)o[9];

				       float reserve = cost*.1f;
				       ans[3] = reserve;

				       float gross = cost - reserve;
				       ans[4] = gross;
				       ans[5] = gross*.72f;


				       //This is for FloorPlan
				       //float cost = (float)o[9];
				       //float dRate = cost*.0007f;
				       //ans[3] = dRate;
//				       LocalDate last;
// new FloatCut("Daily rate"), new IntCut("Days Active"), new FloatCut("Accrued Interest")},
				       // if (o[14] != null)
				       // 	   last = (LocalDate)o[14];
				       // else
				       // 	   last = today;

				       // System.out.println("last: "+last);
				       // int days = (int)ChronoUnit.DAYS.between(start, last);
				       // ans[4] = days;

				       // ans[5] = dRate*days;
				       
				       return ans; 
			   	   }

			   	   public LocalDate next(int freq, LocalDate ld){
				       LocalDate due;
				       if (freq == 30) due = ld.plusMonths(1);
				       else due = ld.plusDays(freq);
			   	       return due;
			   	   }
			       });
	g.push();
	//jt = g.getTable();
	jt = v.getTable();
	jp.add(new JScrollPane(jt), BorderLayout.NORTH);

	setContentPane(jp);	
//	pack();
	setVisible(true);	
    }
}

	    // char ch;
	    // int freq = (Integer)slzt.get(8);
	    // LocalDate due;
	    
	    // if (freq == 30){
	    // 	ch = 'M';
	    // 	due = start.plusMonths(1);
	    // } else {
	    // 	if (freq == 7) ch = 'W';
	    // 	else if (freq == 14) ch = 'B';
	    // 	else throw new Exception("Invalid payment frequency");
	    // 	due = start.plusDays(freq);
	    // }

	    // String terms = slzt.get(5).toString();
	    // terms += " "+ch+" @ "+princ((float)slzt.get(6));

	    // objs[i][1] = terms;
	    // objs[i][2] = princ(start);
	    // objs[i][3] = princ(due);

    // static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // String princ(LocalDate d){
    // 	return d.format(dtf);
    // }

    // String princ(float f){
    // 	return String.format("%.02f", f);
    // }
