package sourceone.pages;
import java.awt.*;

public class HomePage extends Page {

    public HomePage(){
	super("Home Page", null);
	setBounds(550, 300, 400, 300);
	setLayout(new GridLayout(1, 0));

	addRed("Floor Plan", FloorMenu.class);
	addRed("Contracts", ContractMenu.class);
	addRed("Inactive Inventory", FrozeMenu.class);
//	pack();
	setVisible(true);
    }
}
