package sourceone.key;

/**
   Cut subclass for dates. Pulls in a float, pushes one out.
*/

public class FloatCut extends Cut{

    public FloatCut(String n)
    {super(n);}
    

    public FloatCut(String n, String c){
	this(n);
	constraints = c;
    }


    public Object in() throws InputXcpt
    {return ip.getFloat();}

    public void out(Object o)
    {op.put((float)o);}
}
