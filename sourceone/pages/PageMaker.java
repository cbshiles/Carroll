package sourceone.pages;
import java.awt.event.*;

public class PageMaker implements ActionListener{

    private Class type;
    
    public <T extends Page> PageMaker(Class<T> t) {type = t;}

    public void actionPerformed(ActionEvent ae)
    {
	try {type.newInstance();}
	catch (Exception ie) {System.err.println("InstantiationException");}
    }
}
