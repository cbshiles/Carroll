package sourceone.pages;

import sourceone.fields.*;
import sourceone.key.*;
import sourceone.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class Curtailments extends TablePage {
    JButton jb;
    Key inKey;
    Input in;
    LocalDate date;

    public Curtailments(Page p) throws Exception{
	super("Curtailments", p);

	inKey = Key.floorKey.just(new String[]{"ID", "Date Bought", "VIN", "Vehicle", "Item Cost", "Title", "Curtailed"});
	date = LocalDate.now();
	reload();
	
	sourceone.fields.TextField dateBlank;
	dateBlank = new sourceone.fields.TextField("Curtailment Date:", BasicFormatter.cinvert(date));
	jp.add(dateBlank.getJP());

	dateBlank.addListener(new FieldListener() {
		public void dew() {
		    try {
			date = StringIn.parseDate(dateBlank.text());
			reload();
		    } catch (InputXcpt ix) {;/*System.err.println("HGXB");*/}
		    catch (Exception e) {new XcptDialog(Curtailments.this, e);}
		}});

	jp.add(jb = new JButton("Curtail Auto"), BorderLayout.SOUTH);
	jb.addActionListener(new Axon(inKey, g.view.key));

	wrap();
    }

    public void reload() throws Exception{
//similar to backpages reload, but also does pushtable
	in = new QueryIn(inKey, "WHERE DATEDIFF( CURDATE(), Date_Bought ) >= 90 AND Date_Paid IS NULL ORDER BY Date_Bought");

	g = new Grid(inKey, in); g.pull();

	g.addView(new String[]{"ID", "Title", "Curtailed"},
		  new Cut[]{new StringCut("Title"), new FloatCut("Daily Rate"), new IntCut("Days Active"), new FloatCut("Accrued Interest"),
			    new FloatCut("Fees"), new FloatCut("Curtailment Price")}, new FloorCalc(inKey, date, false));
	
	pushTable(true);
	jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

	    SQLBot.bot.update("UPDATE Cars SET Date_Paid="+BasicFormatter.cinvert(date)+", Pay_Off_Amount="+vo[cp]+", Curtailed=2 WHERE ID="+go[id]);
	    System.err.println("UPDATE Cars SET Date_Paid="+BasicFormatter.cinvert(date)+", Pay_Off_Amount="+vo[cp]+", Curtailed=2 WHERE ID="+go[id]);

	    sertView.receiveEntry(go);
	    System.err.println(sertView);
	    sertView.push();

	    kill();
	    
	    } catch (Exception ix) {new XcptDialog(Curtailments.this, ix); ix.printStackTrace();}
	}
    }

    private class TailEnt implements Enterer{
	int ic, vin, veh, tit;
	public TailEnt(Key k){
	    ic = k.dex("Item Cost");
	    vin = k.dex("VIN");
	    veh = k.dex("Vehicle");
	    tit = k.dex("Title");
	}
	//"Date Bought", "VIN", "Vehicle", "Item Cost", "Title", "Curtailed"});
	public Object[] editEntry(Object[] o){
	    return new Object[]{
		date,
		o[vin],
		o[veh],
		.9f*(float)o[ic],
		o[tit],
		1
	    };
	}
    }
}
