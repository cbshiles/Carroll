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

	//# This is outdated: Next due does not check the field, rather calculates the due date after the beginning
	View v = g.addView(new int[]{0, 1, 2, 3, 4, 11, 12}, 
			   new Cut[]{new StringCut("Name"), new DateCut("Next Due"), new IntCut("Payments due"),
				     new FloatCut("Reserve"), new FloatCut("Gross Amount"), new FloatCut("Net Amount")},
			   new Ent(key));

	v.addTable();
	try{
	    jt = (JTable)g.go();
	} catch (Exception e){System.err.println(e.getMessage()+" "+e.getClass().getName());}
	jp.add(new JScrollPane(jt), BorderLayout.NORTH);

	setContentPane(jp);	
//	pack();
	setVisible(true);	
    }

    private class Ent implements Enterer{

	int sd, fq, tp, ln, fn, nd;
	
	public Ent(Key k){
	    fq = k.dex("Payment Frequency");
	    nd = k.dex("Next Due");
//	    am = k.dex("Amount");
//	    np = k.dex("Number of Payments");
//	    pm = k.dex("Payments made");
	    ln = k.dex("Last Name");
	    fn = k.dex("First Name");
	    sd = k.dex("Start Date");
//	    fp = k.dex("Final Payment");
	    tp = k.dex("Total of Payments");
	}

	public Object[] editEntry(Object[] o){

	    LocalDate start = (LocalDate)o[sd];
	    int f = (int)o[fq];
				       
	    float cost = (float)o[tp];
	    float reserve = cost*.1f;
	    float gross = cost - reserve;

	    return new Object[]{
		""+o[ln]+", "+o[fn],
		next(f, start),
		untilToday(f, start),
		reserve,
		gross,
		gross*.72f
	    };
	}

	public int untilToday(int f, LocalDate then){
	    int pays = 0;
	    LocalDate today = LocalDate.now();
	    while (today.compareTo(then) > 0){
		then = next(f, then);
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

    }

}
