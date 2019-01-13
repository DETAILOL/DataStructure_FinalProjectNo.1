import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static File aLinkFile, imgLinkFile, docLinkFile, errorLinkFile;

	public static void main(String[] args) throws IOException {
		
		System.out.println("Please enter the keyword you want to search for: ");
		Scanner search = new Scanner(System.in);

		// Call Google Result
		while (search.hasNextLine()) {
			String keyword = search.next();
			GoogleQuery googleQuery = new GoogleQuery(keyword);

			googleQuery.query();
		}
		search.close();
//		ArrayList<Keyword> moms = new ArrayList<>();
//	
//
//	 for (String mom : moms) {
//	 WebPage rootPage = new WebPage(url, urls.indexOf(url));
//	 WebTree tree = new WebTree(rootPage);
//
//	 tree.root.children.get(1).addChild(new WebNode(new WebPage("uri", "name")));
//
//	 ArrayList<Keyword> keywords = new ArrayList<>();
//	 keywords.add(new Keyword("game",1));
//	 keywords.add(new Keyword("free",3));
//	 keywords.add(new Keyword("download",3));
//	 tree.setPostOrderScore(keywords);
//	 unSorted.add(new Keyword(url,rootPage.score));
//	 tree.printTree();
}
	// QuickSort
	// Sort.sort(unSorted, 0, unSorted.size()-1);
	//
	// //Print Result
	// int i = 0;
	// for(Keyword k : unSorted) {
	// System.out.print(i+1);
	// i++;
	// System.out.println(k.toString());
	// }
	// }
}