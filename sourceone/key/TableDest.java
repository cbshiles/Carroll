package sourceone.key;
import javax.swing.JTable;

public class TableDest implements StringDest {

    Key key;
    int i=0, j=0;
    Object[][] arr;
    
    public TableDest(Key k, int entries){
	key = k;
	arr = new Object[entries][key.length];
    }

    public void put(String s){
//add to Object array?
	arr[i][j++] = s;
    }

    public void endEntry(){
	i++;
	j=0;
//change pointer to next line
    }

    public void close(){

    }

    public JTable getTable()throws InputXcpt{
	return new JTable(arr, key.tableNames());
    }
}
