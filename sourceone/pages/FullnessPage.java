package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import static sourceone.key.Kind.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public abstract class FullnessPage extends BackPage{
    boolean firstTable = true;
    float thing1, thing2;
    LocalDate reportDate, prd;
    JTextField balSum, totSum;
    Key viewKey;
    
    public FullnessPage(String title, Page p){
	super(title, p);
    }

    protected void init(){
	if (ded)  {kill(); return;} // take care of ded possibilities (subclasses must also)
	
	custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});
	
	contKey = Key.contractKey.just(new String[] {
		"ID", "Number of Payments", "Amount of Payment", "Final Payment Amount",
		"Payment Frequency", "Total Contract", "Start Date", "Payments Made", "Next Due", "Gross Amount"});

	try{
	    prd = SQLBot.bot.query1Date("SELECT "+sel+"_Report_Date FROM Meta WHERE ID=1;");
	    SQLBot.bot.done();
	}catch(Exception e){System.err.println("@#: "+e); return;}
	
	viewKey = new Key(
	    new String[]{"Customer Name", "Terms", "Payments Made", "Start Date", "Due Date", "Remaining Balance",
			 "Payments Due", "Total Amount Due"},
	    new Kind[]{STRING, STRING, INT, DATE, DATE, FLOAT, STRING, FLOAT});
    }

    public static LocalDate next(LocalDate du, int freq, LocalDate st){
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

    protected void getTable() {
	g.clearView(viewKey.cuts, new ContractEnt(reportDate));

	try{ g.push1();

	    g.view.addView(null, null, null);
	    g.view.view.addTable();
	    g.view.push1();
	    jsp.setViewportView(jt = (JTable)g.view.view.push());}
	catch (InputXcpt ix){System.err.println("Error in outputting data to table:\n"+ix);}
	catch (Exception e){e.printStackTrace();}

	if (firstTable){
	    balSum.setText(""+(thing1 = g.view.floatSum("Remaining Balance")));
	    firstTable = false;
	}
	totSum.setText(""+(thing2 = g.view.floatSum("Total Amount Due")));
    }

    public class ContractEnt implements Enterer{

	int ln, fn, aop, nop, fp, pf, sd, tc, nd, pm, grs;
	LocalDate till;
	    
	public ContractEnt(LocalDate t){
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

	    grs = k.dex("Gross Amount"); 
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

	    float tep; //total expected to pay
	    float tcO = (float)o[tc];
	    if (tcO < .001)
		tep = (float)o[grs];
	    else
		tep = tcO;

	    //System.out.println("~"+(tep - (int)o[pm]*amt)+" = "+tep+" - "+(int)o[pm] +"*"+amt);


	    return new Object[] {
		""+o[ln]+", "+o[fn],
		terms((int)o[nop], amt, freq, finalPayment),
		o[pm],
		o[sd],
		due,
		tep - (int)o[pm]*amt, //# *assumes no other(non-standard) payment has been made*
		nPays,
		amtDue
	    };
	}

	public boolean fequal(float a, float b){
	    return 0.01 > Math.abs(a-b);}
    
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
	    String trms = ""+num+" "+c+" @ "+amt;
	    if (! fequal(fin, 0f))
		trms += " & 1 @ "+fin;
	    return trms;
	}


    }

}
