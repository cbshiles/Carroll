public class IntDatum extends Datum<Integer> {

    public IntDatum(String n, Integer v)
    {super(n,v);}
    
    String toSQL()
    {return val.toString();}
    
}
