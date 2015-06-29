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
	addF(new TextField("Vehicle"));
	addF(new TextField("VIN"));
	addF(new TextField("Cost"));

	JButton submit = new JButton("Submit");
	submit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
		    try {

			Key key = new Key(
			    new String[]{"Date Bought", "Vehicle", "Item_ID", "Item_Cost"},
			    new Kind[]{DATE, STRING, STRING, FLOAT});

//			View sqview = new View(key){

//			    }

			Grid g = new Grid(key, new StringIn(CarForm.this));

			View v = g.addView(null, new Cut[]{new IntCut("Title")}, new Enterer(){
				public Object[] editEntry(Object[] objs){
				    return new Object[] {0};
				}
			    });
			v.addOut(new SQLOut(key, "Cars"));
			g.pull();
			g.push();
// bot.insertInit("Cars");

			// String s, x;

			// s = "Date Bought";
			// LocalDate ld = map.getDate(s);
			// bot.insertAdd(noWhite(s), bot.toSQL(ld.toString()));

			// s = "Item ID";
			// x = map.getStr("VIN");
			// bot.insertAdd(noWhite(s), bot.toSQL(x));

			// s = "Vehicle";
			// x = map.getStr(s);
			// bot.insertAdd(noWhite(s), bot.toSQL(x));

			// s = "Item Cost";
			// float f = map.getFloat("Cost");
			// bot.insertAdd(noWhite(s), bot.toSQL(f));

			// bot.insertAdd("Title", bot.toSQL(0));

			// bot.insertSend();

			// bot.printSet(bot.query("SELECT * FROM Cars"));
			
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
