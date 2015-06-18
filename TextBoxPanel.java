import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TextBoxPanel extends JPanel {

    DataMap daters;
    
    public TextBoxPanel() {

	daters = new DataMap();

	setLayout(new GridLayout(0, 2));

	String[] fields = new String[] {"First Name", "Last Name", "Address", "Phone Number", "Total Worth"};

	for (String s : fields){
	    daters.put(new TextField(s, this));
	}
	setBackground(Color.BLUE);
    }

}
