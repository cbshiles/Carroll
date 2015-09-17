package sourceone.pages.reports;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.time.*;

public class PurchaseReport extends Report{

    public PurchaseReport (Page p){
	super("Purchase Report", p, new Account[]{new PurCatchAccount()}); 
	rKey = Key.protKey;
	init();
    }
}
