package sourceone.tables;

import sourceone.*;
import sourceone.fields.*;
import java.time.*;
import java.time.format.*;

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
	if (type == 4) //Literal //meh, have boolean, orthagonal

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
    public String getStr(String raw){
	return raw;
    }

    public int getInt(String raw)throws InputXcpt{
	try {return Integer.parseInt(raw);}
	catch (NumberFormatException e) {throw new InputXcpt(raw, e.getMessage()+"\nCouldn't parse as int");}
    }

    public int getInt(String raw, int l, int h)throws InputXcpt{
	int x = getInt(raw);
	if (x < l || x > h) throw new InputXcpt(raw, x+" is not between "+l+" and "+h);
	return x;
    }

    public float getFloat(String raw)throws InputXcpt{
	try {return Float.parseFloat(raw);}
	catch (NumberFormatException e) {throw new InputXcpt(raw, e.getMessage()+"\nCouldn't parse as float");}
    }

    public float getFloat(String raw, int l, int h)throws InputXcpt{
	float x = getFloat(raw);
	if (x < l || x > h) throw new InputXcpt(raw, x+" is not between "+l+" and "+h);
	return x;
    }

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yy");

    public LocalDate getDate(String raw) throws InputXcpt{
	String dStr = raw;
	dStr = dStr.replaceAll("/", "-");
	try {
	return LocalDate.parse(dStr, dtf);
	} catch (DateTimeParseException e){
	    throw new InputXcpt(raw, dStr, "Could not be parsed in MM-dd-yy or MM/dd/yy format");
	}
    }

}
