import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RadioField extends Field implements ActionListener{

    ButtonGroup group = new ButtonGroup();
    private String rtVal = "";

    public RadioField (String n, String[] butts){
	super(n);
	
	for (String s : butts)
	    addButton(s);
    }

    public void addButton(String n){
	JRadioButton jrb = new JRadioButton(n);
	jrb.setActionCommand(n);
	//jrb.setSelected(true);
	group.add(jrb);
	jrb.addActionListener(this);
	jp.add(jrb);
    }

    public void actionPerformed(ActionEvent e) {
	rtVal = e.getActionCommand();
    }

        public String text() throws InputXcpt
    {
	if (rtVal.equals(""))
	    throw new InputXcpt(name, "Must make a selection");
	else return rtVal;
    }
}
