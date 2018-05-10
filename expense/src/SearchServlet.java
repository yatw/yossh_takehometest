import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

//
@WebServlet(name = "SearchServlet", urlPatterns = "/api/search")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain"); 

        
        String title = request.getParameter("title");
        String year = request.getParameter("year");
        String director = request.getParameter("director");
        String star = request.getParameter("star");
        String input_max = request.getParameter("max");
        
        String max = "100"; //default 100 on a page
        if ("Show 25 a page".equals(input_max)) {
        	max = "25";
        	
        }else if ("Show 50 a page".equals(input_max)){
        	max = "50";
        	
        }else if ("Show 75 a page".equals(input_max)){
        	max = "75";
        }
         
         
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", title);
        jsonObject.addProperty("year", year);
        jsonObject.addProperty("director", director);
        jsonObject.addProperty("star", star);
        jsonObject.addProperty("max", max);
        
        response.getWriter().write(jsonObject.toString());
        out.close();

    }
}
