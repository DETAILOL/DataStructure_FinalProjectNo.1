
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleQuery {

	private String url;
	private static File aLinkFile;
	public ArrayList<WebTree> googleResult;

	public GoogleQuery(String searchKeyword) {
		this.url = "https://www.google.com.tw/search?q=" + searchKeyword + "&oe=utf8&num=100";
	}

	private String fetchContent() throws IOException {

		URLConnection connection = new URL(url).openConnection();
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

	public ArrayList<WebTree> query() throws IOException {

		googleResult = new ArrayList<>();
		aLinkFile = new File("C:\\Users\\Public\\Documents\\ALinks.txt");
		try {
			if (aLinkFile.exists())
				aLinkFile.delete();
			aLinkFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// if (this.content == null)
		// this.content = fetchContent();
		Elements lis = Jsoup.parse(fetchContent()).select("div.g");
		ArrayList<WebScore> rootUrls = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			try {
				String title = lis.get(i).select("h3.r").get(0).text();
				Element cite = lis.get(i).getElementsByTag("a").first();
				String citeUrl = "https://www.google.com.tw" + cite.attr("href");
				citeUrl = URLDecoder.decode(citeUrl.substring(citeUrl.indexOf('=') + 1, citeUrl.indexOf('&')), "UTF-8");
				// handle HTTP Error
				URL testurl = new URL(citeUrl);
				HttpURLConnection urlconnection = (HttpURLConnection) testurl.openConnection();
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
				rootUrls.add(new WebScore(title, citeUrl, 0));
				System.out.println(title + " 網址(" + citeUrl + ")");
				googleResult.add(new WebTree(new WebPage(citeUrl, title)));
				getAllLinks(citeUrl, i);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		return googleResult;
	}

	private void getAllLinks(String path, int i) throws IOException {

		Elements aLinks = Jsoup.parse(fetchContent()).select("a[href]");
		System.out.println("開始連結" + path);

		int count = 0;
		for (Element aLink : aLinks) {
			String childUrl = aLink.attr("href");
			if (!childUrl.contains("http://") && !childUrl.contains("https://"))
				childUrl = path + childUrl;
			if (!readTxtFile(aLinkFile).contains(childUrl) && !childUrl.contains("javascript") && count < 3) {
				if (childUrl.contains(path)) {
					if (!childUrl.contains(".doc") && !childUrl.contains(".exl") && !childUrl.contains(".exe")
							&& !childUrl.contains(".apk") && !childUrl.contains(".mp3") && !childUrl.contains(".mp4")) {
						writeTxtFile(aLinkFile, childUrl + "\r\n");
						googleResult.get(i).root.addChild(new WebNode(new WebPage(childUrl)));
						count++;
					}
					System.out.println("\t" + aLink.text() + ": \t" + childUrl);
				}
			}
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