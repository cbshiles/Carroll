package sourceone.fields;
import javax.swing.*;
import java.awt.*;

public class NoLabelField extends Field{

    public JTextField tf = newText();

    public NoLabelField (String n)
    {super(n);}

    public NoLabelField (int n, JComponent j){
    	super("", j);
	jp.setLayout(new GridLayout(1, 1));
	tf = new JTextField(n);
    }
    
    public String text()
    {return tf.getText();}

    public void addListener(FieldListener dl){
	tf.getDocument().addDocumentListener(dl);
    }
}
