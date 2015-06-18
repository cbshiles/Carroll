public abstract class Datum <T> {

    public String name;
    public T val;

    public Datum(String n, T v) {
	name = n;
	val = v;
    }

    abstract T fromString(String s);

    abstract String toSQL();

    public T getVal()
    { return val; }

    public void setVal(T t)
    { val = t; }

    public boolean isNull()
    {return val == null;}
}
