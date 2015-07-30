package sourceone.pages;
import java.awt.*;

public class ContractMenu extends Page {
    
    public ContractMenu(Page p){
	super("Contract Page", p);
	place(.3f, .1f, .3f, .4f);
	setLayout(new GridLayout(0, 1));

	addRed("Add Contract", ContractForm.class);
	addRed("Create Report", AReport.class);
	addRed("Routine Payments", PayContracts.class);
	addRed("Pay Off", PayOff.class);
	
	//pack();
	setVisible(true);
    }
}
