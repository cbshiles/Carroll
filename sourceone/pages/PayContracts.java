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
    LocalDate reportDate;
    
    public PayContracts(){
	super("Pay Contracts");
	setSize(1000, 600);

	try{
	    
	    Grid nada_surf = new Grid (new Key(new Cut[]{new DateCut("Report Date")}),
				       new QueryIn("SELECT Pending_Report_Date FROM Meta;"));

	    nada_surf.pull();
	    reportDate = (LocalDate) nada_surf.data.get(0)[0];
	    
	    Key custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});
	    
	    Key contKey = Key.contractKey.just(new String[] {
		    "ID", "Number of Payments", "Amount of Payment", "Final Payment Amount",
		    "Payment Frequency", "Total Contract", "Start Date", "Payments Made", "Next Due"});

	    Input in = new QueryIn(custKey, contKey, "WHERE Contracts.Next_Due < '"+reportDate+"' AND Contracts.Customer_ID = Customers.ID");

	    Key key = custKey.add(contKey.cuts);
	    
	    Grid g = new Grid(key, in);

	    g.clearView(new Key(
			    new String[]{"Customer Name", "Terms", "Payments Made", "Start Date", "Next Due", "Remaining Balance",
					 "Payments Due", "Total Amount Due"},
			    new Kind[]{STRING, STRING, INT, DATE, DATE, FLOAT, STRING, FLOAT}).cuts,
			new ContractEnt(key, reportDate));
	    g.view.addTable();
	    jp.add(new JScrollPane(jt = (JTable)g.go()), BorderLayout.NORTH);

	    jt.setRowSelectionAllowed(false);
	    
	    JPanel cPan = new JPanel();

	    cPan.add(new JLabel("Posting payments up to "+reportDate));

	    cPan.add(jb = new JButton("Post Payments"));

	    jp.add(cPan, BorderLayout.SOUTH);

	    setContentPane(jp);

	    jb.addActionListener(new ActionListener() {
	    	    public void actionPerformed(ActionEvent e) {
			try {
			    Click ec = new Click(g.key, g.view.key);
			    for (int i=0; i<jt.getRowCount(); i++)
				ec.editEntry(g.data.get(i), g.view.data.get(i));
			} catch (Exception x)
			{System.err.println("Buttons, YO: "+x.getCause()+x.getClass().getName());
			    System.err.println(x.getMessage());}
		    }});
	} catch (Exception e)
	{e.printStackTrace(); System.err.println("YO!: ");}

	setVisible(true);
    }

    private class Click {

	int id, fq, nd, pm, fpa, pd, tma, aop, rb, sd;
	LocalDate tday;
	
	public Click(Key gk, Key vk){
	    tday = LocalDate.now();

	    fq = gk.dex("Payment Frequency");
	    id = gk.dex("ID");
	    fpa = gk.dex("Final Payment Amount");
	    aop = gk.dex("Amount of Payment");

	    nd = vk.dex("Next Due");
	    pm = vk.dex("Payments Made");
	    pd = vk.dex("Payments Due");
	    tma = vk.dex("Total Amount Due");
	    rb = vk.dex("Remaining Balance");
	    sd = vk.dex("Start Date");
	}

	public boolean fequal(float a, float b){
	    return 0.01 > Math.abs(a-b);}

	public Object[] editEntry(Object[] g, Object[] v){

	    float tot_due = (float)v[tma];
	    int pays_due = (int)v[pd];
	    float pay_amt = (float)g[aop];

	    float fin_amt = (float)g[fpa];

	    LocalDate next_due = (LocalDate)v[nd];
	    LocalDate start_day = (LocalDate)v[sd];

	    boolean finul = fequal(tot_due, (float)v[rb]);
	    boolean last;

	    int di = (int)g[id];

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
		    next_due = ContractEnt.next(next_due, (int)g[fq], start_day);
		nDue = "'"+next_due+"'";
	    }else nDue = "NULL";
	    try{
		String cmda = "UPDATE Contracts SET Payments_Made="+((int)v[pm]+pays_due)+", "+paidOff+oPay+" Next_Due="+nDue+" WHERE ID="+di+';';
		String cmdb = "INSERT INTO Payments (Contract_ID, Day, Amount) VALUES ("+di+", '"+tday+"', "+tot_due+");";
		System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.err.println(cmda);
		System.err.println(cmdb);
		System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		SQLBot.bot.update(cmda);
		SQLBot.bot.update(cmdb);

		SQLBot.bot.update("UPDATE Meta SET Pending_Report_Date=NULL WHERE ID=1;");	    
	    } catch (Exception x)
	    {System.err.println("Tired of dese: "+x.getCause()+x.getClass().getName());
		System.err.println(x.getMessage());
		x.printStackTrace();
	    }
	    return null;
	}
    }
}
