package sourceone.fields;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OptionField extends Field{

    JTextField tf = newText();
    JCheckBox cb;
    boolean init, textOn;
    String empty;
    
    public OptionField (String n, String e, boolean to) {
	super(n);
	empty = e;
	init = textOn = to;

	jp.setLayout(new GridLayout(0, 2));
	
	jp.add(cb = new JCheckBox(name, to));
	cb.setOpaque(false);
	cb.setHorizontalAlignment(SwingConstants.CENTER);
	
	cb.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
		    zub(textOn = cb.isSelected());
		}
	    });

	tf.setEnabled(to);	
	jp.add(tf);
	
    }

    public void clear(){
	cb.setSelected(init);
	zub(init);
    }

    private void zub(boolean z){
	tf.setEnabled(z);
	tf.setText("");
    }

    public String text()
    {return textOn?tf.getText():empty;}
}
