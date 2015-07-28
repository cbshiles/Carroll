package sourceone.pages;

import sourceone.key.*;

public class OldPartial extends TablePage{

    public OldPartial(Page p){
	super("Finished Contracts", p);
	//SELECT * FROM Contracts WHERE Paid_Date IS NOT NULL

	Key custKey = Key.customerKey.just(new String[] {"Last Name", "First Name"});

	Key contKey = Key.contractKey.just(new String[] {
		"ID", "Number of Payments", "Amount of Payment", "Final Payment Amount",
		"Payment Frequency", "Total Contract", "Start Date", "Payments Made", "Next Due", "Gross Amount"
	    });

	setVisible(true);
    }
}
