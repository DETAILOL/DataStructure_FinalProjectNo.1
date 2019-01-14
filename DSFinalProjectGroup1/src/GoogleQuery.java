import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleQuery {
	public ArrayList<WebTree> webTrees = new ArrayList<>(); 
	public String searchKeyword;
	public String url;
	public String content;
	public static String results = "";
	public static int num = -1, sum = 0;
	public static File aLinkFile, imgLinkFile, docLinkFile, errorLinkFile;

	public GoogleQuery(String searchKeyword) {
		this.searchKeyword = searchKeyword;
		this.url = "https://www.google.com.tw/search?q=" + searchKeyword + "&oe=utf8&num=100";
	}

	private String fetchContent() throws IOException {
		String retVal = "";
		URL urlStr = new URL(url);
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
		aLinkFile = new File("C:/Users/user/Desktop/workplace for java ee/ALinks.txt");
		imgLinkFile = new File("C:/Users/user/Desktop/workplace for java ee/ImgLinks.txt");
		docLinkFile = new File("C:/Users/user/Desktop/workplace for java ee/DocLinks.txt");
		errorLinkFile = new File("C:/Users/user/Desktop/workplace for java ee/ErrorLinks.txt");
		// 用数组存储四个文件对象，方便进行相同操作
		File[] files = new File[] { aLinkFile, imgLinkFile, docLinkFile, errorLinkFile };
		try {
			for (File file : files) {
				if (file.exists()) // 如果文件存在
					file.delete(); // 则先删除
				file.createNewFile(); // 再创建
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (this.content == null) {
			this.content = fetchContent();
		}
		HashMap<String, String> retVal = new HashMap<String, String>();
		Document document = Jsoup.parse(this.content);
		Elements lis = document.select("div.g");


		ArrayList<WebScore> rootUrls = new ArrayList<>();
		ArrayList<WebTree> unSorted = new ArrayList<>();

		for (int i = 0; i <= 4; i++) {
			try {
				Element h3 = lis.get(i).select("h3.r").get(0);
				String title = h3.text();

				Element cite = lis.get(i).getElementsByTag("a").first();
				String citeUrl = "https://www.google.com.tw" + cite.attr("href");
				rootUrls.add(new WebScore(title,citeUrl,0));
				System.out.println(title + " " + citeUrl);
				webTrees.add(new WebTree(new WebPage(citeUrl, title)));
				getAllLinks(citeUrl,i);
				retVal.put(title, citeUrl);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		for (WebTree tree : webTrees) {
			
			ArrayList<Keyword> keywords = new ArrayList<>();
			keywords.add(new Keyword("game", 1));
			keywords.add(new Keyword("free", 3));
			keywords.add(new Keyword("download", 3));
			tree.setPostOrderScore(keywords);
			unSorted.add(tree);
			tree.printTree();
		}
		 Sort.sort(unSorted, 0, unSorted.size()-1);
		 int i = 0;
		 for(WebTree k : unSorted) {
		 System.out.print(i+1);
		 i++;
		 System.out.println(k.toString());
		 }
		return retVal;
	}

	public String getAllLinks(String path, int i) {
		Boolean found = false;
		Document doc = null;
		try {
			url = path;
			content = fetchContent();
			doc = Jsoup.parse(content);
		} catch (Exception e) {
			// 接收到错误链接（404页面）
			writeTxtFile(errorLinkFile, path + "\r\n"); // 写入错误链接收集文件
			num++;
			if (sum > num) { // 如果文件总数（sum）大于num(当前坐标)则继续遍历
//				getAllLinks(getFileLine(aLinkFile, num));
			}
			return null;
		}
		Elements aLinks = doc.select("a[href]");
		Elements imgLinks = doc.select("img[src]");
		System.out.println("开始链接：" + path);
		int count = 0;
		for (Element element : aLinks) {
			String url = element.attr("href");
			// 判断链接是否包含这两个头

			if (!url.contains("http://") && !url.contains("https://")) {
				// 不是则加上 例：<a href="xitongshow.php?cid=67&id=113" />
				// 则需要加上前缀 http://www.yada.com.cn/xitongshow.php?cid=67&id=113
				// 否则404
				url = path + url;
			}

//			// 如果文件中没有这个链接，而且链接中不包含javascript:则继续(因为有的是用js语法跳转)
			if (!readTxtFile(aLinkFile).contains(url) && !url.contains("javascript") && count < 3) {
				// 路径必须包含网页主链接--->防止爬入别的网站
				if (url.contains(path)) {
					// 判断该a标签的内容是文件还是子链接
					if (url.contains(".doc") || url.contains(".exl") || url.contains(".exe") || url.contains(".apk")
							|| url.contains(".mp3") || url.contains(".mp4")) {
						// 写入文件中，文件名+文件链接
						writeTxtFile(docLinkFile, element.text() + "\r\n\t" + url + "\r\n");
					} else {
						// 将链接写入文件
						writeTxtFile(aLinkFile, url + "\r\n");
//						System.out.println(webTrees.indexOf(count));
					
						webTrees.get(i).root.addChild(new WebNode(new WebPage(url)));
						sum++; // 链接总数+1
						count++;
						found = true;
					}
					System.out.println("這式子旺業"+"\t" + element.text() + "：\t" + url);
				}
			}
		}
		if (found == true)
			return url;
		else
			return null;
	}
	// 同时抓取该页面图片链接
	// for (Element element : imgLinks) {
	// String srcStr = element.attr("src");
	// if (!srcStr.contains("http://") && !srcStr.contains("https://")) {// 没有这两个头
	// srcStr = Spider.results + srcStr;
	// }
	// if (!readTxtFile(imgLinkFile).contains(srcStr)) {
	// // 将图片链接写进文件中
	// writeTxtFile(imgLinkFile, srcStr + "\r\n");
	// }
	// }
	// num++;
	// if (sum > num) {
	// getAllLinks(getFileLine(aLinkFile, num));
	// }
	// }

	public static String readTxtFile(File file) {
		String result = ""; // 读取結果
		String thisLine = ""; // 每次读取的行
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {

				while ((thisLine = reader.readLine()) != null) {
					result += thisLine + "\n";
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void writeTxtFile(File file, String urlStr) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(urlStr);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getFileLine(File file, int num) {
		String thisLine = "";
		int thisNum = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while ((thisLine = reader.readLine()) != null) {
				if (num == thisNum) {
					return thisLine;
				}
				thisNum++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static int getFileCount(File file) {
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while (reader.readLine() != null) { // 遍历文件行
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public static class Sort {

		public static void sort(ArrayList<WebTree> number, int left, int right) {
			if (left < right) {
				int q = partition(number, left, right);
				sort(number, left, q - 1);
				sort(number, q + 1, right);
			}
		}

		private static int partition(ArrayList<WebTree> number, int left, int right) {
			int i = left - 1;
			for (int j = left; j < right; j++) {
				if (number.get(j).treescore >= number.get(right).treescore) {
					i++;
					swap(number, i, j);
				}
			}
			swap(number, i + 1, right);
			return i + 1;
		}

		private static void swap(ArrayList<WebTree> number, int i, int j) {
			WebTree tj = number.get(j);
			int indexOfJ = number.indexOf(number.get(j));
			WebTree ti = number.remove(i);
			number.add(i, tj);
			number.remove(indexOfJ);
			number.add(indexOfJ, ti);
		}
	}
}