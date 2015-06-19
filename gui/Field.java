import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

//Class representing graphical component returning a string

public abstract class Field extends JPanel {

    String name;
    JPanel jp = new JPanel();
    
    public Field (String n)
	{
	    name = n;
	    jp.setOpaque(false);
	    jp.setBorder(LineBorder.createBlackLineBorder());
	}

    public void attach(Container c)
    {c.add(jp);}

    public abstract String text() throws InputXcpt;
}
