package sourceone.pages;
import sourceone.fields.*;
import javax.swing.*;
import java.awt.*;

public class Form extends Page { //Page for inputting information

    DataMap map = new DataMap();

    public Form(String name){
	super(name);
    }
    
    public void addF(Field f) {
	map.put(f);
	add(f.getJP());
    }

    public String noWhite(String a){
	return a.trim().replaceAll("\\s", "_");
    }

}
