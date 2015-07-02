package sourceone.pages;
import sourceone.fields.*;
import sourceone.csv.*;
import sourceone.key.*;
import sourceone.sql.*;
import static sourceone.key.Kind.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ContractPage extends Page {

    JButton addContract = new JButton("Add Contract");
    JButton viewForm = new JButton("View Info");
    JButton payOff = new JButton("Pay Off");

    public ContractPage(){
	super("Floor Plan");
	setSize(400, 600);
	setLayout(new GridLayout(0, 1));
	
	add(addContract);
	addContract.addActionListener(new PageMaker(ContractForm.class));
	
	add(viewForm);
	viewForm.addActionListener(new PageMaker(ContractTable.class));
	
	add(payOff);
	payOff.addActionListener(new PageMaker(PayContracts.class));
	
	//pack();
	setVisible(true);
    }
}
