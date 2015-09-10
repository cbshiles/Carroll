package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.blobs.*;
import java.time.*;

public class PurchaseReport extends TablePage{
    public PurchaseReport extends TablePage(Page p){
	super("Payments Report", p);
	Key k = Key.contractKey.just(new String[]{"Total Contract", "Gross Amount", "Net Amount", "Date Bought"});

	try{
	    Input in = new QueryIn(k, " WHERE Title=1 AND Pay_Off_Amount IS NOT NULL");

	    g = new Grid(key, in);

	    g.addTable();
	    g.pull();
	    g.sort("VIN", true);

	    pushTable(false);

	    jp.add(jb = new JButton("Title Em"), BorderLayout.SOUTH);

	    setContentPane(jp);

    }
}
