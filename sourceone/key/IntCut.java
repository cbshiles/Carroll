package sourceone.key;

/**
Cut subclass for integers. Pulls in an integer, pushes one out.
 */

public class IntCut extends Cut{

    //# maybe remove
    public IntCut(String n)
    {super(n);}
    
    public IntCut(String n, String c){
	this(n);
	constraints = c;
    }

    public Object in() throws InputXcpt
    {return ip.getInt();}

    public void out(Object o)
    {op.put((int)o);}
}
