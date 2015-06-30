package sourceone.key;
public class InputXcpt extends Exception {

    static String z = "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";

    public InputXcpt(String msg){
	super(z+msg+"\n"+z);
    }
    
    public InputXcpt(String k, String msg){
	this("Input Error for "+k+"\n"+msg);
    }

    //InputXcpt that takes other exceptions (in constructor)?

    // public InputXcpt(String k, String val, String msg){
    // 	this(k, "For input string: \""+val+"\"\n"+msg);
    // }


    public InputXcpt(Exception e){
	this(e.getClass().getName()+'\n'+e.getMessage());
    }
}
