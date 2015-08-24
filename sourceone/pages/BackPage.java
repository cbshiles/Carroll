package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import static sourceone.key.Kind.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public abstract class BackPage extends TablePage{
    boolean full, ded=false;
    String sel;
    Key k, viewKey, custKey, contKey;


    public void reload() throws InputXcpt{
	String z = full?">":"<";
	Input in;
	try {
	    in = new QueryIn(custKey, contKey, "WHERE Contracts.Next_Due IS NOT NULL AND Contracts.Customer_ID = Customers.ID AND Contracts.Total_Contract "+z+" 0.01;");
	} catch (Exception e) {throw new InputXcpt(e);}
	k  = custKey.add(contKey.cuts);
	g = new Grid(k, in);
	g.pull();
    }
    
    public BackPage(String title, Page p){
	super(title, p);
	new BackDialog();
    }

    protected abstract void init();
    
    public static LocalDate next(LocalDate du, int freq, LocalDate st){
	LocalDate due;
	if (freq == 30) {
	    due = du.plusMonths(1);
	    int len = due.getMonth().length(Year.isLeap(due.getYear()));
	    int sday = st.getDayOfMonth();
	    int dday = due.getDayOfMonth();
	    if (dday < sday && len > dday){
		due = due.plusDays( (len<sday?len:sday) - dday);
	    }
	}
	else due = du.plusDays(freq);
	return due;
    }

    protected abstract void getTable();
    
    protected class BackDialog extends JDialog implements ActionListener{

	boolean usedMyButtons = false;
	
	public BackDialog(){
	    super(BackPage.this, "Choose report type",Dialog.ModalityType.APPLICATION_MODAL);
	    JButton ab = new JButton("Full Contracts");
	    ab.setActionCommand("full");
	    ab.addActionListener(this);
	
	    JButton bb = new JButton("Partial Contracts");
	    bb.setActionCommand("partial");
	    bb.addActionListener(this);

	    JButton cb = new JButton("Cancel");
	    cb.setActionCommand("exit");
	    cb.addActionListener(this);

	    Container c = getContentPane();
	    JPanel jp = new JPanel();
	    c.add(jp);

	    jp.add(ab); jp.add(bb); jp.add(cb);

	    place(.6f, .25f, .3f, .15f);

	    addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent we) {
			if (! usedMyButtons)
			    ded = true;
			else init();
		    }
		});
		
	    setVisible(true);
	}

	protected void kill(){
	    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	public void place(float x, float y, float w, float h){
	    setBounds((int)(x*dim.width+dim.x), (int)(y*dim.height+dim.y), (int)(w*dim.width), (int)(h*dim.height));
	}

        @Override
	public void actionPerformed(ActionEvent ae) {
	    usedMyButtons = true;
	    String cmd = ae.getActionCommand();
	    if (cmd.equals("exit")) {ded = true;}
	    else {
		full = cmd.equals("full");
		sel = full?"Full":"Partial";
	    }
	    kill();
	}
    }
 
}
