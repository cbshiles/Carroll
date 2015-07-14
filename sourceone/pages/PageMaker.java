package sourceone.pages;
import java.awt.event.*;
import java.awt.*;

public class PageMaker implements ActionListener{

    private Class type;
    private Frame owner;
    
    public <T extends Page> PageMaker(Frame f, Class<T> t)
    {owner = f; type = t;}

    public void actionPerformed(ActionEvent ae)
    {
	try {type.newInstance();}
	catch (Exception ie) {new XcptDialog(owner, ie);}
    }
}
