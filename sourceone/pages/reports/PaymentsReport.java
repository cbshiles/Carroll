package sourceone.pages;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.blobs.*;
import java.time.*;

public class PaymentsReport extends CenterFile{
    
    public PaymentsReport(Page p) throws Exception{
	super("Payments", p, new Account[]{
		new PayAccount(true),
		new PayAccount(false)});
    }

    	@Override
	public Key sendKey(){ return new Key(new Cut[]{new StringCut("Date"), new StringCut("Transaction"), new StringCut("Principle"), new StringCut("Interest"), new StringCut("Total Amount")});}


}

// public void doPif(int i, PayInFact pif){
// 	((ContractAccount)accounts[i]).addPif(pif);
// }

