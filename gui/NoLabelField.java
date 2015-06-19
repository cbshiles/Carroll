import javax.swing.*;
import java.awt.*;

public class NoLabelField extends Field{

    JTextField tf = new JTextField();

    public NoLabelField (String n){
	super(n);
    }
    
    public String text() throws InputXcpt
    {
	return tf.getText();
    }
}
