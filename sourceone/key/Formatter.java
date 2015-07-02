package sourceone.key;

import java.time.*;
import java.time.format.*;


public class Formatter implements Output {

    StringDest sd;
    
    public Formatter(StringDest s){
	sd = s;
    }

    public void put(String x){
	sd.put(x);
    }
    
    public void put(int x){
	sd.put(""+x);
    }
    
    public void put(float x){
	sd.put(String.format("%.02f", x));
    }

    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    
    public void put(java.time.LocalDate x){
	if (x == null) sd.put("N/A");
	else sd.put(x.format(dtf));
    }

    public void endEntry(){sd.endEntry();}

    public void close(){sd.close();}
}
