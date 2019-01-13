
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet implementation class TestProject
 */
@WebServlet("/TestProject")
public class TestProject extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TestProject() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		if (request.getParameter("keyword") == null) {
			String requestUri = request.getRequestURI();
			request.setAttribute("requestUri", requestUri);
			request.getRequestDispatcher("Search.jsp").forward(request, response);
			return;
		} else {
			GoogleQuery google = new GoogleQuery(request.getParameter("keyword"));
			HashMap<String, String> query = google.query();
/**
			class WebData {
				public String keyword;
				public String uri;
				public double score;

				public void Webdata(String keyword, String uri, double score) {
					this.keyword = keyword;
					this.uri = uri;
					this.score = score;
				}
			}
**/
			String[][] s = new String[query.size()][2];
			request.setAttribute("query", s);
			int num = 0;
			for (Map.Entry<String, String> entry : query.entrySet()) {
				s[num][0] = entry.getKey();
				s[num][1] = entry.getValue();
				/**
				WebPage rootPage = new WebPage(entry.getValue(),entry.getKey());
				WebTree tree = new WebTree(rootPage);
				ArrayList<Keyword> keywords = new ArrayList<>();
				keywords.add(new Keyword("game", 3));
				tree.setPostOrderScore(keywords);
				**/
				num++;
			}

			request.getRequestDispatcher("googleitem.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
