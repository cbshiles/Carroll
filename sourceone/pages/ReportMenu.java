package sourceone.pages;

import java.awt.*;
import sourceone.pages.reports.*;

public class ReportMenu extends Page {

    public ReportMenu(Page p){
	super("Report Menu", p);
	place(.9f*3/4, .1f, .9f/4, .4f);
	setLayout(new GridLayout(0, 1));

	addRed("Reserve Report", ReserveReport.class, false);
	addRed("Monthly Report", MonthlyReport.class, false);
	addRed("SourceOne Contract Report", OurContractReport.class, false);

	addRed("Payments Report", PaymentsReport.class, false);
	addRed("Purchase Report", PurchaseReport.class, false);
	addRed("Inventory Report", InvReport.class, false);
	setVisible(true);
    }
}
