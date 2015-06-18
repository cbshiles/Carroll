import javax.swing.*;
import java.awt.*;

//Class representing graphical component returning a string

public abstract class Field extends JPanel {

    String name;
    Container ctnr;
    
    public Field (String n, Container c)
	{name = n; ctnr = c;}

    public abstract String text() throws InputXcpt;
}
