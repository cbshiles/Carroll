package sourceone.fields;
import javax.swing.*;
import java.awt.*;
import sourceone.key.*;

public class NetField extends Field{
    protected JTextField per = newText("72"), amt = newText("---"), pro=newText();

    float gross; boolean hearing=true; TextField tot, res;
    public NetField (TextField tot, TextField res){
	super("Net:");
	this.tot = tot; this.res= res;
	jp.setLayout(new GridLayout(1, 4));
	jp.add(newLabel(name+" % of Gross"));
	jp.add(per);
	jp.add(newLabel("Amount"));
	jp.add(amt);

	jp.add(newLabel("Profit"));
	jp.add(pro);

	pro.setEditable(false);
	
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
			per.setText(""+View.rnd(100*f));
			pro.setText(""+View.rnd(gross-am));
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
		float f = gross*getPer();
		amt.setText(""+View.rnd(f));
		pro.setText(""+View.rnd(gross-f));
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
