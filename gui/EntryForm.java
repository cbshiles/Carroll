import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class EntryForm extends JFrame {

    DataMap map = new DataMap();
    public static Color bkgd = new Color(218, 218, 218);
    SQLBot bot;

    public void addF(Field f) {
	map.put(f);
	f.attach(this);
    }

    public String noWhite(String a){
	return a.trim().replaceAll("\\s", "_");
    }
    
    public EntryForm() {
	super("Transaction");
	setSize(400, 600);
	setLocation(200, 100);

	try {
	bot = new SQLBot("../../db.properties");
	} catch (Exception e) {
	    System.err.println("Error creating SQLBot: "+e.getClass().getName());
	}

	setLayout(new GridLayout(0, 1));

	JPanel textBoxPanel = new JPanel();
	textBoxPanel.setLayout(new GridLayout(0, 2, 30, 20));

	getContentPane().setBackground(bkgd);

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
//	JButton cancel = new JButton("Cancel");

//	cancel.setMargin(new Insets(50, 25, 50, 25));
//	cancel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


//	submit.addActionListener(new SubmitAction());
//	textBoxPanel.add(cancel);


	//add(textBoxPanel);

	// JPanel carPanel = new JPanel();
	// carPanel.setLayout(new GridLayout(0, 2, 30, 20));
	// map.put(new TextField("Make", carPanel));
	// map.put(new TextField("Model", carPanel));
	// map.put(new TextField("VIN", carPanel));

	
	// add(carPanel);

	
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//will actually want DISPOSE_ON_CLOSE, i believe


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
			y = map.getInt(s);
			bot.insertAdd(noWhite(s), bot.toSQL(y));

			s = "Amount of Payments";
			f = map.getFloat(s);
			bot.insertAdd(noWhite(s), bot.toSQL(f));

			s = "Payment Frequency";
			y = map.getInt(s);
			bot.insertAdd(noWhite(s), bot.toSQL(y));

			s = "Final Payment";
			f = map.getFloat(s);
			bot.insertAdd(noWhite(s), bot.toSQL(f));

			s = "Total of Payments";
			f = map.getFloat(s);
			bot.insertAdd(noWhite(s), bot.toSQL(f));

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
