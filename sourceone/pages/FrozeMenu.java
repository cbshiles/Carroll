package sourceone.pages;
import java.awt.*;

public class FrozeMenu extends Page {

    public FrozeMenu(Page p){
	super("Inactive Inventory", p);
	place(.9f/2, .1f, .9f/4, .4f);
	setLayout(new GridLayout(0, 1));

	addRed("Old Floor", OldFloor.class);
	addRed("Old Full", OldFull.class);
	addRed("Old Partial", OldPartial.class);

	setVisible(true);
    }
}
