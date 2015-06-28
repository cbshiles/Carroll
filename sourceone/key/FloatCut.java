package sourceone.key;
public class FloatCut extends Cut{

    public FloatCut(String n)
    {super(n);}
    
    public Object in() throws InputXcpt
    {return ip.getFloat();}

    public void out(Object o)
    {op.put((float)o);}
}
