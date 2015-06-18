import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OptionalText extends Field{

    JTextField tf = new JTextField();
    JCheckBox cb;
    boolean textOn = false;
    String empty;
    
    public OptionalText (String n, Container c, String e) {
	super(n, c);
	empty = e;

	c.add(cb = new JCheckBox(name));
	cb.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
		    tf.setEnabled(textOn = cb.isSelected());
		    tf.setText("");
		}
	    });

	tf.setEnabled(false);	
	c.add(tf);
	
    }

    public String text()
    {return textOn?tf.getText():empty;}
}
