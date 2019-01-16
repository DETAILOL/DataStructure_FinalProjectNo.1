import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static File aLinkFile, imgLinkFile, docLinkFile, errorLinkFile;

	public static void main(String[] args) throws IOException {

		System.out.println("Please enter the keyword you want to search for: ");

		// Call Google Search Result
		Scanner search = new Scanner(System.in);

		String keyword = search.next();
		ArrayList<WebTree> googleResult = new GoogleQuery(keyword).query();
		ArrayList<WebTree> unSorted = new ArrayList<>();

		//		 Tree Construction
		for (WebTree result : googleResult) {
			ArrayList<Keyword> keywords = new ArrayList<>();
			keywords.add(new Keyword("game", 1));
			keywords.add(new Keyword("free", 3));
			keywords.add(new Keyword("download", 3));
			
			keywords.add(new Keyword("porn", -4));
			keywords.add(new Keyword("sex", -1));
			keywords.add(new Keyword("nude", -1));
			keywords.add(new Keyword("tits", -1));

			unSorted.add(result);
			result.setPostOrderScore(keywords);
			result.printTree();
		}
		Sort.sort(unSorted, 0, unSorted.size()-1);
		int i = 1;
		for(WebTree k: unSorted) {
			System.out.print(i + " ");
			i++;
			System.out.println(k.treename+k.treescore);
			System.out.println(k.treeurl);
		}
		search.close();
		googleResult.clear();
	}

	public static class Sort {

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
}