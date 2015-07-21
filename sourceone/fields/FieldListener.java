package sourceone.fields;
import javax.swing.event.*;

public abstract class FieldListener implements DocumentListener{
    public void changedUpdate(DocumentEvent e) {dew();}

    public void removeUpdate(DocumentEvent e) {dew();}

    public void insertUpdate(DocumentEvent e) {dew();}

    public abstract void dew();
}
