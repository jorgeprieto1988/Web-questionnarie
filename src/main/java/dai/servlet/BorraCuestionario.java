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
import com.google.appengine.repackaged.com.google.gson.JsonArray;
import com.google.appengine.repackaged.com.google.gson.JsonObject;

/**
 * Servlet implementation class BorraCuestionario
 */
public class BorraCuestionario extends HttpServlet {
	private DatastoreService datastore;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BorraCuestionario() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String tema = request.getParameter("tema");
		datastore = DatastoreServiceFactory.getDatastoreService();
		if(tema == "")
		{
			 JsonObject errormessage, errorexiste;
			 errormessage = new JsonObject();
			 errorexiste = new JsonObject();
			 
			 errormessage.addProperty("message", "se esperaba el parámetro tema");
			 errorexiste.add("error",errormessage);
			 response.getWriter().write(errorexiste.toString());
		
		}
		else
		{
			String username = request.getUserPrincipal().getName();
			Key userkey =  KeyFactory.createKey("Usuario", username);
			 Filter filtro = new FilterPredicate("tema", FilterOperator.EQUAL, tema);
			 Query busqueda = new Query("Cuestionario").setAncestor(userkey).setFilter(filtro);
			 List<Entity> results =
	         datastore.prepare(busqueda.setKeysOnly()).asList(FetchOptions.Builder.withDefaults());
			 
			 if(results.isEmpty() == false)
			 {
				 Key keycuestionario = results.get(0).getKey();
				 datastore.delete(keycuestionario);
				 
				 JsonArray arrayvacio = new JsonArray();
				 JsonObject resultok = new JsonObject();
				 resultok.add("result", arrayvacio);
				 
				 response.getWriter().write(resultok.toString());				 
			 }
			 else
			 {
				 JsonObject errormessage, errorexiste;
				 errormessage = new JsonObject();
				 errorexiste = new JsonObject();
				 
				 errormessage.addProperty("message", "el tema "+ tema + " no existe en la base de datos");
				 errorexiste.add("error",errormessage);
				 response.getWriter().write(errorexiste.toString());				 				
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
