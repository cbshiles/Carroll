public class StrDatum extends Datum<String> {

    public StrDatum(String n, String v)
    {super(n,v);}
    
    String toSQL()
    {return "'"+val+"'";}
    
}
