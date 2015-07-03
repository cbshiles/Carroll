package sourceone.key;
import java.time.*;
import java.time.format.*;

public class StringIn implements Input {

    private StringSource ss;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yy");
    
    public StringIn(StringSource s)
    {ss=s;}

    public boolean hasEntries()
    {return ss.hasEntries();}

    public String getString() throws InputXcpt{
	return ss.get();
    }

    public int getInt() throws InputXcpt{
	String str = ss.get();
	try {return Integer.parseInt(str);}
	catch (NumberFormatException e) 
	{throw new InputXcpt(str, e.getMessage()+"\nCouldn't parse as int");}
    }

    public float getFloat() throws InputXcpt{
	String str = ss.get();
	try {return Float.parseFloat(str);}
	catch (NumberFormatException e)
	{throw new InputXcpt(str, e.getMessage()+"\nCouldn't parse as float");}
    }

    public LocalDate getDate() throws InputXcpt{
	String str = ss.get();
	str = str.replaceAll("/", "-");
	try {
	return LocalDate.parse(str, dtf);
	} catch (DateTimeParseException e){
	    throw new InputXcpt(str, "Could not be parsed in MM-dd-yy or MM/dd/yy format");
	}
    }

        public static LocalDate getDate(String str) throws InputXcpt{
	str = str.replaceAll("/", "-");
	try {
	return LocalDate.parse(str, dtf);
	} catch (DateTimeParseException e){
	    throw new InputXcpt(str, "Could not be parsed in MM-dd-yy or MM/dd/yy format");
	}
    }

}
