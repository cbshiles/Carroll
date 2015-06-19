import javax.swing.*;
import java.awt.*;

public class TextField extends Field{

    JTextField tf = new JTextField();

    public TextField (String n, Container c){
	super(n, c);

	c.add(new JLabel(name, SwingConstants.CENTER));
	c.add(tf);
    }
    
    public String text() throws InputXcpt
    {
	return tf.getText();
    }
}
