 package dai.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class MainServlet extends HttpServlet {
      /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
      public void doGet(HttpServletRequest req, HttpServletResponse resp)
                  throws IOException, ServletException {
        UserService userService = UserServiceFactory.getUserService();
        String thisURL = req.getRequestURI();
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        if (req.getUserPrincipal() != null) {
          String userName = req.getUserPrincipal().getName();
          DatastoreService datastore;
          datastore = DatastoreServiceFactory.getDatastoreService();
          
          Filter filtro = new FilterPredicate("Email", FilterOperator.EQUAL, userName);
          Query busqueda = new Query("Usuario").setFilter(filtro);
          List<Entity> results = datastore.prepare(busqueda.setKeysOnly()).asList(FetchOptions.Builder.withDefaults());
          
          if(results.size() == 0)
          {
        	  Key userKey = KeyFactory.createKey("Usuario", userName);
        	  Entity user = new Entity("Usuario", userKey);
        	  user.setProperty("Email", userName);
        	  datastore.put(user);
          }
          
          RequestDispatcher view = req.getRequestDispatcher("/main.jsp");
          req.setAttribute("logout", userService.createLogoutURL(thisURL));
          req.setAttribute("usuario", userName);
          view.forward(req, resp);
        } else {
          RequestDispatcher view = req.getRequestDispatcher("/welcome.jsp");
          req.setAttribute("login", userService.createLoginURL(thisURL));
          view.forward(req, resp);
        }
      }
    }