package sourceone.pages;

import static sourceone.key.Kind.*;
import sourceone.key.*;
import sourceone.fields.*;
import sourceone.sql.SQLOut;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.*;

public class CarForm extends Form{
    public CarForm() throws Exception{
	super("Cars");

	setSize(400, 600);
	setLocation(200, 100);

	setLayout(new GridLayout(0, 1));

	addF(new TextField("Date Bought"));
	addF(new TextField("VIN"));
	addF(new TextField("Vehicle"));
	addF(new TextField("Cost"));

	JButton submit = new JButton("Submit");
	submit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
		    try {
			CarForm.this.refresh();
			Key key = Key.floorKey.except(new int[]{0,5,6});

			Grid g = new Grid(key, new StringIn(CarForm.this));

			View v = g.addView(null, new Cut[]{new IntCut("Title")}, new Enterer(){
				public Object[] editEntry(Object[] objs){
				    return new Object[] {0};
				}
			    });
			v.addOut(new SQLOut(v.key, "Cars"));
			g.pull();
			g.push();
		    } catch (Exception ix){//(InputXcpt ix) {
			System.out.println(ix.getMessage());
		    }
		}
	    });

	add(submit);
	pack();
	setVisible(true);
    }
}
