package sourceone.key;
public class DateCut extends Cut{

    public DateCut(String n)
    {super(n);}
    
    public Object in() throws InputXcpt
    {return ip.getDate();}

    public void out(Object o)
    {op.put((java.time.LocalDate)o);}
}
