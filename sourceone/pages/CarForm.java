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
import java.sql.*;

public class CarForm extends Form{
    public CarForm(Page p) throws Exception{
	super("Cars", p);

	place(.9f/4, .1f, .9f/4, .2f);
	setLayout(new GridLayout(0, 1));

	addF(new TextField("Date Bought"));
	addF(new TextField("VIN"));
	addF(new TextField("Vehicle"));
	addF(new TextField("Cost"));

	Key key = Key.floorKey.just(new String[]{"Date Bought","VIN", "Vehicle", "Item Cost"});
	Grid g = new Grid(key, new StringIn(CarForm.this));
	View v = g.addView(null, new Cut[]{new IntCut("Title")}, new Enterer(){
		public Object[] editEntry(Object[] objs){
		    try {
		    ResultSet rs = SQLBot.bot.query("SELECT ID FROM Cars WHERE VIN LIKE '"+g.data.get(0)[1]+"';");
		    if (rs.next()) new InfoDialog(CarForm.this, "WARNING", "There is already a car that is or was on the floor with the same VIN.");
		    } catch (Exception e) {System.out.println("JAH "+e);}
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
	setVisible(true);
    }
}
