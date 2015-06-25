import java.util.HashMap;
import java.time.*;
import java.time.format.*;

public class DataMap {

    private HashMap<String, Field> map;

    public DataMap(){
	map = new HashMap<String, Field>();
    }

    public void put(Field tf){
	map.put(tf.name, tf);
    }

    public String getStr(String key) throws InputXcpt{
	Field tf = map.get(key);
	if (tf == null) throw new InputXcpt(key, "Field not found");
	return tf.text();
    }

    public int getInt(String key)throws InputXcpt{
	String nStr = getStr(key);
	try {return Integer.parseInt(nStr);}
	catch (NumberFormatException e) {throw new InputXcpt(key, e.getMessage()+"\nCouldn't parse as int");}
    }

    public int getInt(String key, int l, int h)throws InputXcpt{
	int x = getInt(key);
	if (x < l || x > h) throw new InputXcpt(key, x+" is not between "+l+" and "+h);
	return x;
    }

    public float getFloat(String key)throws InputXcpt{
	String nStr = getStr(key);
	try {return Float.parseFloat(nStr);}
	catch (NumberFormatException e) {throw new InputXcpt(key, e.getMessage()+"\nCouldn't parse as float");}
    }

    public float getFloat(String key, int l, int h)throws InputXcpt{
	float x = getFloat(key);
	if (x < l || x > h) throw new InputXcpt(key, x+" is not between "+l+" and "+h);
	return x;
    }

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yy");

    public LocalDate getDate(String key) throws InputXcpt{
	String dStr = getStr(key);
	dStr = dStr.replaceAll("/", "-");
	try {
	return LocalDate.parse(dStr, dtf);
	} catch (DateTimeParseException e){
	    throw new InputXcpt(key, dStr, "Could not be parsed in MM-dd-yy or MM/dd/yy format");
	}
    }
}
