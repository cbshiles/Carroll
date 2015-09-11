package sourceone.pages;
import java.awt.*;

public class ContractMenu extends Page {
    
    public ContractMenu(Page p){
	super("Contract Page", p);
	place(.9f/4, .1f, .9f/4, .4f);
	setLayout(new GridLayout(0, 1));

	addRed("Add Contract", ContractForm.class);
	addRed("Create Report", AReport.class);
	addRed("Routine Payments", PayContracts.class);
	addRed("Pay Off", PayOff.class);

	addRed("Add Contract Title", ContAddTitle.class);
	addRed("Release Contract Title", ContDropTitle.class);

	
	//pack();
	setVisible(true);
    }
}
