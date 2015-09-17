package sourceone.pages.reports;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.time.*;

public class PaymentsReport extends Report{
    
    public PaymentsReport(Page p) throws Exception{
	super("Payments", p, new Account[]{
		new PayAccount(true),
		new PayAccount(false)});
	rKey = Key.payKey;
	init();
    }

}
