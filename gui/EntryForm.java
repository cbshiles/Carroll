import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class EntryForm extends JFrame {

    DataMap map = new DataMap();
    
    public EntryForm() {
	super("Entry Form");
	setSize(400, 600);
	setLocation(200, 100);

	setLayout(new GridLayout(0, 1));

	JPanel textBoxPanel = new JPanel();
	textBoxPanel.setLayout(new GridLayout(0, 2));
	String[] fields = new String[] {"First Name", "Last Name", "Address", "Phone Number", "Total Worth"};
	for (String s : fields)
	    map.put(new TextField(s, textBoxPanel));
	map.put(new OptionalText("Balloon payment?", textBoxPanel, "0"));
	textBoxPanel.setBackground(Color.BLUE);

//	JButton cancel = new JButton("Cancel");
	JButton submit = new JButton("Submit");
//	cancel.setMargin(new Insets(50, 25, 50, 25));
//	cancel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	submit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
		    try {
		    String fName = map.getStr("First Name");
		    int worth = map.getInt("Total Worth");
		    } catch(InputXcpt ix) {
			System.out.println(ix.getMessage());
		    }
		}
	    });

//	submit.addActionListener(new SubmitAction());
//	textBoxPanel.add(cancel);
	textBoxPanel.add(submit);

	add(textBoxPanel);

	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//will actually want DISPOSE_ON_CLOSE, i believe
	
	setVisible(true);	

    }
    

}
