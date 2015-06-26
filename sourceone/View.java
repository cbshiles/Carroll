public class View {
    Table tbl;
    int[] dex;

    public View(Table t, int[] d){
	tbl = t;
	dex = d;
    }

    public String[] getHeads(){
	String[] heads = new String[dex.length];
	for (int i=0; i<dex.length; i++)
	    heads[i] = tbl.cols[dex[i]].name;
	return heads;
    }

    // public boolean in(int[] dex, int x){
    // 	for(int i=0; i<dex.length; i++){
    // 	    if (dex[i] == x) return true;
    // 	}
    // 	return false;
    // }


    public Object[][] getData(Table tbl){
	int rows = tbl.objs.length, cols = dex.length;
	Object[][] arr = new Object[rows][cols];
	
	for (int i=0; i<rows; i++){
	    for (int j=0; j<cols; j++){
		arr[i][j] = tbl.objs[i][dex[j]];
	    }
	}
	return null;
    }
    
    // public Object[][] getData(Table tbl){
    // 	int it=0, iv=0;
    // 	int dex[] = new int[cols.length];
    // 	while (it < tbl.cols.length && iv < this.cols.length){
    // 	    if (tbl.cols[it].name.equals(cols[iv].name)){
    // 		dex[iv] = it;
    // 		iv++;
    // 	    }
    // 	    it++;
    // 	}
    // 	Object[][] arr = new Object[tbl.objs.length][cols.length];
    // 	for (int r=0; r<tbl.objs.length; r++){
	    
    // 	}

    // }
}
