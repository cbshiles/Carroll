import javax.swing.*;
import java.awt.*;

public class TextField extends Field{

    JTextField tf = new JTextField();

    public TextField (String n){
	super(n);
//	jp.setLayout(new BoxLayout(jp, BoxLayout.LINE_AXIS));
	jp.setLayout(new GridLayout(0, 2));
	JLabel jl = new JLabel(name, SwingConstants.CENTER);
	jp.add(jl);
	tf.setBorder(BorderFactory.createMatteBorder(30, 15, 30, 15, EntryForm.bkgd));
	jp.add(tf);
    }
    
    public String text()
    {
	return tf.getText();
    }
}
