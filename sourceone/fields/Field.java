package sourceone.fields;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

//Class representing graphical component returning a string

public abstract class Field {

    String name;
    JPanel jp;
    
    public Field (String n, JPanel j)
    {
	name = n;
	jp = j;
    }

    public Field (String n)
    {
	name = n;
	jp = new JPanel();
	jp.setOpaque(false);
	jp.setBorder(LineBorder.createBlackLineBorder());
    }

    public JPanel getJP(){return jp;}

    public abstract String text();

    JLabel newLabel(String n){
	return new JLabel(n, SwingConstants.CENTER);
    }

    JTextField newText(){
	JTextField tf = new JTextField();
//	tf.setBorder(BorderFactory.createMatteBorder(15, 5, 15, 5, Page.bkgd));
	return tf;
    }
}
