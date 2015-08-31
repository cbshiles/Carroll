package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
//import sourceone.csv.*;
import java.time.*;

import sourceone.pages.blobs.*;

//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;

public class ReserveReport extends CenterFile{

    Grid go;
    View v;
    
    public ReserveReport(Page p) throws Exception{
	super("Reserve", p);

	blobs.add(new BotTrax());
	blobs.add(new SoldTrax());

	dew(3, 2015, 6);
    }

    // public float getStart(LocalDate ld) {//get beginning balance, starting at ld
    // 	Key r = Key.contractKey.just("Reserve");
    // 	Grid g;
    // 	try {
    // 	    g = new Grid(r, new QueryIn(r,
    // 					"WHERE Date_Bought < '"+ld+"' AND ( Paid_Off IS NULL OR Paid_Off >= '"+ld
    // 					+"' );"));
    // 	    g.pull();
    // 	} catch (Exception e) {new XcptDialog(this, e); return .1337f;}
    // 	return -g.floatSum("Reserve");
    // }
 }
