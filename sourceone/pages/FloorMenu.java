package sourceone.pages;

import java.awt.*;
import sourceone.pages.reports.*;

public class FloorMenu extends Page {

    public FloorMenu(Page p){
	super("Floor Plan", p);
	place(0f, .1f, .9f/4, .4f);
	setLayout(new GridLayout(0, 1));

	addRed("Add Car", CarForm.class);
	addRed("Active Report", CarReport.class);
	addRed("Add Title", AddTitle.class);
	addRed("Release Title", ReleaseTitle.class);
	addRed("Pay Off", FloorPay.class);
	addRed("Curtailments", Curtailments.class);
	
	//pack();
	setVisible(true);
    }
}
