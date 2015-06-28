package sourceone.key;
public class StringCut extends Cut{

    public StringCut(String n)
    {super(n);}
    
    public Object in() throws InputXcpt
    {return ip.getString();}

    public void out(Object o)
    {op.put(o.toString());}
}
