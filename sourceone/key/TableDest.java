package sourceone.key;
import javax.swing.*;
import javax.swing.table.*;

public class TableDest implements StringDest {

    Key key;
    int i=0;
    Object[] arr;
    DefaultTableModel model;
    JTable table;
    
    public TableDest(Key k){
	key = k;
	model = new DefaultTableModel(key.tableNames(), 0);
	table = new JTable(model);
	arr = new Object[key.length];
    }

    public void put(String s){
//add to Object array
	arr[i++] = s;
    }

    public void endEntry(){
	model.addRow(arr);
	i=0; //start at beginning of line
	arr = new Object[key.length];
    }

/**
   @return A JTable with all the outputted information.
*/
    public Object close(){
	return table;
    }
}
