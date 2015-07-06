package sourceone.key;

/**
Abstract Output that takes Objects and turns them into formatted Strings.
Sends these Strings to a {@link StringDest}
convert() methods for each {@link Kind} are need for an instantiatiable subclass.
 */

public abstract class Formatter implements Output{

    StringDest sd;
    
    public Formatter(StringDest s){
	sd = s;
    }

    public void put(String x){
	sd.put(convert(x));
    }
    
    public void put(int x){
	sd.put(convert(x));
    }
    
    public void put(float x){
	sd.put(convert(x));
    }
    
    public void put(java.time.LocalDate x){
	sd.put(convert(x));
    }

    public void endEntry(){sd.endEntry();}

    public Object close(){return sd.close();}

    public abstract String convert(String x);
    
    public abstract String convert(int x);
    
    public abstract String convert(float x);

    public abstract String convert(java.time.LocalDate x);
}
