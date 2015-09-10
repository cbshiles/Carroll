package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import static sourceone.key.Kind.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class OurContractReport extends BackPage{

    JTextField balText, dueText;
    float balSum, dueSum;

    public OurContractReport(Page p) throws InputXcpt{
	super("Our AR Report", p);

	if (ded) return;
	reload();

	JPanel bPan = new JPanel();
	bPan.setBorder(new javax.swing.border.EmptyBorder(0,10,10,20));

	bPan.setLayout(new GridLayout(0, 7));
	addEmpties(4, bPan);
	bPan.add(balText = new JTextField());
	addEmpties(1, bPan);
	bPan.add(dueText = new JTextField());
	
	jp.add(bPan, BorderLayout.SOUTH);

	getTable();
	wrap();
    }

    protected void init(){
	if (ded)  {kill(); return;} // take care of ded possibilities (subclasses must also)

	custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});

	contKey = Key.contractKey.just(new String[] {
		"ID", "Number of Payments", "Amount of Payment", "Final Payment Amount", "AR Num",
		"Payment Frequency", "VIN", "Total Contract", "Start Date", "Payments Made", "Next Due", "Gross Amount"});

	//this area will obviously change
	viewKey = new Key(
	    new String[]{"Account #", "Customer Name", "Terms", "VIN", "Remaining Balance", "Payout Date", "Amount Due"},
	    new Kind[]{STRING, STRING, STRING, STRING,  FLOAT, DATE, FLOAT});
    }

        protected void getTable() {
	g.clearView(viewKey.cuts, new OurEnt(LocalDate.now()));

	try{ g.push1();

	    g.view.addView(null, null, null);
	    g.view.view.addTable();
	    g.view.push1();
	    g.view.view.sort("Customer Name", true);
	    jsp.setViewportView(jt = (JTable)g.view.view.push());}
	catch (InputXcpt ix){System.err.println("Error in outputting data to table:\n"+ix);}
	catch (Exception e){e.printStackTrace();}

	balText.setText(""+(balSum = g.view.floatSum("Remaining Balance")));
	dueText.setText(""+(dueSum = g.view.floatSum("Amount Due")));
    }


    public class OurEnt implements Enterer{

	int ln, fn, aop, nop, fp, pf, sd, tc, nd, pm, grs, vin, arn;
	LocalDate till;
	
	public OurEnt(LocalDate t){
	    till = t;

	    arn = k.dex("AR Num");
	    
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
	    vin = k.dex("VIN");
	}

	public Object[] editEntry(Object[] o){
	    int nPays, freq = (int)o[pf];
	    LocalDate due = (LocalDate)o[nd];
	    float amt = (float)o[aop];

	    float finalPayment = (float)o[fp];
	    int fin = (finalPayment > 0.01)?1:0;

	    LocalDate sdO = (LocalDate)o[sd];
	    
	    int tmp = numPays(sdO, freq, due);
	    int nopO = (int)o[nop];
	    int maxStd = nopO - (int)o[pm];
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
		o[arn],
		""+o[ln]+", "+o[fn],
		terms((int)o[nop], amt, freq, finalPayment),
		o[vin],
		tep - (int)o[pm]*amt, //# *assumes no other(non-standard) payment has been made*
		nexti(sdO, freq, nopO + fin),
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

	public LocalDate nexti(LocalDate st, int freq, int i){
	    if (freq == 30) 
		return st.plusMonths(i);
	    else
		return st.plusDays(freq*i);
	}

    }
}
