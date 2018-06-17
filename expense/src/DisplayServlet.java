

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;



import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

 
@WebServlet(name = "DisplayServlet", urlPatterns = "/api/displaytable")
public class DisplayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    @Resource(name = "jdbc/expensedb")
    private DataSource dataSource;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain"); 
		
        try {
        	
        	 
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Declare our statement
            Statement statement = dbcon.createStatement();
            
       
            
            String select_query = "SELECT id, e_date, e_value, e_reason\r\n" + 
            		"FROM expense;"; 
            
            // execute
            ResultSet rs = statement.executeQuery(select_query);
            
            
            
            JsonArray jsonArray = new JsonArray();

            while (rs.next()) {
            	String id = rs.getString("id");
            	String e_date = rs.getString("e_date");
            	String e_value = rs.getString("e_value");
            	String e_reason = rs.getString("e_reason");
            	
            	JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", id);
                jsonObject.addProperty("e_date", e_date);
                jsonObject.addProperty("e_value", e_value);
                jsonObject.addProperty("e_reason", e_reason);
                jsonArray.add(jsonObject);
            }


            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);
            
            rs.close();
            statement.close();
            dbcon.close();
          


        } catch (Exception e) {
        	System.out.println(e);
			// write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());

			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);

        }
 
        out.close();

    }
 
}
