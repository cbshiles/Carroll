public class SQLOut implements Output{
    String stmt, vals="";
    boolean first = true;
    
    public SQLOut(Key k, String tableName){
	stmt = "INSERT INTO "+tableName+" (";
	
	String keys = "";
	for (Cut c : k.cuts){
	    if (! first) keys += ", ";
	    else first = true;
	    keys += c.sqlName
	}

	stmt += keys+") VALUES (";
	first = false;
    }

    public void put(String s)
    {
	if (s == null) return null;
	else add("'"+s+"'");
    }

    public void put(LocalDate d)
    {
	if (d == null) return null;
	else add("'"+d.toString()+"'");
    }
    
    public void put(int n)
    {add(n.toString());}

    public void put(float n)
    {add(n.toString());}

    private void add(String s) {
	if (! first) vals += ", ";
	else first = false;
	vals += s;
    }

    public void endEntry(){
	System.out.println(stmt+vals+");");
//	SQLBot.bot.update(stmt+vals+");");
	vals = "";
    }
}
