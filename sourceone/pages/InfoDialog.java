package sourceone.pages;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class InfoDialog extends JDialog implements ActionListener{

    public InfoDialog(Window f, String title, String body){
	super(f, title,  Dialog.ModalityType.APPLICATION_MODAL);

	setBounds(500,500,500,350);

	Container c = getContentPane();
	JPanel jp = new JPanel();
	c.add(jp);
	JTextArea jta;
	jp.add(jta = new JTextArea(body, 5, 20));
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
