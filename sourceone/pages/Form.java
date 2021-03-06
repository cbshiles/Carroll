package sourceone.pages;
import sourceone.key.*;//StringSource;
import sourceone.fields.Field;
import javax.swing.*;
import java.awt.*;

public class Form extends Page implements StringSource{ //Page for inputting information

    java.util.List<Field> fields = new java.util.ArrayList<Field>();
    boolean gotem = true;
    int i;

    public Form(String name, Page p){
	super(name, p);
    }
    
    public void addF(Field f) {
	fields.add(f);
	add(f.getJP()); //This is why you can't use that other constructor
    }

    public boolean hasEntries()
    {
	boolean ans = gotem;
	gotem = !gotem;
	return ans; 
    }

    public void freshen(){
	for (Field f : fields)
	    f.clear();
    }

    public String getName(){
	if (i != 0) i--;
	return fields.get(i).name;
    }

    public String get() throws InputXcpt{
	if (i >= fields.size())
	{i=0; throw new InputXcpt("Not enough fields");}
	return fields.get(i++).text();
    }

    public void refresh() { //Allow info to be input again
	gotem = true;
	i=0;
    }
}
