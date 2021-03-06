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

    private java.text.DecimalFormat myFormatter = new java.text.DecimalFormat("#0.00");
    private String frm(float ff) {return myFormatter.format(ff);}
    
    public String convert(float x){
	return frm(x);
    }

    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");
    static final DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("MM_dd_yy");
    
    public static String cinvert(LocalDate x){ //# make private
	if (x == null) return "N/A";
	else return x.format(dtf);
    }

    public static String finvert(LocalDate x){ //# make private
	if (x == null) return "N/A";
	else return x.format(dtf3);
    }
    
    public static String cxnvert(LocalDate x){ //# make private
	if (x == null) return "N/A";
	else return x.format(dtf2);
    }
    
    public static String today(){return cinvert(LocalDate.now());}
    
    public String convert(LocalDate x){
	if (x == null) return "N/A";
	else return x.format(dtf);
    }
}
