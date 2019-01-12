import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
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
	            if(number.get(j).weight <= number.get(right).weight) { 
	                i++; 
	                swap(number, i, j); 
	            } 
	        } 
	        swap(number, i+1, right); 
	        return i+1; 
	    } 

	    private static void swap(ArrayList<Keyword> number, int i, int j) {
	        int indexOfJ = number.indexOf(number.get(j));
	    	Keyword t = number.remove(i);
	    	number.add(i, number.get(j));
	        number.add(indexOfJ, t);
	    }
	}
	public static void main(String[] args) throws IOException {
		ArrayList<Keyword> unSorted = new ArrayList<>();
		//Scanner search = new Scanner(System.in);

		//Call Google Result
		
		//CountScore
		ArrayList<String> urls = new ArrayList<>();
		urls.add("http://down.ali213.net/pcgame/");
		urls.add("https://m.wanyx.com/");
		urls.add("http://pc.duowan.com/xiazai/pojie/");
		urls.add("http://bbs.3dmgame.com/forum-129-1.html");
		for (String url : urls) {
			WebPage rootPage = new WebPage(url, urls.indexOf(url));
			WebTree tree = new WebTree(rootPage);

			// tree.root.children.get(1).addChild(new WebNode(new WebPage("uri", "name")));

			ArrayList<Keyword> keywords = new ArrayList<>();
			keywords.add(new Keyword("game",1));
			keywords.add(new Keyword("free",3));
			keywords.add(new Keyword("download",3));
			tree.setPostOrderScore(keywords);
			unSorted.add(new Keyword(url,rootPage.score));
			tree.printTree();
		}
		//QuickSort
		Sort.sort(unSorted, 0, unSorted.size()-1);
		
		//Print Result
		int i = 0;
		for(Keyword k : unSorted) {
			System.out.print(i);
			i++;
			System.out.println(k.toString());
		}
	}
}