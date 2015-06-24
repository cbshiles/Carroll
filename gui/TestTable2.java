import javax.swing.*;
import java.awt.*;

public class TestTable2 {

    public static void run(JTable jt){
	JFrame jf = new JFrame("Testooooo");
	jf.setSize(1000, 600);
	JPanel jp = new JPanel();

//	jp.add(new JScrollPane(jt));
//	jf.setContentPane(jp);
	jf.setContentPane(new JScrollPane(jt));
	//jf.pack();
	jf.setVisible(true);
    }

    public static void main(String[] args){
	try{
	    SQLBot bot;
	    bot = new SQLBot("../../db.properties");
	    //JTable x = new ContractTable(bot).makePayTable();
	    JTable x = new FloorTable(bot).makeFloorTable();
	    run(x);
	} catch (Exception e)
	{System.err.println("YO: "+e.getCause()+e.getClass().getName());}

    }
}
