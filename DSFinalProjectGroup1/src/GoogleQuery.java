import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.lang.reflect.Field;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class GoogleQuery {
	public String searchKeyword;
	public String url;
	public String content;
	
	public GoogleQuery(String searchKeyword) {
		this.searchKeyword = searchKeyword;
		this.url = "https://www.google.com.tw/search?q=" + searchKeyword + "&oe=utf8&num=100";
	}

	private String fetchContent() throws IOException {
		String retVal = "";
		URL urlStr = new URL(this.url);
		URLConnection connection = urlStr.openConnection();
		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0(Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
		connection.connect();
		InputStream inputStream = connection.getInputStream();
		InputStreamReader inReader = new InputStreamReader(inputStream, "UTF8");
		BufferedReader bf = new BufferedReader(inReader);

		String line = null;
		while ((line = bf.readLine()) != null) {
			retVal += line;
		}
		return retVal;
	}

	public HashMap<String, String> query() throws IOException {
		if (this.content == null) {	
			this.content = fetchContent();
		}
		HashMap<String, String> retVal = new HashMap<String, String>();
		Document document = Jsoup.parse(this.content);
		Elements lis = document.select("div.g");
		ArrayList<String> urls = new ArrayList<>();
		ArrayList<Keyword> unSorted = new ArrayList<>();

		for (Element li : lis) {
			try {
				Element h3 = li.select("h3.r").get(0);
				String title = h3.text();

				Element cite = li.getElementsByTag("a").first();
				String citeUrl = "https://www.google.com.tw"+ cite.attr("href");

				urls.add(citeUrl);
				
				retVal.put(title, citeUrl);
			} catch (IndexOutOfBoundsException e) {
				// Do nothing
			}
		}
//		for (String url : urls) {
//			WebPage rootPage = new WebPage(url, urls.indexOf(url));
//			WebTree tree = new WebTree(rootPage);
//			
//			ArrayList<Keyword> keywords = new ArrayList<>();
//			keywords.add(new Keyword("game",1));
//			keywords.add(new Keyword("free",3));
//			keywords.add(new Keyword("download",3));
//			tree.setPostOrderScore(keywords);
//			unSorted.add(new Keyword(url,rootPage.score));
////			tree.printTree();
//			
//			}
//		Sort.sort(unSorted, 0, unSorted.size()-1);
//		int i = 0;
//		for(Keyword k : unSorted) {
//			System.out.print(i+1);
//			i++;
//			System.out.println(k.toString());
//		}
		return retVal;
	}

		public static class Sort {

		    public static void sort(ArrayList<Keyword> number, int left, int right) {
		        if(left < right) { 
		            int q = partition(number, left, right); 
		            sort(number, left, q-1); 
		            sort(number, q+1, right); 
		        } 
		    }

		    private static int partition(ArrayList<Keyword> number, int left, int right) {  
		        int i = left - 1; 
		        for(int j = left; j < right; j++) { 
		            if(number.get(j).weight >= number.get(right).weight) { 
		                i++; 
		                swap(number, i, j); 
		            } 
		        } 
		        swap(number, i+1, right); 
		        return i+1; 
		    } 

		    private static void swap(ArrayList<Keyword> number, int i, int j) {
		        Keyword tj = number.get(j);
		    	int indexOfJ = number.indexOf(number.get(j));
		    	Keyword ti = number.remove(i);
		    	number.add(i, tj);
		    	number.remove(indexOfJ);
		        number.add(indexOfJ, ti);
		    }
		}
}