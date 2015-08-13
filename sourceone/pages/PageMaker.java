package sourceone.pages;
import java.awt.event.*;
import java.awt.*;
import sourceone.key.InputXcpt;

public class PageMaker implements ActionListener{

    private Class type;
    private Page owner;
    private boolean locker;

    public <T extends Page> PageMaker(Page f, Class<T> t, boolean lk)
    {owner = f; type = t; locker=lk;}

    public void actionPerformed(ActionEvent ae)
    {
	    try {
		if (owner.isBusy())
		    throw new InputXcpt("Already have a window open.");
		else if (owner.isParent())
		    owner.removeChildren();
		owner.setBusy(locker);
		try {type.getDeclaredConstructor(Page.class).newInstance(owner);}
		catch (java.lang.reflect.InvocationTargetException e) { 
		    throw e.getCause();
		}
	    }
	    catch (Exception ie) {new XcptDialog(owner, ie); ie.printStackTrace();}
	    catch (Throwable t) {System.err.println("What the heck is this?"); t.printStackTrace();}
    }
}
