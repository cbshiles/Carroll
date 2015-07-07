package sourceone.pages;

import sourceone.csv.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.fields.*;
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
	setSize(1000, 600);
	try{
	    Key custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});
	    
	    Key contKey = Key.contractKey.just(new String[] {
		    "ID", "Number of Payments", "Amount of Payment", "Final Payment Amount",
		    "Payment Frequency", "Total Contract", "Start Date", "Payments Made", "Next Due"});

	    Input in = new QueryIn(custKey, contKey, "WHERE Contracts.Next_Due < CURDATE() AND Contracts.Customer_ID = Customers.ID");

	    Key key = custKey.add(contKey.cuts);
	    
	    Grid g = new Grid(key, in);

	    g.clearView(new Key(
			    new String[]{"Customer Name", "Terms", "Start Date", "Due Date", "Remaining Balance",
					 "Payments Due", "Total Amount Due"},
			    new Kind[]{STRING, STRING, DATE, DATE, FLOAT, STRING, FLOAT}).cuts,
			new Ent(key));
	    g.view.addTable();
	    jp.add(new JScrollPane(jt = (JTable)g.go()), BorderLayout.NORTH);

	    JPanel cPan = new JPanel();

	    Field payDay = new sourceone.fields.TextField("Date paid:",
							  BasicFormatter.cinvert(LocalDate.now()));

	    cPan.add(payDay.getJP());

	    cPan.add(jb = new JButton("Post Payments"));

	    jp.add(cPan, BorderLayout.SOUTH);

	    setContentPane(jp);

	    jb.addActionListener(new ActionListener() {
	    	    public void actionPerformed(ActionEvent e) {
			try {
			    Enterer ec = new Click(key, StringIn.parseDate(payDay.text()));
			    for (int i : jt.getSelectedRows())
				ec.editEntry(g.data.get(i));
			} catch (Exception x)
			{System.err.println("Buttons, YO: "+x.getCause()+x.getClass().getName());
			    System.err.println(x.getMessage());}
		    }});
	} catch (Exception e)
	{e.printStackTrace(); System.err.println("YO!: ");}

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

    private class Click  implements Enterer{

	int id, fq, nd, pm, fpa, nop, sd;
	LocalDate paid;
	
	public Click(Key k, LocalDate p){
	    paid = p;
	    fq = k.dex("Payment Frequency");
	    nd = k.dex("Next Due");
	    pm = k.dex("Payments Made");
	    id = k.dex("ID");
	    nop = k.dex("Number of Payments");
	    fpa = k.dex("Final Payment Amount");
	    sd = k.dex("Start Date");
	}

	public Object[] editEntry(Object[] o){
	    int pays = (int)o[pm];
	    int diff = (int)o[nop] - pays;
	    float fin = (float)o[fpa];
	    String where = "WHERE ID="+o[id];
	    if (diff == 0){//all standard payments made
		//SQLBot.bot.update("UPDATE Contracts SET Other_Payments="+fin+", Paid_Off="+paid+", Next_Due=NULL "+where);

		
	    } else { //we'll be making a standard payment
		pays += 1;
		if (diff == 1 && fin < 0.01){ // last standard, no final

		//set paid off day
		//clear out next due
		}else { 
		    //increment due date
		}
	    }

	    LocalDate due = next((LocalDate)o[nd], (int)o[fq], (LocalDate)o[sd]);
	    try {
		//System.out.println("UPDATE Contracts SET Payments_made="+pays+", Next_Due='"+due+"' WHERE ID="+o[id]+';');
		//System.out.println("INSERT INTO Payments (Contract_ID, Day) VALUES ("+o[id]+", '"+paid+"');");
	    
	    SQLBot.bot.update("UPDATE Contracts SET Payments_Made="+pays+", Next_Due='"+due+"' WHERE ID="+o[id]+';');
	    SQLBot.bot.update("INSERT INTO Payments (Contract_ID, Day) VALUES ("+o[id]+", '"+paid+"');");
	    } catch (Exception x)
	    {System.err.println("Tired of dese: "+x.getCause()+x.getClass().getName());
		System.err.println(x.getMessage());}
	    return null;
	}
    }

	    Key custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});
	    
	    Key contKey = Key.contractKey.just(new String[] {
		    "ID", "Number of Payments", "Amount of Payment", "Final Payment Amount",
		    "Payment Frequency", "Total Contract", "Start Date", "Payments Made", "Next Due"});

    
    private class Ent implements Enterer{

	int ln, fn, aop, nop, fp, pf, sd, tc, nd, pm;
	    
	public Ent(Key k){
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

	    int tmp = numPays((LocalDate)o[sd], freq, due);
	    int maxPays = (int)o[nop] - (int)o[pm];
	    nPays = (tmp<maxPays)?tmp:maxPays;

	    float finalPayment = (float)o[fp];
	    boolean fin = finalPayment > 0.01;

	    return new Object[] {
		""+o[ln]+", "+o[fn],
		terms((int)o[nop], amt, freq),
		o[sd],
		due,
		(float)o[tc] - (int)o[pm]*amt, //# assumes no other payment
		nPays + (fin?1:0),
		nPays*amt + finalPayment
	    };
	}
	
	public int numPays(LocalDate st, int freq, LocalDate due){
	    int pays = 0;
	    LocalDate today = LocalDate.now();
	    while (today.compareTo(due) > 0){
		due = next(due, freq, st);
		pays++;
	    }
	    return pays;
	}


	public String terms(int num, float amt, int freq){
	    char c;
	    if (freq==7) c='W';
	    else if (freq ==14) c='B';
	    else c='M';
	    return ""+num+" "+c+" @ "+amt;
	}
    }

}
