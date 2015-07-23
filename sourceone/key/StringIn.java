package sourceone.key;

import java.time.*;
import java.time.format.*;

/**
   Implements {@link Input} by pulling from a {@link StringSource} and parsing Strings into the various Kinds of data.
*/

public class StringIn implements Input {

    private StringSource ss;

    /**
       @param s  The {@link StringSource}, which this instance of StringIn will pull from.
     */
    public StringIn(StringSource s)
    {ss=s;}

    /**
       Check if it's StringSource still has entries.
     */
    public boolean hasEntries()
    {return ss.hasEntries();}

    /**
       @return the String gotten from the StringSource, with no modification.
     */
    public String getString() throws InputXcpt{
	return ss.get();
    }

    /**
       Calls Integer.parseInt
    */
    public static int parseInt(String str) throws InputXcpt{
	try {return Integer.parseInt(str);}
	catch (NumberFormatException e) 
	{throw new InputXcpt(e.getMessage()+"\nCouldn't parse as int");}
    }
    
    /**
       @return integer parsed from the StringSource
    */
    public int getInt() throws InputXcpt
    {return parseInt(ss.get());}

    /**
       Calls Float.parseFloat
    */
    public static float parseFloat(String str) throws InputXcpt{
	try {return Float.parseFloat(str);}
	catch (NumberFormatException e)
	{throw new InputXcpt(e.getMessage()+"\nCouldn't parse as float");}
    }

    /**
       @return float parsed from the StringSource
     */
    public float getFloat() throws InputXcpt
    {return parseFloat(ss.get());}


    // private static final DateTimeFormatter dtf = new DateTimeFormatterBuilder().append(
    // 	null, new DateTimeParser[]{ 
    // 	    DateTimeFormat.forPattern("MM-dd-yy").getParser(),
    // 	    DateTimeFormat.forPattern("M-d-yy").getParser()
    // 	}).toFormatter();

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M-d-yy");
    
    /**
       @return Parsed date as a {@link java.time.LocalDate} from the StringSource
    */
    public LocalDate getDate() throws InputXcpt
    {return parseDate(ss.get());}

    /**
       Attempts to parse a String as a date in MM-dd-yy or MM/dd/yy format.
    */
    public static LocalDate parseDate(String str) throws InputXcpt{
	str = str.replaceAll("/", "-");
	try {
	    return LocalDate.parse(str, dtf);
	} catch (DateTimeParseException e){
	    throw new InputXcpt(str, "Could not be parsed in MM-dd-yy or MM/dd/yy format");
	}
    }

    public void done(){;}

}
