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

public class AReport extends FullnessPage{

    JButton jb;
    
    public AReport(){
	super("Create AR Report");

	try{
	    getTable(LocalDate.now());
	    
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
			    getTable(d);
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
			    pView.addOut(new CustReport(pKey, "AR_Report_"+reportDate+".csv", ",,,,,"+thing1+",,"+thing2));

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
				SQLBot.bot.update("UPDATE Meta SET "+sel+"_Report_Date='"+reportDate+"' WHERE ID=1;");

			}catch (Exception x)
			{x.printStackTrace(); 
			    System.err.println("~?~ "+x);}
		    }});
		
	} catch (Exception e)
	{e.printStackTrace(); System.err.println("YO!: ");}
    }
}
