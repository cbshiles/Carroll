import javax.swing.JFrame;
import java.awt.event.*;
import java.awt.Color;

public class Page extends JFrame {

    public static final Color bkgd = new Color(190, 190, 240);
    public static SQLBot bot = Driver.bot;

    public Page(String name) {
	super(name);
	Driver.addPage();
	
	addWindowListener(new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent we) {
		    Driver.removePage();
		}
	    });

	getContentPane().setBackground(bkgd);
    }
}
