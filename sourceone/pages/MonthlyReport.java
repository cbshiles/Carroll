package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.blobs.*;
import java.time.*;

public class MonthlyReport extends CenterFile{
    
    public MonthlyReport(Page p) throws Exception{
	super("Monthly", p, new Account[]{
		// new ContractAccount(false),
		// new ContractAccount(true),
		new PurchaseAccount(),
//		new FloorAccount(),
		new ReserveReport.ResAccount()});

//	 PayInFact pif = new PayInFact();
//	 doPif(0, pif);
//	 System.out.println("OWWW");
//	 doPif(1, pif);
	 dew();
    }

    // public void doPif(int i, PayInFact pif){
    // 	((ContractAccount)accounts[i]).addPif(pif);
    // }
    
 }
