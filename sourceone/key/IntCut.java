package sourceone.key;

/**
Cut subclass for integers. Pulls in an integer, pushes one out.
 */

public class IntCut extends Cut{

    //# maybe remove
    public IntCut(String n)
    {this(n, "");}
    
    public IntCut(String n, String c){
	super(n);
	fullSQL = sqlName+" int "+c;
    }

    public Object in() throws InputXcpt
    {return ip.getInt();}

    public void out(Object o)
    {op.put((int)o);}
}
