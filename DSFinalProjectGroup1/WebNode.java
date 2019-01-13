import java.io.IOException;
import java.util.ArrayList;

public class WebNode {
	public WebNode parent;
	public ArrayList<WebNode> children;
	public WebPage webPage;
	public double nodeScore;

	public WebNode(WebPage webPage) {
		this.webPage = webPage;
		this.children = new ArrayList<WebNode>();
	}

	public void setNodeScore(ArrayList<Keyword> keywords) throws IOException {
		webPage.setScore(keywords);
		this.nodeScore = webPage.score;

		for (WebNode child : children) {
			this.nodeScore += child.nodeScore;
		}
	}

	public void addChild(WebNode child) {
		this.children.add(child);
		child.parent = this;
	}

	public int getDepth() {
		int retVal = 1;
		WebNode currNode = this;

		while (currNode.parent != null) {
			retVal++;
			currNode = currNode.parent;
		}
		return retVal;
	}

}