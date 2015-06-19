import javax.swing.*;
import java.awt.*;

public class Mandatory extends Field{

    JTextField tf = new JTextField();
    boolean mand;

    public Mandatory (String n, Container c, boolean m) {
	super(n, c);
	mand = m;

	c.add(new JLabel(name, SwingConstants.CENTER));

	if (mand) tf.setBackground(new Color(255, 215, 215));
	c.add(tf);
    }

    public Mandatory (String n, Container c) {
	this(n, c, false);
    }

    
    public String text() throws InputXcpt
    {
	String s = tf.getText();
	if (s.equals("") && mand)
	    throw new InputXcpt(name, "Empty mandatory field");
	return s;
    }
}
