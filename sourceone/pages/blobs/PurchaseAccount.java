package sourceone.pages.blobs;

import sourceone.key.*;
import sourceone.sql.*;
import sourceone.pages.*;
import java.time.*;

public class PurchaseAccount extends CenterFile.Account{

    public PurchaseAccount(){super("Purchases", new Blob[]{/*new FloorBlob(true),*/ new PurBlob(true), new PurBlob(false)});} 

    public float getStart(LocalDate ld)throws Exception{
	return 0f;
    }
}
