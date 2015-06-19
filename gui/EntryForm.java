import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class EntryForm extends JFrame {

    DataMap map = new DataMap();
    static int r = 218;
	public static Color bkgd = new Color(r, r, r);

    public void addF(Field f) {
	map.put(f);
	f.attach(this);
    }
    
    public EntryForm() {
	super("Transaction");
	setSize(400, 600);
	setLocation(200, 100);

	setLayout(new GridLayout(0, 1));

	JPanel textBoxPanel = new JPanel();
	textBoxPanel.setLayout(new GridLayout(0, 2, 30, 20));

	getContentPane().setBackground(bkgd);

	addF(new TextField("First Name"));
	addF(new TextField("Last Name"));
	addF(new TextField("Address"));
	addF(new TextField("Phone Number"));
	addF(new TextField("Total of Payments"));



	addF(new TextField("Number of Payments"));
	addF(new TextField("Amount of Payments"));

	addF(new RadioField("Payment Frequency",
			       new String[]{"Weekly", "Biweekly", "Monthly"}));

	addF(new OptionField("Final payment?", "0", true));
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
			String freq = map.getStr("Payment Frequency");
			System.out.println(freq);
			
			String fName = map.getStr("First Name");
			int worth = map.getInt("Total Worth");
		    } catch(InputXcpt ix) {
			System.out.println(ix.getMessage());
		    }
		}
	    });
	add(submit);
	pack();
	setVisible(true);	

    }
    

}
