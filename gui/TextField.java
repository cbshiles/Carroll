import javax.swing.*;
import java.awt.*;

public class TextField extends Field{

    JTextField tf = newText();

    public TextField (String n){
	super(n);
//	jp.setLayout(new BoxLayout(jp, BoxLayout.LINE_AXIS));
	jp.setLayout(new GridLayout(0, 2));

	jp.add(newLabel(name));
	jp.add(tf);
    }
    
    public String text()
    {
	return tf.getText();
    }
}
