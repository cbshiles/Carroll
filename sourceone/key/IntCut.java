package sourceone.key;
public class IntCut extends Cut{

    public IntCut(String n)
    {super(n);}
    
    public Object in() throws InputXcpt
    {return ip.getInt();}

    public void out(Object o)
    {op.put((int)o);}
}
