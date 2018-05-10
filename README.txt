description of the problem and solution:
Build a web page that support a submit functionaily, which will insert the user input into MySQL database, and update the corresponding entries in a table.

Solution is to use html to construct the page, javascript binding to submit to Java, and JavaServlet conduct the JDBC (with MySQL).
In the JavaServlet, insert the new user entry to database, and then select all entry from the database and pass back to javascript via JSON.
Inside Javascript, unback the data from JSON, construct the rowHTML string and append to HTML.


Whether the solution focuses on back-end, front-end or if it's full stack.:
The solution is full stack, proving a decent user interface while making sure the backend can communicate with database correctly.

Reasoning behind your technical choices, including architectural:
I choose to use a front-end back-end architectural to keep things clear, another benefit is the front end can be easily modify without affecting the backend.
Great for applying the same backend to different applications.

Trade-offs you might have made, anything you left out, or what you might do differently if you were to spend additional time on the project:
I spend extra 6 minutes to fix a JSON bug which cause all the table entries to be UNDEFINED, it turned out to be a one line fix on parsing.
I left out the Date to be incorrect because I ran out of time. 
If I had extra time I would fix the incorrect date, and implement user input check.
Those were the two only things I couldn't finish in 3 hours.