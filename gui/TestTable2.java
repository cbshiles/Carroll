import javax.swing.*;
import java.awt.*;

public class TestTable2 {

    public static void main(String[] args){
	SQLBot bot;
	try {
	    bot = new SQLBot("../../db.properties");

	    ContractTable tbl = new ContractTable(bot);

	    JFrame jf = new JFrame("Testooooo");
	    JPanel jp = new JPanel();

//	    JTable jt = new JTable(tbl.d2Converter(tbl.readDB()), tbl.getHeads());
	    JTable jt = tbl.makePayTable();
	    jp.add(new JScrollPane(jt));
	    jf.setContentPane(jp);
	    jf.pack();
	    jf.setVisible(true);
	}
	catch (Exception e)
	{System.err.println("YO: "+e.getMessage());}
    }
}
