package sourceone.key;

import java.util.HashMap;

public class KeyMap extends Key {

    private HashMap<String, Integer> map;

    public KeyMap(Cut[] c){super(c); fillMap();}

    public KeyMap(String[] names, Kind[] types){super(names, types); fillMap();}

    private void fillMap(){
	map = new HashMap<String, Integer>();
	for (int i=0; i<cuts.length; i++){
	    map.put(cuts[i].name, i);
	}
    }

    public int dex(String cName){
	Integer i = map.get(cName);
	if (i == null){
	    System.err.println(cName+" is not found in the key mapping!");
	    System.exit(0);
	}
	return i;
    }

    public Key except(String[] names){
	return except(populate(names));
    }

    public Key just(String[] names){
	return just(populate(names));
    }

    private int[] populate(String[] names){
	int[] ix = new int[names.length];
	for (int i=0; i<names.length; i++){
	    ix[i] = dex(names[i]);
	}
	return ix;
    }


}
