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


	Key key = Key.contractKey;

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
	try{
	jt = v.getTable();
	} catch (Exception e){System.err.println(e.getMessage()+" "+e.getClass().getName());}
	jp.add(new JScrollPane(jt), BorderLayout.NORTH);

	setContentPane(jp);	
//	pack();
	setVisible(true);	
    }
}
