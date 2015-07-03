package sourceone;

import sourceone.sql.*;
import sourceone.csv.*;
import sourceone.key.*;

public class CarLoad {
    public static void main(String[] args) throws Exception{
	SQLBot.bot = new SQLBot("res/db.properties");
	Grid g = new Grid(Key.floorKey.except(new int[]{0,5,6}), new StringIn(new CSVInput("res/FloorPlan7-3-15.csv")));
				View v = g.addView(null, new Cut[]{new IntCut("Title")}, new Enterer(){
				public Object[] editEntry(Object[] objs){
				    return new Object[] {0};
				}
			    });
			v.addOut(new SQLOut(v.key, "Cars"));
	g.go();
    }
}
