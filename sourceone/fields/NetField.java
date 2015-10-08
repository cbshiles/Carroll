package sourceone.fields;
import javax.swing.*;
import java.awt.*;
import sourceone.key.*;

public class NetField extends TextField{
    public JTextField amt = newText("---"), pro=newText();

    float gross; boolean hearing=true; TextField tot;

    @Override
    public void addListener(javax.swing.event.DocumentListener dl){
	amt.getDocument().addDocumentListener(dl);
    }
    
    public NetField (TextField tot){
	super("% of Gross:");
	tf.setText("72");
	this.tot = tot;
	jp.setLayout(new GridLayout(1, 4));

	jp.add(newLabel("Amount"));
	jp.add(amt);

	jp.add(newLabel("Profit"));
	jp.add(pro);

	pro.setEditable(false);
	
	javax.swing.event.DocumentListener outl = new NetListener();
	tot.addListener(outl);

	tf.getDocument().addDocumentListener(new FieldListener(){
		public void dew(){
		    if (! hearing) return;
		    hearing = false;
		    try {
			float f = getTf()*gross;
			amt.setText(""+View.rnd(f));
			pro.setText(""+View.rnd(gross-f));
		    } catch (InputXcpt ix) {amt.setText("---");}
		    hearing = true;
		}
	    });


	amt.getDocument().addDocumentListener(new FieldListener(){
		public void dew(){
		    if (! hearing) return;
		    hearing = false;
		    try {
			float am = StringIn.parseFloat(amt.getText());
			float f = am/gross;
			tf.setText(""+View.rnd(100*f));
			pro.setText(""+View.rnd(gross-am));
		    } catch (InputXcpt ix) {;}//amt.setText("---");}
		    hearing = true;
		}
	    });
    }

    private class NetListener extends FancyListener{
	public void dew(javax.swing.event.DocumentEvent e){
	    if (! hearing) return;
	    hearing = false;
	    try {
		gross = StringIn.parseFloat(tot.text());
		float f = gross*getTf();
		amt.setText(""+View.rnd(f));
		pro.setText(""+View.rnd(gross-f));
		tf.setText(tf.getText());
	    } catch (InputXcpt ix) {amt.setText("---");}
	    hearing = true;
	}
	
    }

    private float getTf() throws InputXcpt{
	return StringIn.parseFloat(tf.getText())/100f;
    }

    public String text(){
	return amt.getText();
    }

    public void clear(){
	tf.setText("72");
	amt.setText("---");
    }
}
