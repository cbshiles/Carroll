package sourceone.fields;
import javax.swing.*;
import java.awt.*;

public class TextField extends Field{

    JTextField tf = newText();

    public TextField (String n){
	super(n);
	jp.setLayout(new GridLayout(1, 2));
	jp.add(newLabel(name));
	jp.add(tf);
    }
    
    public String text()
    {System.out.println("hurr: "+tf.getText());
	return tf.getText();}
}
