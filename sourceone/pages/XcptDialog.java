package sourceone.pages;

import sourceone.pages.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

public class XcptDialog extends JDialog implements ActionListener{

    Exception cept;
    
    public XcptDialog(Window f, Exception ex){
	super(f, "ERROR", Dialog.ModalityType.DOCUMENT_MODAL);
	cept = ex;
	setBounds(500,500,500,150);


	Container c = getContentPane();
	JPanel jp = new JPanel();
	c.add(jp);
	JTextArea jta;
	jp.add(jta = new JTextArea(""+cept, 5, 20));
	jta.setEditable(false);
	jta.setLineWrap(true);
	jta.setWrapStyleWord(true);
	
	JButton cb = new JButton("Ok");
	cb.addActionListener(this);
	jp.add(cb);
	setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
	dispose();
    }

}
