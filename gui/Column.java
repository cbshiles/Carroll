public class Column{

    public String name, sqlName;
    public Type type;
    public String txt;
    boolean literal;
/*
	if (type == 0) //String
	if (type == 1) //Integer
	if (type == 2) //Float
	if (type == 3) //Date
	if (type == 4) //Literal

 */

    public Column(String n, Type t){
	name = n;
	sqlName = name.trim().replaceAll("\\s", "_");
	type = t;
	literal = false;
    }

    public Column(String n, Type t, String z){
	this(n, t);
	txt = z;
	literal = true;
    }
}
