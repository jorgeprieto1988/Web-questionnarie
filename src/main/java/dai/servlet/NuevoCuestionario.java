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
 * Servlet implementation class NuevoCuestionario
 */
public class NuevoCuestionario extends HttpServlet {
	 private DatastoreService datastore;
	 
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NuevoCuestionario() {
    	
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());	
		String tema = request.getParameter("tema");
		datastore = DatastoreServiceFactory.getDatastoreService();
		String username = request.getUserPrincipal().getName();
		Key userkey =  KeyFactory.createKey("Usuario", username);
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
			 Filter filtro = new FilterPredicate("tema", FilterOperator.EQUAL, tema);
			 Query busqueda = new Query("Cuestionario").setAncestor(userkey).setFilter(filtro);
			 List<Entity> results =
	         datastore.prepare(busqueda.setKeysOnly()).asList(FetchOptions.Builder.withDefaults());
			 
			 if(results.isEmpty() == false)
			 {
				 JsonObject errormessage, errorexiste;
				 errormessage = new JsonObject();
				 errorexiste = new JsonObject();
				 
				 errormessage.addProperty("message", "el tema "+ tema + " ya existe en la base de datos");
				 errorexiste.add("error",errormessage);
				 response.getWriter().write(errorexiste.toString());
			 }
			 else
			 {
				 Entity cuestionario = new Entity("Cuestionario", userkey);
				 cuestionario.setProperty("tema", tema);
				 datastore.put(cuestionario);
				 
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
