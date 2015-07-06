package sourceone.key;

/**
   Cut subclass for dates. Pulls in an java.time.LocalDate, pushes one out.
*/

public class DateCut extends Cut{

    public DateCut(String n)
    {super(n);}
    
    public DateCut(String n, String c){
	this(n);
	constraints = c;
    }
    public Object in() throws InputXcpt
    {return ip.getDate();}

    public void out(Object o)
    {op.put((java.time.LocalDate)o);}
}
