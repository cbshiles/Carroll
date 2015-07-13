package sourceone.pages;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

public class ConfirmDialog extends JDialog implements ActionListener{

    boolean chosen = false, ret;
    String date;

    public ConfirmDialog(Frame f, String name,String d){
	super(f, name, Dialog.ModalityType.DOCUMENT_MODAL);
	date = d;
    }

    boolean confirmed(){
	Container c = getContentPane();
	JPanel jp = new JPanel();
	c.add(jp);
	jp.add(new JLabel("This will be overwriting a previous report from "+date+". Is this ok?"));
	JButton ob = new JButton("Overwrite");
	JButton cb = new JButton("Cancel");
	ob.setActionCommand("T");
	cb.setActionCommand("F");
	ob.addActionListener(this);
	cb.addActionListener(this);
	jp.add(ob);
	jp.add(cb);
	setBounds(500,500,500,150);
	
	setVisible(true);

	return ret;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
	String action = ae.getActionCommand();
	ret = action.equals("T")?true:false;
	dispose();
	}
}
