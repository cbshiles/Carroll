package sourceone.pages;
import java.awt.*;

public class ReportMenu extends Page {

    public ReportMenu(Page p){
	super("Report Menu", p);
	place(.9f*3/4, .1f, .9f/4, .4f);
	setLayout(new GridLayout(0, 1));

	addRed("Reserve Report", ReserveReport.class, false);
	addRed("Monthly Report", MonthlyReport.class, false);
	addRed("SourceOne Contract Report", OurContractReport.class, false);

	setVisible(true);
    }
}
