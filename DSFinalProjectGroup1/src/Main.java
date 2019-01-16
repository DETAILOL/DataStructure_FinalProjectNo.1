import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class Main {
	public static File aLinkFile, imgLinkFile, docLinkFile, errorLinkFile;

	public static void main(String[] args) throws IOException {

		System.out.println("Please enter the keyword you want to search for: ");

		// Call Google Search Result
		Scanner search = new Scanner(System.in);

		String keyword = search.next();
		ArrayList<WebTree> googleResult = new GoogleQuery(keyword).query();
		ArrayList<WebTree> sorted = sortGoogleQuery.sort(googleResult);
		for (WebTree webTree : sorted) {
			System.out.println(webTree.treename+" "+webTree.treescore);
			System.out.println(webTree.treeurl);
		}
//		int i = 1;
//		for(Entry<String, String> entry : sorted.entrySet()) {
//		    System.out.print(i + " ");
//			i++;
//			System.out.println(entry.getKey());
//			System.out.println(entry.getValue());
//		}
		search.close();
		googleResult.clear();
	}
}