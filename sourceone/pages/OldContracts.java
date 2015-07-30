package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;

public class OldContracts extends TablePage{

    public OldContracts(Page p, boolean full){
	super("Finished Contracts", p);
	//SELECT * FROM Contracts WHERE Paid_Date IS NOT NULL

	Key custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});

	Key contKey = Key.contractKey.just(new String[] {
		"Number of Payments", "Amount of Payment", "Final Payment Amount",
		"Payment Frequency", "Total Contract", "Start Date", "VIN", "Payments Made", "Gross Amount",
		"Reserve", "Net Amount", "Other Payments", "Paid Off", "Payments Made", "Customer ID"
	    });

	if (!full) contKey = contKey.accept(new String[]{"Total Contract"});

	String op = full?">":"<";

	try {
	    Input in = new QueryIn(custKey, contKey, "WHERE Contracts.Paid_Off IS NOT NULL AND Contracts.Customer_ID = Customers.ID AND Contracts.Total_Contract "+op+" 0.01;");
	    Key k  = custKey.add(contKey.cuts);
	    g = new Grid(k, in);
	    g.pull();
	    g.addView(null, null,null);
	    pushTable();
	} catch (Exception e) {new XcptDialog(OldContracts.this, e);}

	tablePlace();
	setVisible(true);
    }
}
