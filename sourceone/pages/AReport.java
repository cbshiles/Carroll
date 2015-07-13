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

public class AReport extends Page{

    JTable jt;
    JPanel jp = new JPanel(new BorderLayout());
    JButton jb;
    JScrollPane jsp = new JScrollPane();
    Grid g = null;
    Key inKey = null, viewKey = null;
    LocalDate reportDay;
    boolean full;
    
    public void changeDate(LocalDate ld) throws InputXcpt{
	g.clearView(viewKey.cuts, new ContractEnt(inKey, ld));
	g.view.addTable();
	jsp.setViewportView(jt = (JTable)g.push());
	reportDay = ld;
    }
    
    public AReport(Boolean f){
	super("Create AR Report");
	full = f;
	setSize(1000, 600);

	Key custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});
	
	Key contKey = Key.contractKey.just(new String[] {
		"ID", "Number of Payments", "Amount of Payment", "Final Payment Amount",
		"Payment Frequency", "Total Contract", "Start Date", "Payments Made", "Next Due", "Gross Amount"});

	jp.add(jsp, BorderLayout.NORTH);
	try{
	    String z = full?">":"<";
	    Input in = new QueryIn(custKey, contKey, "WHERE Contracts.Next_Due IS NOT NULL AND Contracts.Customer_ID = Customers.ID AND Contracts.Total_Contract "+z+"0.01;");

	    //Stopped editting hereerere
	    
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
			} catch (InputXcpt ix) {System.err.println("HGXB");}
		    }
		});

	    jb.addActionListener(new ActionListener() {
	    	    public void actionPerformed(ActionEvent e) {
			try {
			    View pView = g.view.addView(new String[]{"Payments Made"}, null, null);

			    g.view.push1();
			    float thing1 = pView.floatSum("Remaining Balance");
			    float thing2 = pView.floatSum("Total Amount Due");
			    
			    Key pKey = new Key(new Cut[]{new StringCut("Last name"), new StringCut("First name")});
			    pKey = pKey.add(pView.key.accept(new String[]{"Customer Name"}).cuts);
			    pView.addOut(new CustReport(pKey, "AR_Report_"+reportDay+".csv", ",,,,,"+thing1+",,"+thing2));

			    System.err.println("pview size"+pView.data.size());

			    pView.push();

			    //# check if its null, have a confirmation dialog pop up
			    String sel = full?"Full":"Partial";
			    LocalDate prd = SQLBot.bot.query1Date("SELECT "+sel+"_Report_Date FROM Meta WHERE ID=1;");

			    boolean doit = true;
			    
			    if (prd != null){
				ConfirmDialog pop = new ConfirmDialog(AReport.this, "Report Overwrite", ""+prd);
				doit = pop.confirmed();
//			    pop.dispose();
			    }

			    if (doit)
				SQLBot.bot.update("UPDATE Meta SET "+sel+"_Report_Date='"+reportDay+"' WHERE ID=1;");

			}catch (Exception x)
			{x.printStackTrace(); 
			    System.err.println("~?~ "+x);}
		    }});
		
	} catch (Exception e)
	{e.printStackTrace(); System.err.println("YO!: ");}

	setContentPane(jp);

	setVisible(true);
    }
}
