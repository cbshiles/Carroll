package sourceone.pages.reports;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.time.*;

public class InvReport extends Report{

    public InvReport (Page p){
	super("Inventory Report", p, new Account[]{
		new PayAccount(true), new PayAccount(false), new PurCatchAccount()}, true);
	rKey = Key.invKey;
	init();
    }
}
