package sourceone.pages;

import sourceone.csv.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.fields.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class PayContracts extends FullnessPage {

    JButton jb;
    
    public PayContracts(Page p) throws InputXcpt{
	super("Pay Contracts", p);
	if (ded) return;
	
	if (prd == null) {kill(); throw new InputXcpt("No record of any unpaid reports");}
	reportDate = prd;
	reload();
	
	JPanel cPan = new JPanel();
	cPan.setLayout(new GridLayout(2, 0));
	
	JPanel bPan = new JPanel();
	bPan.setBorder(new javax.swing.border.EmptyBorder(10,10,10,20));
	bPan.setLayout(new GridLayout(0, 8));
	addEmpties(5, bPan);
	bPan.add(balSum = new JTextField());
	addEmpties(1, bPan);
	bPan.add(totSum = new JTextField());
	cPan.add(bPan);
	    
	JPanel aPan = new JPanel();
	sourceone.fields.TextField batchID;
	batchID = new sourceone.fields.TextField("Batch ID: ", "");
	aPan.add(batchID.getJP());
	aPan.add(new JLabel("Posting payments up to "+reportDate));
	aPan.add(jb = new JButton("Post Payments"));
	cPan.add(aPan);

	jp.add(cPan, BorderLayout.SOUTH);
	getTable();

	jb.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    try {
			String bid = batchID.text();
			if (bid.trim().equals("")) throw new InputXcpt("Batch ID cannot be blank");
			java.sql.ResultSet rs = SQLBot.bot.query("SELECT ID FROM Payments WHERE Batch_ID LIKE '"+bid+"'");
			if (rs.next()) throw new InputXcpt("Batch ID already used");
			Click ec = new Click(g.key, g.view.key, bid);
			for (int i=0; i<jt.getRowCount(); i++)
			    ec.editEntry(g.data.get(i), g.view.data.get(i));
			kill();
		    }
		    catch (InputXcpt ix) {new XcptDialog(PayContracts.this, ix);}
		    catch (Exception x)
		    {//System.err.println("Buttons, YO: "+x.getCause()+x.getClass().getName());
			x.printStackTrace();}
		}});

	wrap();
    }

    private class Click {

	int id, fq, nd, pm, fpa, pd, tma, aop, rb, sd;
	int nqme;
	LocalDate tday;
	String bid;
	
	public Click(Key gk, Key vk, String bid){
	    this.bid =bid;
	    tday = LocalDate.now();

	    fq = gk.dex("Payment Frequency");
	    id = gk.dex("ID");
	    fpa = gk.dex("Final Payment Amount");
	    aop = gk.dex("Amount of Payment");
	    nd = gk.dex("Next Due");

	    pm = vk.dex("Payments Made");
	    pd = vk.dex("Payments Due");
	    tma = vk.dex("Total Amount Due");
	    rb = vk.dex("Remaining Balance");
	    sd = vk.dex("Start Date");
	    nqme = vk.dex("Customer Name");
	}

	public boolean fequal(float a, float b){
	    return 0.01 > Math.abs(a-b);}

	public void editEntry(Object[] g, Object[] v){

	    float tot_due = (float)v[tma];
	    int pays_due = (int)v[pd];
	    float pay_amt = (float)g[aop];

	    float fin_amt = (float)g[fpa];

	    LocalDate next_due = (LocalDate)g[nd];
	    LocalDate start_day = (LocalDate)v[sd];

	    boolean finul = fequal(tot_due, (float)v[rb]);
	    boolean last;

	    int di = (int)g[id];

	    // System.err.println(v[nqme]);
	    // System.err.println(tot_due+" "+pays_due+" "+pay_amt+" "+fin_amt);

	    if (fequal(tot_due, pays_due*pay_amt)){
		last = false;
	    }
	    else if (fequal(tot_due, (pays_due-1)*pay_amt + fin_amt)){
		last = true;
		assert finul;
		pays_due -= 1;
	    } else throw new Error("Can't figure out how to make payments");

	    String paidOff = finul?"Paid_Off='"+tday+"', " : "";
	    String oPay = finul?"Other_Payments="+fin_amt+", ":"";
	    String nDue;
	    if (! finul){
		for (int j=0; j<pays_due; j++)
		    next_due = next(next_due, (int)g[fq], start_day);
		nDue = "'"+next_due+"'";
	    }else nDue = "NULL";
	    try{
		String cmda = "UPDATE Contracts SET Payments_Made="+((int)v[pm]+pays_due)+", "+paidOff+oPay+" Next_Due="+nDue+" WHERE ID="+di+';';
		String cmdb = "INSERT INTO Payments (Contract_ID, Day, Amount, Batch_ID) VALUES ("+di+", '"+tday+"', "+tot_due+", '"+bid+"');";
		// System.err.println(cmda);
		// System.err.println(cmdb);
		SQLBot.bot.update(cmda);
		SQLBot.bot.update(cmdb);

		SQLBot.bot.update("UPDATE Meta SET "+sel+"_Report_Date=NULL WHERE ID=1;");	    
	    } catch (Exception x)
	    {System.err.println("Tired of dese: "+x.getCause()+x.getClass().getName());
		System.err.println(x.getMessage());
		x.printStackTrace();
	    }
	}
    }
}
