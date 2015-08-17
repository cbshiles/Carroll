package sourceone.fields;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import sourceone.key.InputXcpt;
import java.util.Enumeration;

public class RadioField extends Field{

    ButtonGroup group = new ButtonGroup();

    public RadioField (String n, String[] butts, int s){
	super(n);
	init();	
	addButtons(butts);
	select(s);
    }

    public RadioField (String n, String[] front, String[] back, int s){
	super(n);
	init();
	addButtons(front, back);
	select(s);
    }

    public void select(int n){
	AbstractButton ab;
	Enumeration<AbstractButton> butts = group.getElements();
	for (int i=0; i<=n && butts.hasMoreElements(); i++){
	    ab = butts.nextElement();
	    if (i == n) ab.setSelected(true);
	}
	    
    }

    public void clear(){group.clearSelection();}
    
    public void init(){
	JLabel jl = newLabel(name);
	jl.setPreferredSize(new Dimension(200, 30));
	jp.add(jl);

    }

    private void addButtons(String[] butts){
	int m = butts.length;
	for (int i=0; i<m; i++){
	    addButton(butts[i], butts[i]);
	}
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
