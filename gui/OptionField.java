import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OptionField extends Field{

    JTextField tf = newText();
    JCheckBox cb;
    boolean textOn;
    String empty;
    
    public OptionField (String n, String e, boolean to) {
	super(n);
	empty = e;
	textOn = to;

	jp.setLayout(new GridLayout(0, 2));
	
	jp.add(cb = new JCheckBox(name, to));
	cb.setOpaque(false);
	cb.setHorizontalAlignment(SwingConstants.CENTER);
	
	cb.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
		    tf.setEnabled(textOn = cb.isSelected());
		    tf.setText("");
		}
	    });

	tf.setEnabled(to);	
	jp.add(tf);
	
    }

    public String text()
    {return textOn?tf.getText():empty;}
}
