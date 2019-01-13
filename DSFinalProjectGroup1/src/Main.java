import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) throws IOException {
		ArrayList<Keyword> unSorted = new ArrayList<>();
		System.out.println("Please enter the keyword you want to search for: ");
		Scanner search = new Scanner(System.in);

		//Call Google Result
		while(search.hasNextLine()) {
			String keyword = search.next();
			GoogleQuery googleQuery = new GoogleQuery(keyword);
			googleQuery.query();
		}search.close();
		//CountScore
		
//		urls.add("http://down.ali213.net/pcgame/");
//		urls.add("https://m.wanyx.com/");
//		urls.add("http://pc.duowan.com/xiazai/pojie/");
//		urls.add("http://bbs.3dmgame.com/forum-129-1.html");
//		for (String url : urls) {
//			WebPage rootPage = new WebPage(url, urls.indexOf(url));
//			WebTree tree = new WebTree(rootPage);

			// tree.root.children.get(1).addChild(new WebNode(new WebPage("uri", "name")));

//			ArrayList<Keyword> keywords = new ArrayList<>();
//			keywords.add(new Keyword("game",1));
//			keywords.add(new Keyword("free",3));
//			keywords.add(new Keyword("download",3));
//			tree.setPostOrderScore(keywords);
//			unSorted.add(new Keyword(url,rootPage.score));
//			tree.printTree();
		}
//		}
//	}
}