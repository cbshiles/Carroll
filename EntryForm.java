import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EntryForm extends JFrame {
    
    public EntryForm() {
	super("Entry Form");
	setSize(400, 600);
	setLocation(200, 100);

	setLayout(new GridLayout(0, 1));

//		System.out.println("THIS: d");
	add(new TextBoxPanel());
	add(new JLabel("berrerer"));
	setVisible(true);
		add(new JLabel("cerrerer"));
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//will actually want DISPOSE_ON_CLOSE, i believe

    }
    

}
