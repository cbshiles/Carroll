package sourceone.pages;
import java.awt.*;

public class FloorMenu extends Page {

    public FloorMenu(){
	super("Floor Plan");
	setSize(400, 600);
	setLayout(new GridLayout(0, 1));

	addRed("Add Car", CarForm.class);
	addRed("Active Report", CarReport.class);
	addRed("Add Title", TitlePage.class);
	addRed("Pay Off", FloorPay.class);
	
	//pack();
	setVisible(true);
    }
}
