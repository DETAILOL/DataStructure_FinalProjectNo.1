import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class sortGoogleQuery {

	public static ArrayList<WebTree> sort(ArrayList<WebTree> webTrees) throws IOException {
		
		HashMap<String, String> retVal = new HashMap<String, String>();

		//Tree Construction
		ArrayList<WebTree> unSorted = new ArrayList<>();
		
		for (WebTree result : webTrees) {
			
			ArrayList<Keyword> keywords = new ArrayList<>();
			keywords.add(new Keyword("game", 2));
			keywords.add(new Keyword("free", 2));
			keywords.add(new Keyword("download", 5));

			keywords.add(new Keyword("porn", -4));
			keywords.add(new Keyword("sex", -1));
			keywords.add(new Keyword("nude", -1));
			keywords.add(new Keyword("tits", -1));
			unSorted.add(result);
			result.setPostOrderScore(keywords);
			result.printTree();
			
		}
		sort(unSorted, 0, unSorted.size() - 1);
		
		return unSorted;
	}

	public static void sort(ArrayList<WebTree> googleResult, int left, int right) {
		if (left < right) {
			int q = partition(googleResult, left, right);
			sort(googleResult, left, q - 1);
			sort(googleResult, q + 1, right);
		}
	}

	private static int partition(ArrayList<WebTree> googleResult, int left, int right) {
		int i = left - 1;
		for (int j = left; j < right; j++) {
			if (googleResult.get(j).treescore >= googleResult.get(right).treescore) {
				i++;
				swap(googleResult, i, j);
			}
		}
		swap(googleResult, i + 1, right);
		return i + 1;
	}

	private static void swap(ArrayList<WebTree> googleResult, int i, int j) {
		WebTree tj = googleResult.get(j);
		int indexOfJ = googleResult.indexOf(googleResult.get(j));
		WebTree ti = googleResult.remove(i);
		googleResult.add(i, tj);
		googleResult.remove(indexOfJ);
		googleResult.add(indexOfJ, ti);
	}
}
