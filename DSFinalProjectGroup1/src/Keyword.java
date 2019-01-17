public class Keyword {
	public String keyword;
	public double weight;
	
	public Keyword(String keyword, double weight) {
		this.keyword = keyword;
		this.weight = weight;
	}
	@Override
	public String toString() {
		return "[" + keyword + ", " + weight + "]";
	}
}