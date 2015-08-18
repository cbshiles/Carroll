package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

//# works only with current date

public class Curtailments extends TablePage {
    JButton jb;
    public Curtailments(Page p) throws Exception{
	super("Curtailments", p);

	Key inKey = Key.floorKey.just(new String[]{"ID", "Date Bought", "VIN", "Vehicle", "Item Cost", "Title", "Curtailed"});

	Input in = new QueryIn(inKey, "WHERE DATEDIFF( CURDATE(), Date_Bought ) >= 90 AND Date_Paid IS NULL");

	g = new Grid(inKey, in); g.pull();

	g.sort("Date Bought", true);

	g.addView(new String[]{"ID", "Title", "Curtailed"}, new Cut[]{new StringCut("Title"), new FloatCut("Daily Rate"), new IntCut("Days Active"),
								      new FloatCut("Accrued Interest"), new FloatCut("Fees"), new FloatCut("Curtailment Price")}, new FloorCalc(inKey, LocalDate.now(), false));
	
	pushTable(true);
	jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	jp.add(jb = new JButton("Curtail Auto"), BorderLayout.SOUTH);
	jb.addActionListener(new Axon(inKey, g.view.key));

	wrap();
    }

    private class Axon implements ActionListener{
	int id, cp;
	Key sertKey, gk;
	View sertView;
	
	public Axon(Key gk, Key vk){
	    this.gk = gk;
	    id = gk.dex("ID");
	    cp = vk.dex("Curtailment Price");
	    sertKey = Key.floorKey.just(new String[]{"Date Bought", "VIN", "Vehicle", "Item Cost", "Title", "Curtailed"});
	    sertView = new View(sertKey, new TailEnt(gk));
	    sertView.addOut(new SQLFormatter(new InsertDest(sertKey)));
	}
	public void actionPerformed(ActionEvent e){

	    try{
	    int[] dx = jt.getSelectedRows();
	    if (dx.length == 0) throw new InputXcpt("No car selected");
	    Object[] go = g.data.get(dx[0]);
	    Object[] vo = g.view.data.get(dx[0]);

	    SQLBot.bot.update("UPDATE Cars SET Date_Paid=CURDATE(), Pay_Off_Amount="+vo[cp]+", Curtailed=2 WHERE ID="+go[id]);
	    System.err.println("UPDATE Cars SET Date_Paid=CURDATE(), Pay_Off_Amount="+vo[cp]+", Curtailed=2 WHERE ID="+go[id]);

	    sertView.receiveEntry(go);
	    System.err.println(sertView);
	    sertView.push();

	    //SQLBot.bot.update(
// set date paid to today & paid amount to Curtailment Price
	    //add new Entry to cars

	    kill();
	    
	    } catch (Exception ix) {new XcptDialog(Curtailments.this, ix); ix.printStackTrace();}
	}
    }

    private class TailEnt implements Enterer{
	int ic, vin, veh;
	public TailEnt(Key k){
	    ic = k.dex("Item Cost");
	    vin = k.dex("VIN");
	    veh = k.dex("Vehicle");
	}
	//"Date Bought", "VIN", "Vehicle", "Item Cost", "Title", "Curtailed"});
	public Object[] editEntry(Object[] o){
	    return new Object[]{
		LocalDate.now(),
		o[vin],
		o[veh],
		.9f*(float)o[ic],
		0,
		1
	    };
	}
    }
}
//    JUST NIX THAT ENTRY AND START A NEW ONE
