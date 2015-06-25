import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FloorPage extends Page {

    JButton addCar = new JButton("Add Car");
    JButton addTitle = new JButton("Add Title");
    JButton payOff = new JButton("Pay Off");

    public FloorPage(){
	super("Floor Plan");
	setSize(400, 600);
	setLayout(new GridLayout(0, 1));
	
	add(addCar);
	addCar.addActionListener(new Relocator<CarForm>(CarForm.class));
	
	add(addTitle);
	addTitle.addActionListener(new Relocator<TitlePage>(TitlePage.class));
	
	add(payOff);
//	payOff.addActionListener(new Relocator<PayPage>(PayPage.class));
	
	//pack();
	setVisible(true);
    }
}
