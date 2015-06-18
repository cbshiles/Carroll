import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StrField {

    String name, value;
    Container ctnr;

    public StrField (String n, Container c) {
	name = n; ctnr = c;
	c.add(new JLabel(name));
	c.add(new JTextField());
    }
}
