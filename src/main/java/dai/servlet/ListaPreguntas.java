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
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.repackaged.com.google.gson.JsonArray;
import com.google.appengine.repackaged.com.google.gson.JsonObject;

/**
 * Servlet implementation class ListaPreguntas
 */
public class ListaPreguntas extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DatastoreService datastore;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListaPreguntas() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		datastore = DatastoreServiceFactory.getDatastoreService();
		String tema = request.getParameter("tema");
		String username = request.getUserPrincipal().getName();
		Key userkey =  KeyFactory.createKey("Usuario", username);
		Filter filtro = new FilterPredicate("tema", FilterOperator.EQUAL, tema);
		Query busqueda = new Query("Pregunta").setAncestor(userkey).setFilter(filtro).addSort("pregunta", SortDirection.ASCENDING);		
		List<Entity> results = datastore.prepare(busqueda).asList(FetchOptions.Builder.withDefaults());
		JsonArray arraytemas = new JsonArray();
		JsonObject resultok = new JsonObject();
		 
		System.out.println("tamaño " + results.size());

		for(int i=0; i < results.size(); i++)
		{
			Entity entitypregunta = results.get(i);
			JsonObject jsonpregunta = new JsonObject();
			
			jsonpregunta.addProperty("pregunta", entitypregunta.getProperty("pregunta").toString());
			jsonpregunta.addProperty("respuesta", entitypregunta.getProperty("respuesta").toString());
			jsonpregunta.addProperty("tema", entitypregunta.getProperty("tema").toString());
			
			arraytemas.add(jsonpregunta);	
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
