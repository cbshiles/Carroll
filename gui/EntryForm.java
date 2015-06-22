import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class EntryForm extends Form {

    public EntryForm() throws Exception{
	super("Transaction", "../../db.properties");
	setSize(400, 600);
	setLocation(200, 100);

	setLayout(new GridLayout(0, 1));

	addF(new TextField("First Name"));
	addF(new TextField("Last Name"));
	addF(new TextField("Address"));
	addF(new TextField("Phone Number"));

	addF(new TextField("Number of Payments"));
	addF(new TextField("Amount of Payments"));

	addF(new RadioField("Payment Frequency",
			    new String[]{"Weekly", "Biweekly", "Monthly"},
			    new String[]{"7", "14", "30"}));

	addF(new OptionField("Final Payment", "0", true));

	addF(new TextField("Total of Payments"));

	addF(new TextField("Make"));
	addF(new TextField("Model"));
	addF(new TextField("VIN"));

	JButton submit = new JButton("Submit");
	submit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
		    try {
			bot.insertInit("Transactions");

			String s,x;
			int y;
			float f;
			
			s = "First Name";
			x = map.getStr(s);
			bot.insertAdd(noWhite(s), bot.toSQL(x));

			s = "Last Name";
			x = map.getStr(s);
			bot.insertAdd(noWhite(s), bot.toSQL(x));

			s = "Address";
			x = map.getStr(s);
			bot.insertAdd(noWhite(s), bot.toSQL(x));

			s = "Phone Number";
			x = map.getStr(s);
			bot.insertAdd(noWhite(s), bot.toSQL(x));

			s = "Number of Payments";
			int numPay = map.getInt(s);
			bot.insertAdd(noWhite(s), bot.toSQL(numPay));

			s = "Amount of Payments";
			float onePayment = map.getFloat(s);
			bot.insertAdd(noWhite(s), bot.toSQL(onePayment));

			s = "Payment Frequency";
			y = map.getInt(s);
			bot.insertAdd(noWhite(s), bot.toSQL(y));

			s = "Final Payment";
			float fin = map.getFloat(s);
			bot.insertAdd(noWhite(s), bot.toSQL(fin));

			s = "Total of Payments";
			float total = map.getFloat(s);
			bot.insertAdd(noWhite(s), bot.toSQL(total));

			float sum = numPay*onePayment + fin;
			if (Math.abs(sum - total) > .001)
			    throw new InputXcpt("Payment summation does not equal total");

			bot.insertToday("Date");
			
			bot.insertSend();

			bot.printSet(bot.query("SELECT * FROM Transactions"));
			
		    } catch (Exception ix){//(InputXcpt ix) {
			System.out.println(ix.getMessage());
		    }
		}
	    });

	add(submit);
	pack();
	setVisible(true);	
    }
}
