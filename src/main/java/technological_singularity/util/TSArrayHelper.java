package technological_singularity.util;

import java.util.ArrayList;
import java.util.List;

public class TSArrayHelper {
	@SuppressWarnings("unchecked")
	public static <T> List<T>[] createArrayOfEmptyLists(int amount){
		ArrayList<T>[] lists = new ArrayList[amount];
		for(int i=0;i<amount;i++){
			lists[i] = new ArrayList<T>();
		}
		return lists;
	}
}
