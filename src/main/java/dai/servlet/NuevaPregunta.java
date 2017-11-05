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
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.repackaged.com.google.gson.JsonArray;
import com.google.appengine.repackaged.com.google.gson.JsonObject;

/**
 * Servlet implementation class NuevaPregunta
 */
public class NuevaPregunta extends HttpServlet {
	 private DatastoreService datastore;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NuevaPregunta() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String tema = request.getParameter("tema");
		String pregunta = request.getParameter("pregunta");
		String respuesta = request.getParameter("respuesta");
		datastore = DatastoreServiceFactory.getDatastoreService();
		String username = request.getUserPrincipal().getName();
		Key userkey =  KeyFactory.createKey("Usuario", username);
		if(pregunta == "")
		{
			 JsonObject errormessage, errorexiste;
			 errormessage = new JsonObject();
			 errorexiste = new JsonObject();
			 
			 errormessage.addProperty("message", "se esperaba el parámetro pregunta");
			 errorexiste.add("error",errormessage);
			 response.getWriter().write(errorexiste.toString());
		
		}
		else
		{
			 Filter filtro = CompositeFilterOperator.and(
					 new FilterPredicate("tema", FilterOperator.EQUAL, tema),
					 new FilterPredicate("pregunta", FilterOperator.EQUAL, pregunta));
			 
			 Query busqueda = new Query("Pregunta").setAncestor(userkey).setFilter(filtro);
			 List<Entity> results =
	         datastore.prepare(busqueda.setKeysOnly()).asList(FetchOptions.Builder.withDefaults());
			 
			 if(results.isEmpty() == false)
			 {
				 JsonObject errormessage, errorexiste;
				 errormessage = new JsonObject();
				 errorexiste = new JsonObject();
				 
				 errormessage.addProperty("message", "la pregunta "+ pregunta + " ya existe en la base de datos");
				 errorexiste.add("error",errormessage);
				 response.getWriter().write(errorexiste.toString());
			 }
			 else
			 {
				 Entity savepregunta = new Entity("Pregunta", userkey);
				 savepregunta.setProperty("pregunta", pregunta);
				 savepregunta.setProperty("respuesta", respuesta);
				 savepregunta.setProperty("tema", tema);
				 datastore.put(savepregunta);
				 
				 JsonArray arrayvacio = new JsonArray();
				 JsonObject resultok = new JsonObject();
				 resultok.add("result", arrayvacio);
				 
				 response.getWriter().write(resultok.toString());
			 }
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
