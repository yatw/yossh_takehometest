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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//
@WebServlet(name = "ExpenseServlet", urlPatterns = "/api/expense")
public class ExpenseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    @Resource(name = "jdbc/expensedb")
    private DataSource dataSource;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain"); 

        
        String input_date = request.getParameter("date");
        float input_value = Float.parseFloat(request.getParameter("value")); 
        String input_reason = request.getParameter("reason");
        //System.out.println(input_date);
        //System.out.println(input_value);
        //System.out.println(input_reason);
        //System.out.println("----------------------------------------");
        try {
        	
 
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();
            System.out.println("connection success");
            // Declare our statement
            Statement statement = dbcon.createStatement();
            
            String insert_query = "INSERT INTO expense (e_value, e_date, e_reason) VALUES (?,?,?);";
            PreparedStatement preparedStmt = dbcon.prepareStatement(insert_query);
            preparedStmt.setFloat(1, input_value);
            preparedStmt.setDate(2, java.sql.Date.valueOf(input_date));
            preparedStmt.setString(3, input_reason);
            preparedStmt.execute();
           
            
            
            String select_query = "SELECT id, e_date, e_value, e_reason\r\n" + 
            		"FROM expense;"; 
            
            ResultSet rs = statement.executeQuery(select_query);
            JsonArray jsonArray = new JsonArray();
            int count = 0;
            while (rs.next()) {
            	System.out.println(count++);
            	String id = rs.getString("id");
            	String e_date = rs.getString("e_date");
            	String e_value = rs.getString("e_value");
            	String e_reason = rs.getString("e_reason");
            	
            	System.out.println(e_reason);
            	
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
        	System.out.println("done");

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
