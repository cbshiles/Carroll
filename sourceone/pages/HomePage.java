package sourceone.pages;

import sourceone.fields.*;
import javax.swing.*;
import java.awt.*;


public class HomePage extends Page {
    JButton floor = new JButton("Floor Plan");
    JButton contracts = new JButton("Contracts");
    
    public HomePage(){
	super("Home Page");
	setSize(400, 600);
	setLayout(new GridLayout(0, 1));
	add(floor);
	add(contracts);
	pack();
	setVisible(true);

	floor.addActionListener(new PageMaker(FloorPage.class));
//	floor.addActionListener(new Relocator<FloorPage>(FloorPage.class));
	//contracts.addActionListener(new Relocator<ContractPage>(ContractPage.class));
    }
}