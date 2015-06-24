import java.awt.event.*;

public class Relocator <T extends Page> implements ActionListener{

    private Class<T> type;
    
    public Relocator(Class<T> t) {type = t;}

    public void actionPerformed(ActionEvent ae)
    {
	try {type.newInstance();}
	catch (Exception ie) {System.err.println("InstantiationException");}
    }
}
