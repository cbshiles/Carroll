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
			    new String[]{"Customer Name", "Terms", "Payments Made", "Start Date", "Due Date", "Remaining Balance",
					 "Payments Due", "Total Amount Due"},
			    new Kind[]{STRING, STRING, INT, DATE, DATE, FLOAT, STRING, FLOAT}).cuts,
			new ContractEnt(key, reportDate));
	    g.view.addTable();
	    jp.add(new JScrollPane(jt = (JTable)g.go()), BorderLayout.NORTH);

	    JPanel cPan = new JPanel();

	    cPan.add(jb = new JButton("Post Payments"));

	    jp.add(cPan, BorderLayout.SOUTH);

	    setContentPane(jp);

	    jb.addActionListener(new ActionListener() {
	    	    public void actionPerformed(ActionEvent e) {
			try {
			    Enterer ec = new Click(key);
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

    private class Click  implements Enterer{

	int id, fq, nd, pm, fpa, nop, sd;
	LocalDate paid;
	
	public Click(Key k){
	    paid = reportDate;
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
	    String where = "WHERE ID="+o[id]+';';
	    LocalDate due = ContractEnt.next((LocalDate)o[nd], (int)o[fq], (LocalDate)o[sd]);
		    

	    try {
		if (diff == 0){//all standard payments made
		    SQLBot.bot.update("UPDATE Contracts SET Other_Payments="+fin+", Paid_Off='"+paid+"', Next_Due=NULL "+where);
		    SQLBot.bot.update("INSERT INTO Payments (Contract_ID, Day, Amount) VALUES ("+o[id]+", '"+paid+"', "+fin+");");
		} else { //we'll be making a standard payment
		    pays += 1;
		    if (diff == 1 && fin < 0.01){ // last standard, no final
			SQLBot.bot.update("UPDATE Contracts SET Payments_Made="+pays+", Paid_Off='"+paid+"', Next_Due=NULL "+where);
		    }else { 
			SQLBot.bot.update("UPDATE Contracts SET Payments_Made="+pays+", Next_Due='"+due+"' "+where);
		    }
		    SQLBot.bot.update("INSERT INTO Payments (Contract_ID, Day) VALUES ("+o[id]+", '"+paid+"');");
		}

		//System.out.println("UPDATE Contracts SET Payments_made="+pays+", Next_Due='"+due+"' WHERE ID="+o[id]+';');
		//System.out.println("INSERT INTO Payments (Contract_ID, Day) VALUES ("+o[id]+", '"+paid+"');");
	    
	    } catch (Exception x)
	    {System.err.println("Tired of dese: "+x.getCause()+x.getClass().getName());
		System.err.println(x.getMessage());
		x.printStackTrace();
	    }
	    return null;
	}
    }
}
