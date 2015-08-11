package sourceone.key;

import java.time.*;
import java.time.format.*;

public class ReserveFormatter extends Formatter {

    public ReserveFormatter(StringDest s){
	super(s);
    }

    public String convert(String x){
	return x;
    }
    
    public String convert(int x){
	return ""+x;
    }
    
    public String convert(float x){
	if (Math.abs(x) < 0.01) return "";
	else return String.format("%.02f", x);
    }

    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");

    public String convert(LocalDate x){
	if (x == null) return "";
	else return x.format(dtf);
    }
}
