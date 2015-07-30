package sourceone.pages;

import static sourceone.key.Kind.*;
import sourceone.key.*;
import sourceone.fields.*;
import sourceone.sql.*;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.*;

public class CarForm extends Form{
    public CarForm(Page p) throws Exception{
	super("Cars", p);

	place(.3f, .1f, .25f, .35f);
	setLayout(new GridLayout(0, 1));

	addF(new TextField("Date Bought"));
	addF(new TextField("VIN"));
	addF(new TextField("Vehicle"));
	addF(new TextField("Cost"));

	Key key = Key.floorKey.just(new String[]{"Date Bought","VIN", "Vehicle", "Item Cost"});
	Grid g = new Grid(key, new StringIn(CarForm.this));
	View v = g.addView(null, new Cut[]{new IntCut("Title")}, new Enterer(){
		public Object[] editEntry(Object[] objs){
		    return new Object[] {0};
		}
	    });
	v.addOut(new SQLFormatter(new InsertDest(v.key, "Cars", true)));
	
	JButton submit = new JButton("Submit");
	submit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
		    CarForm.this.refresh();

		    try {
			if ((int)g.go() == -1)
			    throw new InputXcpt("SQL insertion unsuccessful");
			freshen();
		    } catch (InputXcpt ix){
			new XcptDialog(getName(), CarForm.this, ix);
		    } 
		}
	    });

	add(submit);
	pack();
	setVisible(true);
    }
}
