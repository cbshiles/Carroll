import java.util.HashMap;

public class DataMap {

    private HashMap<String, TextField> map;

    public DataMap(){
	map = new HashMap<String, TextField>();
    }

    public void put(TextField tf){
	map.put(tf.name, tf);
    }

    public String getStr(String key) throws AccessException{
	TextField tf = map.get(key);
	if (tf == null) throw new AccessException("Key: "+key+" did not have an entry in the map.");
	return tf.text();
    }

    public int getInt(String key)throws AccessException{
	String intStr = getStr(key);
	try {return Integer.parseInt(intStr);}
	catch (NumberFormatException e) {throw new AccessException("Couldn't parse "+intStr+"\n"+e.getMessage());}
    }

    public int getInt(String key, int l, int h)throws AccessException{
	int x = getInt(key);
	if (x < l || x > h) throw new AccessException(x+" is not between "+l+" and "+h);
	return x;
    }

    public float getFloat(String key)throws AccessException{
	String intStr = getStr(key);
	try {return Float.parseFloat(intStr);}
	catch (NumberFormatException e) {throw new AccessException("Couldn't parse "+intStr+"\n"+e.getMessage());}
    }

    public float getFloat(String key, int l, int h)throws AccessException{
	float x = getFloat(key);
	if (x < l || x > h) throw new AccessException(x+" is not between "+l+" and "+h);
	return x;
    }

}
