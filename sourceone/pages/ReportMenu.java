package sourceone.pages;
import java.awt.*;

public class ReportMenu extends Page {

    public ReportMenu(Page p){
	super("Report Menu", p);
	place(.9f*3/4, .1f, .9f/4, .4f);
//	setBounds(550, 300, 400, 300);
	setLayout(new GridLayout(1, 0));

	addRed("Reserve Report", ReserveReport.class, false);
	addRed("Monthly Report", MonthlyReport.class, false);
//	pack();
	setVisible(true);
    }
}
