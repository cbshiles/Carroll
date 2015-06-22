import javax.swing.*;
import java.awt.*;

public class TestTable {

    public static void main(String[] args){
	ATable at = new ATable();

	JFrame jf = new JFrame("Testooooo");
	jf.setContentPane(new JScrollPane(at.getTable()));
	jf.add(new JLabel("sds"));
	jf.pack();
	jf.setVisible(true);
    }
}
