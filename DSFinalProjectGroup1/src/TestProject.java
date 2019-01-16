
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
		}
		
		//Call Google Search Result
		ArrayList<WebTree> query = new GoogleQuery(request.getParameter("keyword")).query();
		
		//Calculate Score and Do the Sorting thing
		HashMap<String, String> sorted = new HashMap<String, String>();
		
		for (WebTree webTree : sortGoogleQuery.sort(query)) {
			sorted.put(webTree.treename, webTree.treeurl);
		}
		String[][] s = new String[sorted.size()][2];
		request.setAttribute("query", s);
		int num = 0;
		for (Map.Entry<String, String> entry : sorted.entrySet()) {
			s[num][0] = entry.getKey();
			s[num][1] = entry.getValue();
			num++;
		}
		request.getRequestDispatcher("googleitem.jsp").forward(request, response);
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
