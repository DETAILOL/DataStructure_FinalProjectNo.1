import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WordCounter {
	private String urlStr;
	private String content;

	public WordCounter(String urlStr) {
		this.urlStr = urlStr;
	}

	private String fetchContent() throws IOException {
		URL url = new URL(this.urlStr);
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String retVal = " ";
		String line = null;

		while ((line = br.readLine()) != null) 
		{
			retVal = retVal + line + "\n";
		}

		return retVal;
	}

	public int countKeyword(String keyword) throws IOException {
		int count = 0;
		if (content == null) {
			content = fetchContent();
		}
		content = content.toUpperCase();
		keyword = keyword.toUpperCase();


		int T = 0;
		int index;
		while ((index = content.indexOf(keyword, T)) >= 0) {
			count++;
			T = index + keyword.length();
		}
		return count;
	}
}