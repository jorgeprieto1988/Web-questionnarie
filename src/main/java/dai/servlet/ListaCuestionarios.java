package dai.servlet;


import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.repackaged.com.google.gson.JsonArray;
import com.google.appengine.repackaged.com.google.gson.JsonObject;

/**
 * Servlet implementation class ListaCuestionarios
 */
public class ListaCuestionarios extends HttpServlet {
	private DatastoreService datastore;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListaCuestionarios() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		datastore = DatastoreServiceFactory.getDatastoreService();
		String username = request.getUserPrincipal().getName();
		Key userkey =  KeyFactory.createKey("Usuario", username);
		Query busqueda = new Query("Cuestionario").setAncestor(userkey).addSort("tema", SortDirection.ASCENDING);		
		List<Entity> results = datastore.prepare(busqueda).asList(FetchOptions.Builder.withDefaults());
		JsonArray arraytemas = new JsonArray();
		JsonObject resultok = new JsonObject();
		 

		for(int i=0; i < results.size(); i++)
		{
			Entity jsontema = results.get(i);
			arraytemas.add(jsontema.getProperty("tema").toString());

		}
		
		resultok.add("result", arraytemas);
		response.getWriter().write(resultok.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
