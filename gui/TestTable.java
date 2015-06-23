import javax.swing.*;
import java.awt.*;

public class TestTable {

    public static void pain(String[] args){
	ATable at = new ATable();

	JFrame jf = new JFrame("Testooooo");
	JPanel jp = new JPanel();
	jp.add(new JScrollPane(at.getTable()));
	jf.setContentPane(jp);
	jf.pack();
	jf.setVisible(true);
    }

    public static void main(String[] args){
	SQLBot bot;
	try {
	    bot = new SQLBot("../../db.properties");

	    Column[] cols = new Column[] {
		new Column("ID", 1),
		new Column("Purchase Date", 3),
		new Column("Vehicle", 0),
		new Column("VIN", 0),
		new Column("Cost", 2)};

	    Table tbl = new Table("Cars", cols, bot);

	    JFrame jf = new JFrame("Testooooo");
	    JPanel jp = new JPanel();

	    JTable jt = new JTable(tbl.d2Converter(tbl.readDB()), tbl.getHeads());
	    jp.add(new JScrollPane(jt));
	    jf.setContentPane(jp);
	    jf.pack();
	    jf.setVisible(true);
	}
	catch (Exception e)
	{System.err.println("YO: "+e.getMessage());}
    }
}
