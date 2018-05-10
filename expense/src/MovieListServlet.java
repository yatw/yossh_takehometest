
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
import java.util.*;

//
@WebServlet(name = "MovieListServlet", urlPatterns = "/api/movielist")
public class MovieListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();
            

            // Declare our statement
            Statement statement = dbcon.createStatement();
            
            String input_title = request.getParameter("title");
            String input_year = request.getParameter("year");
            String input_director = request.getParameter("director");
            String input_star = request.getParameter("star");
            String input_genre = request.getParameter("genre");
            String sortby = request.getParameter("sortby");

            int page = Integer.parseInt(request.getParameter("page"));
            int max = Integer.parseInt(request.getParameter("max"));
            
            
            String query = "SELECT movies.id, title, year, director, rating, GROUP_CONCAT(DISTINCT genres.name) AS genre_list, GROUP_CONCAT(stars.name) AS star_list, GROUP_CONCAT(stars.id) AS starid_list\r\n" + 
            		"FROM (movies left join ratings ON movies.id = ratings.movieId), genres , genres_in_movies , stars, stars_in_movies\r\n" + 
            		"WHERE movies.id = genres_in_movies.movieId\r\n" + 
            		"AND genres.id = genres_in_movies.genreId \r\n" + 
            		"AND movies.id = stars_in_movies.movieId \r\n" + 
            		"AND stars.id = stars_in_movies.starId\r\n";

            if (input_title != "") { 	
            	query += String.format("AND title like '%s'\r\n",input_title);
            }
            if (input_year != "") {
            	query += String.format("AND year like %s\r\n", input_year);  
            }
            if (input_director != "") {
            	query += String.format("AND director like '%s'\r\n", input_director);  
            }            
            if (input_star != "") {
            	query += String.format("AND stars.name like '%s'\r\n", input_star);   
            }            
            
            if (input_genre != "") {
            	query += String.format("AND genres.name LIKE '%s'\r\n", input_genre);
            }
                        
    		query +="GROUP BY title\r\n";
    		
    		if ("ratingdesc".equals(sortby)) {
    			query += "ORDER BY ratings.rating DESC,title\r\n";
    		}else if ("ratingasc".equals(sortby)) {
    			query += "ORDER BY ratings.rating ASC,title\r\n";
    		}else if ("titleasc".equals(sortby)) {
    			query += "ORDER BY title \r\n";  // automatically ASC
    		}else {
    			query += "ORDER BY title DESC\r\n";
    		}
    		
    		
    		 
    		query += String.format("LIMIT %s;", String.valueOf(page*max+1));  // only get as much as to the current page
    																		// get one more to see if this is last page
    		System.out.println(query);

            // Perform the query
            ResultSet rs = statement.executeQuery(query);
        
            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            
            
            // calculate the range of movies to display
            // loop through the previous pages but dont show them
            int count = 1;
            int ignore = max *(page-1);
            int show_movie = 0;
            boolean last_page = true;
            while (rs.next()) {
            	
            	if (count <= ignore) {
            		count++;
            		continue; // ignore everything in the previous pages
            	}
            	
            	if (show_movie < max) {
            		show_movie++; 
	                String movie_id = rs.getString("movies.id");
	                String title = rs.getString("title");
	                String year = rs.getString("year");
	                String director = rs.getString("director");
	                String rating = rs.getString("rating");
	                if(rs.wasNull())
	                {
	                	rating = "0";
	                }
	                String genre_list = rs.getString("genre_list");
	                String star_list = rs.getString("star_list");
	                String starid_list = rs.getString("starid_list");
	                // Create a JsonObject based on the data we retrieve from rs
	                JsonObject jsonObject = new JsonObject();
	                jsonObject.addProperty("count", String.valueOf(count++));
	                jsonObject.addProperty("movie_id", movie_id);
	                jsonObject.addProperty("title", title);
	                jsonObject.addProperty("year", year);
	                jsonObject.addProperty("director", director);
	                jsonObject.addProperty("rating", rating);
	                jsonObject.addProperty("genre_list", genre_list);
	                jsonObject.addProperty("star_list", star_list);
	                jsonObject.addProperty("starid_list", starid_list);
	
	                jsonArray.add(jsonObject);
            	}else {
            		// there are more movie to show than max
            		last_page = false;
            	}
            }
            
            //System.out.println(last_page);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("last_page", last_page);
            jsonArray.add(jsonObject);
            
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            rs.close();
            statement.close();
            dbcon.close();
        } catch (Exception e) {
        	
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
