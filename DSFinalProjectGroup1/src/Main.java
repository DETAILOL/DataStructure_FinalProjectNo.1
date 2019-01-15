import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static File aLinkFile, imgLinkFile, docLinkFile, errorLinkFile;

	public static void main(String[] args) throws IOException {

		System.out.println("Please enter the keyword you want to search for: ");

		// Call Google Search Result
		Scanner scanner = new Scanner(System.in);
		ArrayList<WebTree> googleResult = new GoogleQuery(scanner.next()).query();
		scanner.close();
		
		// Tree Construction
		for (WebTree tree : googleResult) {
			ArrayList<Keyword> keywords = new ArrayList<>();
			keywords.add(new Keyword("game", 1));
			keywords.add(new Keyword("free", 3));
			keywords.add(new Keyword("download", 3));
			tree.setPostOrderScore(keywords);
			// unSorted.add(tree);
			tree.printTree();
		}
		
//		Sort.sort(unSorted, 0, unSorted.size() - 1);
//		int i = 0;
//		for (WebTree k : unSorted) {
//			System.out.print(i + 1);
//			i++;
//			System.out.println(k.toString());
//		}
		googleResult.clear();
	}
}