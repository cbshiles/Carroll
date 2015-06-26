package sourceone.fields;
import javax.swing.*;
import java.awt.*;

public class NoLabelField extends Field{

    JTextField tf = newText();

    public NoLabelField (String n)
    {super(n);}
    
    public String text()
    {return tf.getText();}
}
