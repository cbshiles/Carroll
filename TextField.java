import javax.swing.*;
import java.awt.*;
//import java.awt.event.*;

public class TextField extends JPanel{

    String name, value;
    Container ctnr;
    JTextField tf = new JTextField();

    public TextField (String n, Container c) {
	name = n; ctnr = c;
	c.add(new JLabel(name));
	c.add(tf);
    }

    public String text()
    {return tf.getText();}
}
