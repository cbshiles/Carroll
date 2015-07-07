package sourceone.key;

/**
   Cut subclass for Strings. Pulls in an String, pushes one out.
*/

public class StringCut extends Cut{

    public StringCut(String n){super(n);}
    
    public StringCut(int len, String n)
    {this(len, n, "");}
    
    public StringCut(int len, String n, String c){
	super(n);
	fullSQL = sqlName+" varchar("+len+") "+c;
    }
    
    public Object in() throws InputXcpt
    {return ip.getString();}

    public void out(Object o)
    {op.put(o.toString());}
}
