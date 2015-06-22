import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class FloorForm extends Form{
    public FloorForm() throws Exception{
	super("Cars", "../../db.properties");

	setSize(400, 600);
	setLocation(200, 100);

	setLayout(new GridLayout(0, 1));

	addF(new TextField("Date bought"));
	addF(new TextField("Item ID"));
	addF(new TextField("Vehicle"));
	addF(new TextField("Item Cost"));

	JButton submit = new JButton("Submit");
	submit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
		    try {
			bot.insertInit("Cars");

			String s, x;

			s = "Date bought";
			LocalDate ld = map.getDate(s);
			bot.insertAdd(noWhite(s), bot.toSQL(ld.toString()));

			s = "Item ID";
			x = map.getStr(s);
			bot.insertAdd(noWhite(s), bot.toSQL(x));

			s = "Vehicle";
			x = map.getStr(s);
			bot.insertAdd(noWhite(s), bot.toSQL(x));

			s = "Item Cost";
			float f = map.getFloat(s);
			bot.insertAdd(noWhite(s), bot.toSQL(f));

			bot.insertAdd("Title", bot.toSQL(0));

			bot.insertSend();

			bot.printSet(bot.query("SELECT * FROM Cars"));
			
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
