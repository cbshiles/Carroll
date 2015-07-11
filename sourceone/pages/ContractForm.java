package sourceone.pages;

import javax.swing.*;
//import java.awt.*;
import java.awt.event.*;

import static sourceone.key.Kind.*;
import sourceone.key.*;
import sourceone.sql.*;
import sourceone.fields.*;

public class ContractForm extends Form {
    public ContractForm() throws Exception{
	super("Contract");
	setSize(400, 600);
	setLocation(200, 100);

	setLayout(new java.awt.GridLayout(0, 1));

	addF(new TextField("First Name"));
	addF(new TextField("Last Name"));
	addF(new TextField("Address"));
	addF(new TextField("Phone Number"));

	addF(new TextField("Number of Payments"));
	addF(new TextField("Amount"));
	addF(new OptionField("Final Payment", "0", true));
	
	addF(new RadioField("Payment Frequency",
			    new String[]{"Weekly", "Biweekly", "Monthly"},
			    new String[]{"7", "14", "30"}));

	addF(new TextField("Total of Payments"));

	addF(new TextField("Start Date"));

	addF(new TextField("Vehicle"));
	addF(new TextField("VIN"));

	JButton submit = new JButton("Submit");

	Key custKey = Key.customerKey.accept(new String[]{"ID", "email"});
	Grid custGrid = new Grid(custKey, new StringIn(this));
	custGrid.addView(null, null, null);
	custGrid.view.addOut(new SQLFormatter(new InsertDest(custGrid.view.key, "Customers", true)));

	
	Key contKey = Key.contractKey.accept(new String[]{"ID", "Reserve", "Gross Amount", "Net Amount", "Next Due", "Paid Off", "Other Payments", "Customer ID", "Payments Made"});
	Ent ent = new Ent(contKey);
	Grid contGrid = new Grid(contKey, new StringIn(this));
	contGrid.addView(null, new Cut[]{new DateCut("Next Due"), new FloatCut("Other Payments"), new IntCut("Customer ID"),
					 new FloatCut("Reserve"), new FloatCut("Gross Amount"), new FloatCut("Net Amount")},
	    ent);
	contGrid.view.addOut(new SQLFormatter(new InsertDest(contGrid.view.key, "Contracts")));

	submit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
		    try {
			ContractForm.this.refresh();
			ent.set_id((int)custGrid.go());
			contGrid.go();
		    } catch (InputXcpt ix) {
			new XcptDialog(ContractForm.this, ix);
		    }}});

	add(submit);
	pack();
	setVisible(true);
    }


    private class Ent implements Enterer{
	
	int sd, aop, nop, fpa, tc;
	int cust_id;
	
	public Ent(Key k){
	    sd = k.dex("Start Date");
	    aop = k.dex("Amount of Payment");
	    nop = k.dex("Number of Payments");
	    fpa = k.dex("Final Payment Amount");
	    tc = k.dex("Total Contract");
	}

	public void set_id(int i){cust_id=i;}
    
	public Object[] editEntry(Object[] o)throws InputXcpt{
	    float total, sum;
	    total = (float)o[tc];
	    sum = (int)o[nop] * (float)o[aop] + (float)o[fpa];
	    if (Math.abs(sum - total) > 0.001f)
		throw new InputXcpt(""+sum+" != "+total+"\nPayment summation does not equal total");

	    float p = .1f;
	    float z = .72f;
	    float gross = (1-p)*total;
	    return new Object[]{
		o[sd], //Next Due
		0f, //Other payments
		cust_id,
		p*total, //reserve
		gross,
		gross*z //net
	    };
	}
    }
}
