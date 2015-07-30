package sourceone.pages;
import java.awt.*;

public class FrozeMenu extends Page {

    public FrozeMenu(Page p){
	super("Inactive Inventory", p);
	place(.6f, .1f, .3f, .4f);
	setLayout(new GridLayout(0, 1));

	addRed("Old Floor", OldFloor.class);
	addRed("Old Full", OldFull.class);
	addRed("Old Partial", OldPartial.class);
//	pack();
	setVisible(true);
    }
}
