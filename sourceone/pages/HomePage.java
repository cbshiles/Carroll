package sourceone.pages;
import java.awt.*;

public class HomePage extends Page {

    public HomePage(){
	super("Home Page", null);
	place(0f, 0f, .9f, .1f);
//	setBounds(550, 300, 400, 300);
	setLayout(new GridLayout(1, 0));

	addRed("Floor Plan", FloorMenu.class, false);
	addRed("Contracts", ContractMenu.class, false);
	addRed("Inactive Inventory", FrozeMenu.class, false);
	addRed("Reserve Report", ReserveReport.class, false);
//	pack();
	setVisible(true);
    }
}
