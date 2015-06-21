import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

//Class representing graphical component returning a string

public abstract class Field {

    String name;
    JPanel jp;
    
    public Field (String n, JPanel j)
	{
	    name = n;
	    jp = j;
	    jp.setOpaque(false);
	    jp.setBorder(LineBorder.createBlackLineBorder());
	}

    public Field (String n)
    {this(n, new JPanel());}

    public void attach(Container c)
    {c.add(jp);}

    public abstract String text() throws InputXcpt;

    JLabel newLabel(String n){
	return new JLabel(n, SwingConstants.CENTER);
    }

    JTextField newText(){
	JTextField tf = new JTextField();
	tf.setBorder(BorderFactory.createMatteBorder(15, 5, 15, 5, EntryForm.bkgd));
	return tf;
    }
}
