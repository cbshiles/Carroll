package sourceone.pages;
import sourceone.fields.*;
import sourceone.csv.*;
import sourceone.key.*;
import sourceone.sql.*;
import static sourceone.key.Kind.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ContractMenu extends Page {

    JButton addContract = new JButton("Add Contract");
    JButton viewForm = new JButton("Create Report");
    JButton payOff = new JButton("Pay Off");
    JButton payAll = new JButton("Routine Payments");
    
    public ContractMenu(){
	super("Contract Page");
	setSize(400, 600);
	setLayout(new GridLayout(0, 1));
	
	add(addContract);
	addContract.addActionListener(new PageMaker(ContractForm.class));
	
	add(viewForm);
	viewForm.addActionListener(new PageMaker(AReport.class));

	add(payAll);
	payAll.addActionListener(new PageMaker(PayContracts.class));
	
	add(payOff);
	payOff.addActionListener(new PageMaker(PayOff.class));
	
	//pack();
	setVisible(true);
    }
}
