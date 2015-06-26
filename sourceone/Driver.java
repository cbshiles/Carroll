//package stink;

public class Driver {
    private static int pages = 0;
    public static SQLBot bot;

    public static void addPage() {pages++;}
    
    public static void removePage() {
	if (--pages == 0)
	    System.exit(0);
    }
    
    public static void main(String[] args) throws Exception{
	bot = new SQLBot("../res/db.properties");
	new HomePage();
	//new CSVTable().dew();
    }
}
