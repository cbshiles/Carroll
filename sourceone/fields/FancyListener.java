package sourceone.fields;
import javax.swing.event.*;

public abstract class FancyListener implements DocumentListener{
    public void changedUpdate(DocumentEvent e) {dew(e);}

    public void removeUpdate(DocumentEvent e) {dew(e);}

    public void insertUpdate(DocumentEvent e) {dew(e);}

    public abstract void dew(DocumentEvent e);
}
