import javax.swing.*;
import java.awt.*;

public class Form extends JFrame {

    public static Color bkgd = new Color(218, 218, 218);

    DataMap map = new DataMap();
    SQLBot bot;

    public Form(String name, String dbFile) throws Exception{
	super(name);
	bot = new SQLBot(dbFile);
	
	getContentPane().setBackground(bkgd);

	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//will actually want DISPOSE_ON_CLOSE, i believe
    }
    
    public void addF(Field f) {
	map.put(f);
	f.attach(this);
    }

    public String noWhite(String a){
	return a.trim().replaceAll("\\s", "_");
    }

}
