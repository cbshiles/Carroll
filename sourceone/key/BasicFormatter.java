package sourceone.key;

import java.time.*;
import java.time.format.*;

public class BasicFormatter extends Formatter {

    public BasicFormatter(StringDest s){
	super(s);
    }

    public String convert(String x){
	return x;
    }
    
    public String convert(int x){
	return ""+x;
    }
    
    public String convert(float x){
	return String.format("%.02f", x);
    }

    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");

    public static String cinvert(LocalDate x){
	if (x == null) return "N/A";
	else return x.format(dtf);
    }
    
    public String convert(LocalDate x){
	if (x == null) return "N/A";
	else return x.format(dtf);
    }
}
