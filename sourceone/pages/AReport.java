package sourceone.pages;

import sourceone.csv.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.fields.*;
import static sourceone.key.Kind.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

import java.util.Calendar;


public class AReport extends Page{

    JTable jt;
    JPanel jp = new JPanel(new BorderLayout());
    JButton jb;
    JScrollPane jsp = new JScrollPane();
    Grid g = null;
    Key inKey = null, viewKey = null;
    
    public void changeDate(LocalDate ld) throws InputXcpt{
	g.clearView(viewKey.cuts, new Ent(inKey, ld));
	g.view.addTable();
	jsp.setViewportView(jt = (JTable)g.push());
    }
    
    public AReport(){
	super("Create AR Report");
	setSize(1000, 600);

	Key custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});
	
	Key contKey = Key.contractKey.just(new String[] {
		"ID", "Number of Payments", "Amount of Payment", "Final Payment Amount",
		"Payment Frequency", "Total Contract", "Start Date", "Payments Made", "Next Due"});

	jp.add(jsp, BorderLayout.NORTH);
	try{
	    
	    Input in = new QueryIn(custKey, contKey, "WHERE Contracts.Next_Due IS NOT NULL AND Contracts.Customer_ID = Customers.ID");

	    inKey = custKey.add(contKey.cuts);
	    
	    g = new Grid(inKey, in);

	    g.pull();

	    viewKey = new Key(
		new String[]{"Customer Name", "Terms", "Payments Made", "Start Date", "Due Date", "Remaining Balance",
					 "Payments Due", "Total Amount Due"},
		new Kind[]{STRING, STRING, INT, DATE, DATE, FLOAT, STRING, FLOAT});

	    changeDate(LocalDate.now());
	    
	    JPanel cPan = new JPanel();

	    sourceone.fields.TextField payDay = new sourceone.fields.TextField("Report for:",
							  BasicFormatter.cinvert(LocalDate.now()));

	    cPan.add(payDay.getJP());

	    cPan.add(jb = new JButton("Create Report"));

	    jp.add(cPan, BorderLayout.SOUTH);

	    payDay.addListener(new DocumentListener() {
		    public void changedUpdate(DocumentEvent e) {
			//System.out.println("changedUpdate");
			warn();
		    }
		    public void removeUpdate(DocumentEvent e) {
			//System.out.println("removeUpdate");
			warn();
		    }
		    public void insertUpdate(DocumentEvent e) {
			//System.out.println("insertUpdate");
			warn();
		    }

		    public void warn() {
			try {
			    LocalDate d = StringIn.parseDate(payDay.text());
			    changeDate(d);
			} catch (InputXcpt ix) {;}
		    }
		});
/*
  Print out report 
somehow indicate the creation of this payment block in the database

 */
	    jb.addActionListener(new ActionListener() {
	    	    public void actionPerformed(ActionEvent e) {
			try {
			    View pView = g.view.addView(new String[]{"Payments Made"}, null, null);
			    Key pKey = new Key(new Cut[]{new StringCut("Last name"), new StringCut("First name")});
			    pKey = pKey.add(pView.key.accept(new String[]{"Customer Name"}).cuts);
			    
			    pView.addOut(new CSVOutput(pKey, "poppa_smU4f.csv"));
			    g.push();
			}catch (Exception x)
			{x.printStackTrace(); 
			    System.err.println("~?~ "+x);}
		    }});
		
	} catch (Exception e)
	{e.printStackTrace(); System.err.println("YO!: ");}

	setContentPane(jp);

	setVisible(true);
    }

    private LocalDate next(LocalDate du, int freq, LocalDate st){
	LocalDate due;
	if (freq == 30) {
	    due = du.plusMonths(1);
	    int len = due.getMonth().length(Year.isLeap(due.getYear()));
	    int sday = st.getDayOfMonth();
	    int dday = due.getDayOfMonth();
	    if (dday < sday && len > dday){
		due = due.plusDays( (len<sday?len:sday) - dday);
	    }
	}
	else due = du.plusDays(freq);
	return due;
    }


    private class Ent implements Enterer{

	int ln, fn, aop, nop, fp, pf, sd, tc, nd, pm;
	LocalDate till;
	    
	public Ent(Key k, LocalDate t){
	    till = t;
	    
	    ln = k.dex("Last Name");
	    fn = k.dex("First Name");

	    aop = k.dex("Amount of Payment");
	    nop = k.dex("Number of Payments");
	    fp = k.dex("Final Payment Amount");
	    
	    pf = k.dex("Payment Frequency");
	    nd = k.dex("Next Due");
	    pm = k.dex("Payments Made");
	    sd = k.dex("Start Date");
	    tc = k.dex("Total Contract");
	}

	public Object[] editEntry(Object[] o){
	    int nPays, freq = (int)o[pf];
	    LocalDate due = (LocalDate)o[nd];
	    float amt = (float)o[aop];

	    float finalPayment = (float)o[fp];
	    int fin = (finalPayment > 0.01)?1:0;

	    int tmp = numPays((LocalDate)o[sd], freq, due);
	    int maxStd = (int)o[nop] - (int)o[pm];
	    int maxPays = maxStd + fin;
	    nPays = (tmp<maxPays)?tmp:maxPays;

	    float amtDue = nPays*amt;
	    if (nPays > maxStd)
		amtDue += finalPayment - amt;


	    return new Object[] {
		""+o[ln]+", "+o[fn],
		terms((int)o[nop], amt, freq, finalPayment),
		o[pm],
		o[sd],
		due,
		(float)o[tc] - (int)o[pm]*amt, //# *assumes no other payment*
		nPays,
		amtDue
	    };
	}
	
	public int numPays(LocalDate st, int freq, LocalDate due){
	    int pays = 0;
	    while (till.compareTo(due) >= 0){
		due = next(due, freq, st);
		pays++;
	    }
	    return pays;
	}


	public String terms(int num, float amt, int freq, float fin){
	    char c;
	    if (freq==7) c='W';
	    else if (freq ==14) c='B';
	    else c='M';
	    return ""+num+" "+c+" @ "+amt+"& 1 @ "+fin;
	}
    }

}
