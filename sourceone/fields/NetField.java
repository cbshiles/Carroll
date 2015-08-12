package sourceone.fields;
import javax.swing.*;
import java.awt.*;
import sourceone.key.*;

public class NetField extends Field{
    protected JTextField per = newText("72"), amt = newText("---");

    float gross; boolean hearing=true; TextField tot, res;
    public NetField (TextField tot, TextField res){
	super("Net:");
	this.tot = tot; this.res= res;
	jp.setLayout(new GridLayout(1, 4));
	jp.add(newLabel(name+" % of Gross"));
	jp.add(per);
	jp.add(newLabel("Amount"));
	jp.add(amt);

	FieldListener outl = new NetListener();
	tot.addListener(outl);
	res.addListener(outl);

	per.getDocument().addDocumentListener(new FieldListener(){
		public void dew(){
		    if (! hearing) return;
		    hearing = false;
		    try {
			float f = getPer()*gross;
			amt.setText(""+View.rnd(f));
		    } catch (InputXcpt ix) {amt.setText("---");}
		    hearing = true;
		}
	    });


	amt.getDocument().addDocumentListener(new FieldListener(){
		public void dew(){
		    if (! hearing) return;
		    hearing = false;
		    try {
			float f = StringIn.parseFloat(amt.getText())/gross;
			per.setText(""+View.rnd(100*f));
		    } catch (InputXcpt ix) {;}//amt.setText("---");}
		    hearing = true;
		}
	    });
    }

    private class NetListener extends FieldListener{
	public void dew(){
	    if (! hearing) return;
	    hearing = false;
	    try {
		gross = StringIn.parseFloat(tot.text()) - StringIn.parseFloat(res.text());
		amt.setText(""+View.rnd(gross*getPer()));
	    } catch (InputXcpt ix) {amt.setText("---");}
	    hearing = true;
	}
	
    }

    private float getPer() throws InputXcpt{
	return StringIn.parseFloat(per.getText())/100f;
    }

    public String text(){
	return amt.getText();
    }

    public void clear(){
	per.setText("72");
	amt.setText("---");
    }
}
