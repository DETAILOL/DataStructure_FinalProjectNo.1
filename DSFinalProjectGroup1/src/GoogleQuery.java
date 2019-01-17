import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleQuery {

	private ArrayList<WebTree> googleResult;
	private static File aLinkFile;
	private String url;
	private String content;

	public GoogleQuery(String searchKeyword) {
		this.url = "https://www.google.com.tw/search?q=" + searchKeyword + "&oe=utf8&num=100";
	}

	public ArrayList<WebTree> query() throws IOException {

		googleResult = new ArrayList<>();
		aLinkFile = new File("C:/Users/Public/Documents/ALinks.txt");

		try {
			if (aLinkFile.exists())
				aLinkFile.delete();
			aLinkFile.createNewFile();

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (this.content == null) {
			this.content = fetchContent();
		}
		Document document = Jsoup.parse(this.content);
		Elements lis = document.select("div.g");


		for (int i = 0; i <= 8; i++) {
			try {
				String title = lis.get(i).select("h3.r").get(0).text();
				Element cite = lis.get(i).getElementsByTag("a").first();
				String citeUrl = "https://www.google.com.tw" + cite.attr("href");
//				citeUrl = URLDecoder.decode(citeUrl.substring(citeUrl.indexOf('=') + 1, citeUrl.indexOf('&')), "UTF-8");

				 //handle HTTP Error
				 URL testurl = new URL(citeUrl);
				 HttpURLConnection urlconnection = (HttpURLConnection)
				 testurl.openConnection();
				 urlconnection.connect();
				
				 int statusCode = urlconnection.getResponseCode();
				
				 if (statusCode == HttpURLConnection.HTTP_UNAVAILABLE) {
				 System.out.println("Error, URL is unavaliable");
				 continue;
				 } else if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
				 System.out.println("Error, URL not found");
				 continue;
				 } else if (statusCode == HttpURLConnection.HTTP_FORBIDDEN) {
				 System.out.print("Error, forbidden URL");
				 continue;
				 }
				
				googleResult.add(new WebTree(new WebPage(citeUrl, title)));
				//Find Children
				getAllLinks(citeUrl, i);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return googleResult;
	}
	
	private String fetchContent() throws IOException {

		URL urlStr = new URL(url);
		URLConnection connection = urlStr.openConnection();
		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
		connection.connect();
		InputStream inputStream = connection.getInputStream();

		InputStreamReader inReader = new InputStreamReader(inputStream, "UTF8");
		BufferedReader bf = new BufferedReader(inReader);

		String retVal = "";
		String line = null;
		while ((line = bf.readLine()) != null) {
			retVal += line;
		}
		return retVal;
	}

	private void getAllLinks(String path, int i) throws IOException {
	 
		url = path;
		
		Elements aLinks = Jsoup.parse(fetchContent()).select("a[href]");
		System.out.println("開始連結" + path);

		int count = 1;
		try {
			for (Element aLink : aLinks) {
			
				String childUrl = aLink.attr("href");
				if (!childUrl.contains("http://") && !childUrl.contains("https://"))
					childUrl = "https:" + childUrl;

				if (!readTxtFile(aLinkFile).contains(childUrl) && !childUrl.contains("javascript") && count < 4) {
					
					if (childUrl.contains(path)) {
						if (!childUrl.contains(".doc") && !childUrl.contains(".exl") && !childUrl.contains(".exe")
								&& !childUrl.contains(".apk") && !childUrl.contains(".mp3") && !childUrl.contains(".mp4")) {
							writeTxtFile(aLinkFile, childUrl + "\r\n");
							System.out.println("\t"+count+". "+childUrl);
							googleResult.get(i).root.addChild(new WebNode(new WebPage(childUrl)));
							count++;
						}
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}
		
	}

	private static String readTxtFile(File file) {
		String result = ""; 
		String thisLine = ""; 
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

	private static void writeTxtFile(File file, String urlStr) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(urlStr);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}