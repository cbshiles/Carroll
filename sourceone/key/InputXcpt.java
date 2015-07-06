package sourceone.key;
public class InputXcpt extends Exception {

    static String z = "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";

    public InputXcpt(String msg){
	super(z+msg+"\n"+z);
    }
    
    public InputXcpt(String k, String msg){
	this("Input Error for "+k+"\n"+msg);
    }

    public InputXcpt(Exception e){
	super(e);
    }

    public InputXcpt(String msg, Exception e){
	super(msg, e);
    }
}
