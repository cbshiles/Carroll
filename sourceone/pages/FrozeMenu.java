package sourceone.pages;
import java.awt.*;

public class FrozeMenu extends Page {

    public FrozeMenu(Page p){
	super("Inactive Inventory", p);
	setBounds(550, 300, 400, 300);
	setLayout(new GridLayout(0, 1));

	addRed("Old Floor", OldFloor.class);
	addRed("Old Full", OldFull.class);
	addRed("Old Partial", OldPartial.class);
//	pack();
	setVisible(true);
    }
}
