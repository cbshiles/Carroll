public class Driver {
    private static int pages = 0;

    public static void addPage() {pages++;}
    
    public static void removePage() {
	if (--pages == 0)
	    System.exit(0);
    }
    
    public static void main(String[] args){
	new Home();
    }
}
