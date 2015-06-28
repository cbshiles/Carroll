package sourceone.key;
public class InputXcpt extends Exception {

    static String z = "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
    
    public InputXcpt(String k, String msg){
	super(z+"Input Error for string "+k+"\n"+msg+z);
    }

    //InputXcpt that takes other exceptions (in constructor)?

    // public InputXcpt(String k, String val, String msg){
    // 	this(k, "For input string: \""+val+"\"\n"+msg);
    // }

    public InputXcpt(String msg){
	super(msg+"\n");
    }

    public InputXcpt(Exception e){
	super(z+e.getClass().getName()+'\n'+e.getMessage()+z);
    }
}
