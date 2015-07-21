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

    public TextField (String n, String init){
	this(n);
	tf.setText(init);
    }

    public void clear(){tf.setText("");}
    
    public String text()
    {return tf.getText();}

    public void addListener(FieldListener dl){
	tf.getDocument().addDocumentListener(dl);
    }
}
