package sourceone.fields;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import sourceone.key.InputXcpt;
import java.util.Enumeration;

public class RadioField extends Field{

    ButtonGroup group = new ButtonGroup();

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

    public void clear(){group.clearSelection();}
    
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

	group.add(jrb);
	jp.add(jrb);
    }
    
    public String text() throws InputXcpt
    {
	ButtonModel bm = group.getSelection();
	if (bm == null) throw new InputXcpt(name, "Must make a selection");
	return bm.getActionCommand();
    }
}
