package sourceone.pages;
import java.awt.*;

public class ContractMenu extends Page {
    
    public ContractMenu(){
	super("Contract Page");
	setSize(400, 600);
	setLayout(new GridLayout(0, 1));

	addRed("Add Contract", ContractForm.class);
	addRed("Create Report", AReport.class);
	addRed("Pay Off", PayContracts.class);
	addRed("Routine Payments", PayOff.class);
	
	//pack();
	setVisible(true);
    }
}
