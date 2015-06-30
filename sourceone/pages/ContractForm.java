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

	Key key = new Key(
	    new String[]{"First Name", "Last Name", "Address", "Phone Number",
			 "Number of Payments", "Amount", "Final Payment",
			 "Payment Frequency", "Total of Payments", "Start Date", "Vehicle", "VIN"},
	    new Kind[]{STRING, STRING, STRING, STRING, INT, FLOAT, FLOAT, INT, FLOAT, DATE, STRING, STRING});
	Grid g = new Grid(key, new StringIn(this));

	submit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
		    try {
			ContractForm.this.refresh();
			g.pull();
			Object[] o = g.data.get(0);
			System.out.println((int)o[4]*(float)o[5] + (float)o[6] + " == "+ (float)o[8]);
			if (Math.abs((int)o[4]*(float)o[5] + (float)o[6] - (float)o[8]) > .001)
			    throw new InputXcpt("Payment summation does not equal total");

			View v = g.addView(null, new Cut[]{new IntCut("Payments made"), new DateCut("Paid off day"), new DateCut("Next Due")},
					   new Enterer(){
					       public Object[] editEntry(Object[] objs){
						   return new Object[] {0, null, objs[9]};
					       }
					   });

			v.addOut(new SQLOut(v.key, "Contracts"));
			g.push();
			v.push();
		    } catch (Exception ix){//(InputXcpt ix) {
			System.out.println("Submission error: "+ix.getMessage());
			System.out.println(ix.getClass().getName());
		    }}});
	add(submit);
	pack();
	setVisible(true);	
    }
}
