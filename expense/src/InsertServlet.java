import com.google.gson.JsonArray;

import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;



 
@WebServlet(name = "InsertServlet", urlPatterns = "/api/insert")
public class InsertServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    

    private double checkvalidvalue(String input) {
    	
    	
    	double value = -1;
    	try {
    		  value = Double.parseDouble(input);    		  	        		  
    		} catch (Exception e) {
    		  System.out.println(e); 
    	}
    	
    	return value;
    }
 
    @Resource(name = "jdbc/expensedb")
    private DataSource dataSource;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain"); 

        
        String input_date = request.getParameter("date");
        double input_value = checkvalidvalue(request.getParameter("value"));
        String input_reason = request.getParameter("reason");
        
        
        
        JsonObject jsonObject = new JsonObject();
        if (input_date.isEmpty() || input_value < 0 || input_reason.isEmpty()) {
        	jsonObject.addProperty("valid", "invalid");
        	out.write(jsonObject.toString());
        	return;
        }else {
			jsonObject.addProperty("valid", "valid");
			out.write(jsonObject.toString());
        }
                
        //System.out.println(input_date);
        //System.out.println(input_value);
        //System.out.println(input_reason);
        //System.out.println("----------------------------------------");
       
        try {
        	
 
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();
            System.out.println("connection success");
            // Declare our statement
          
            String insert_query = "INSERT INTO expense (e_value, e_date, e_reason) VALUES (?,?,?);";
            PreparedStatement preparedStmt = dbcon.prepareStatement(insert_query);
            preparedStmt.setDouble(1, input_value);
            preparedStmt.setDate(2, java.sql.Date.valueOf(input_date));
            preparedStmt.setString(3, input_reason);
            preparedStmt.execute();
            preparedStmt.close();
            dbcon.close();


        } catch (Exception e) {
        	System.out.println(e);
			// write error message JSON object to output
			JsonObject errorobject = new JsonObject();
			errorobject.addProperty("errorMessage", e.getMessage());
			out.write(errorobject.toString());

			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);

        }
 
        out.close();

    }
}
