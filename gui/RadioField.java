import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RadioField extends Field implements ActionListener{

    ButtonGroup group = new ButtonGroup();
    private String rtVal = "";

    public RadioField (String n, String[] butts){
	super(n);
	init();	
	addButtons(butts);
    }

    public RadioField (String n, String[] front, String[] back){
	super(n);
	init();
	addButtons(front, back);
    }

    public void init(){
//	jp.setLayout(new GridLayout(0, 4));//new FlowLayout(FlowLayout.CENTER));
	JLabel jl = newLabel(name);
	jl.setPreferredSize(new Dimension(200, 30));
	jp.add(jl);

    }

    private void addButtons(String[] butts){
	for (String s : butts)
	    addButton(s, s);
    }

    private void addButtons(String[] front, String[] back){
	int m = front.length<back.length?front.length:back.length;
	for (int i=0; i<m; i++){
	    addButton(front[i], back[i]);
	}
    }
    
    public void addButton(String f, String b){
	JRadioButton jrb = new JRadioButton(f);
	jrb.setActionCommand(b);
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
