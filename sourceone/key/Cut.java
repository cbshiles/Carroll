package sourceone.key;

public abstract class Cut{
    //cannot store the data: are being reused through references

    public String name, sqlName;
    public Input ip;
    public Output op;
    
    public Cut(String n)
    {name = n;
    sqlName = n.trim().replaceAll("\\s", "_");}

    public abstract Object in() throws InputXcpt;

    public abstract void out(Object o);
}
