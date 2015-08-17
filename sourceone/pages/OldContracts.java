package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.fields.*;
import java.awt.event.*;
import java.time.*;
import javax.swing.*;

public class OldContracts extends TablePage{
    TextField aDate, bDate;
    Key custKey, contKey;
    boolean full;

    public OldContracts(Page p, boolean full) throws Exception{
	super("Finished Contracts", p);
	this.full = full;
	//SELECT * FROM Contracts WHERE Paid_Date IS NOT NULL

	custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});

	contKey = Key.contractKey.just(new String[] {
		"Number of Payments", "Amount of Payment", "Final Payment Amount",
		"Payment Frequency", "Total Contract", "Start Date", "VIN", "Payments Made", "Gross Amount",
		"Reserve", "Net Amount", "Other Payments", "Paid Off", "Payments Made", "Customer ID"
	    });

	if (!full) contKey = contKey.accept(new String[]{"Total Contract"});

		JPanel cPan = new JPanel();

	aDate = new TextField("Start Date");
	bDate = new TextField("End Date");

	cPan.add(aDate.getJP());
	cPan.add(bDate.getJP());

	FieldListener fl = new FieldListener() {
		public void dew() {
		    try {
			refresh();
		    } catch (InputXcpt ix) {;/*System.err.println("HGXB");*/}
		    catch (Exception e) {new XcptDialog(OldContracts.this, e);}
		}};

	aDate.addListener(fl);
	bDate.addListener(fl);
	    
	jp.add(cPan, java.awt.BorderLayout.SOUTH);

	refresh();
	
	tablePlace();
	setVisible(true);
    }

       private void refresh() throws Exception {
	   String op = full?">":"<";
	   Input in = new QueryIn(custKey, contKey, "WHERE Contracts.Paid_Off IS NOT NULL AND Contracts.Customer_ID = Customers.ID AND Contracts.Total_Contract "+op+" 0.01"+getClause());
	   Key k  = custKey.add(contKey.cuts);
	   g = new Grid(k, in);
	   g.pull();
	   g.addView(null, null,null);
	   pushTable();
       }

        public String getClause() {//get cher time clause
	LocalDate a = null, b = null;
	try{ a = StringIn.parseDate(aDate.text()); }
	catch (InputXcpt ix) {;}
	try{ b = StringIn.parseDate(bDate.text()); }
	catch (InputXcpt ix) {;}

	String str;

	if (a != null || b!= null) str = " AND ";
	else return "";

	String col = "Paid_Off";
		 
	if (a != null)
	    str += col+" >= '"+BasicFormatter.cxnvert(a)+"\'";
	
	if (a != null && b!= null) str += " AND ";

	if (b != null)
	    str += col+" <= '"+BasicFormatter.cxnvert(b)+"\'";
	return str;
    }

}
