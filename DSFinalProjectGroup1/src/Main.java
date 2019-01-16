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
		HashMap<String, String> sorted = sortGoogleQuery.sort(googleResult);
		
		String[][] s = new String[sorted.size()][2];
		int num = 0;
		for(Entry<String, String> entry : sorted.entrySet()) {
		    String key = entry.getKey();
		    String value = entry.getValue();
		    s[num][0] = key;
		    s[num][1] = value;
		    num++;
		}
		int i = 1;
		for(Entry<String, String> entry : sorted.entrySet()) {
			System.out.print(i + " ");
			i++;
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
		}
		search.close();
		googleResult.clear();
	}
}