package sourceone.key;

/**
   A Cut simply connects an Input and Output. Has a subclass corresponding to each Kind.
   The Kind is what type of data the Cut asks for from the Input and feeds into the Output.
   <p>
   A Cut can be thought of as the header of a column in a table or spreadsheet. It tells you the name of 
   the column, and what type of data it expects. Cuts actually have name and sqlName, for tables and print-outs, and 
   databasing, respectively.
   <p>
   Cuts do not store any processed data, they merely work to automatically cast data between Input and Output, and
   remember column names. This means reusing a Cut object is fine for any situation so far conceived, and indeed higher orders 
   of this system depend on Cut reusability through reference copying.
*/

public abstract class Cut{

    public String name, sqlName, fullSQL;
    protected Input ip;
    protected Output op;

    /**
       @param n The name of the column. From this name, sqlName is generated by removing exterior whitespace,
       and replacing any interior whitespace with underscores.
    */
    public Cut(String n)
    {name = n;
	sqlName = n.trim().replaceAll("\\s", "_");}

    public Cut clone(){
	try {
	return this.getClass().getDeclaredConstructor(String.class).newInstance(name);
	} catch (Exception e) {throw new Error("nope didnt work");}
    }

    /**
       Asks for a Object of type Kind from the Input.
       @return The object returned from the Input.
    */
    public abstract Object in() throws InputXcpt;

    /**
       Sends an Object of type Kind to the Output.
       @param o The object to be casted and sent to the Output.
     */
    public abstract void out(Object o);
}
