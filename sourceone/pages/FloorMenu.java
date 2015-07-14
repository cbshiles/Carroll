package sourceone.pages;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FloorMenu extends Page {

    JButton addCar = new JButton("Add Car");
    JButton addTitle = new JButton("Add Title");
    JButton payOff = new JButton("Pay Off");

    public FloorMenu(){
	super("Floor Plan");
	setSize(400, 600);
	setLayout(new GridLayout(0, 1));
	
	add(addCar);
	addCar.addActionListener(new PageMaker(this, CarForm.class));
	
	add(addTitle);
	addTitle.addActionListener(new PageMaker(this, TitlePage.class));
	
	add(payOff);
	payOff.addActionListener(new PageMaker(this, FloorPay.class));
	
	//pack();
	setVisible(true);
    }
}
