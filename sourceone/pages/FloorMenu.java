package sourceone.pages;
import java.awt.*;

public class FloorMenu extends Page {

    public FloorMenu(Page p){
	super("Floor Plan", p);
	place(0f, .1f, .9f/4, .4f);
	setLayout(new GridLayout(0, 1));

	addRed("Add Car", CarForm.class);
	addRed("Active Report", CarReport.class);
	addRed("Add Title", TitlePage.class);
	addRed("\"Remove\" Title", TitleDone.class);
	addRed("Pay Off", FloorPay.class);
	
	//pack();
	setVisible(true);
    }
}
