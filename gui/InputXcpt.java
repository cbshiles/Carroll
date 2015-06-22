public class InputXcpt extends Exception {

    static String z = "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
    
    public InputXcpt(String k, String msg){
	super(z+"Input Error for field "+k+"\n"+msg+z);
    }
    public InputXcpt(String k, String val, String msg){
	this(k, "For input string: \""+val+"\"\n"+msg);
    }

    public InputXcpt(String msg){
	super(msg+"\n");
    }


}
