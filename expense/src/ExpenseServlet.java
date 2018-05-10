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

        
        String date = request.getParameter("date");
        float value = Float.parseFloat(request.getParameter("value")); 
        String reason = request.getParameter("reason");
        System.out.println(date);
        System.out.println(value);
        System.out.println(reason);
        
        try {
        	
        	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date parsed = format.parse("20110210");
            java.sql.Date sqldate = new java.sql.Date(parsed.getTime());
        	
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();
            System.out.println("connection success");
            // Declare our statement
            Statement statement = dbcon.createStatement();
            
            String query = "INSERT INTO expense (e_value, e_date, e_reason) VALUES (?,?,?);";
            PreparedStatement preparedStmt = dbcon.prepareStatement(query);
            preparedStmt.setFloat(1, value);
            preparedStmt.setDate(2, sqldate);
            preparedStmt.setString(3, reason);
            preparedStmt.execute();
            dbcon.close();
            System.out.println("statmentdone");

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
