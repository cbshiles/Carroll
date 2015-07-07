package sourceone.fields;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;

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
    
    public String text()
    {return tf.getText();}

    public void addListener(DocumentListener dl){
	tf.getDocument().addDocumentListener(dl);
    }
}
