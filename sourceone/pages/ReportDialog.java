package sourceone.pages;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

public class ReportDialog extends JDialog implements ActionListener{
    Class p;
    public ReportDialog(Frame f, Class p){
	super(f, "Choose report type",Dialog.ModalityType.DOCUMENT_MODAL);
	this.p = p;
	JButton ab = new JButton("Full Contracts");
	ab.setActionCommand("full");
	ab.addActionListener(this);
	
	JButton bb = new JButton("Partial Contracts");
    	bb.setActionCommand("partial");
	bb.addActionListener(this);

	Container c = getContentPane();
	JPanel jp = new JPanel();
	c.add(jp);

	jp.add(ab); jp.add(bb);

	setBounds(500,500,500,150);

	setVisible(true);
    }

        @Override
    public void actionPerformed(ActionEvent ae) {
	    String cmd = ae.getActionCommand();
	    try {p.getDeclaredConstructor(Boolean.class).newInstance(cmd.equals("full"));}
	    catch (Exception e){System.err.println(e);}
	    dispose();
	}
}
