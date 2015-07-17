package sourceone.pages;
import java.awt.*;

public class HomePage extends Page {

    public HomePage(){
	super("Home Page");
	setSize(400, 600);
	setLayout(new GridLayout(0, 1));

	addRed("Floor Plan", FloorMenu.class);
	addRed("Contracts", ContractMenu.class);
	pack();
	setVisible(true);
    }
}
