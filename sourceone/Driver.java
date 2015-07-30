package sourceone;

import sourceone.sql.SQLBot;
import sourceone.pages.*;

public class Driver {
    private static int pages = 0;
    public static SQLBot bot;


    
    public static void addPage() {pages++;}
    
    public static void removePage() {
	if (--pages == 0)
	    System.exit(0);
    }
    
    public static void main(String[] args) throws Exception{
	SQLBot.bot = new SQLBot("res/db.properties");

	Page.dim = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
	System.out.println(Page.dim);

	new HomePage();
	EventDispatchThreadHangMonitor.initMonitoring();
    }
}
