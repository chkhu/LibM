To compile and run this project, you should first:

1. Have MySQL installed.(https://dev.mysql.com/downloads/installer/)

2. have JDK installed on your machine (https://www.oracle.com/java/technologies/downloads/)

3. have JDBC for MySQL installed (https://dev.mysql.com/downloads/connector/j/)

4. check the sql connector's absolute location
   It should look like:
	/Library/Java/Extensions/mysql-connector-j-8.0.32.jar
	(on Mac or linux)

5. edit the ``LibManager.java`` file to fit into your MySQL connection
	```
	    	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";//JDBC driver
      	private static final String DB_URL = "jdbc:mysql://localhost:3306/library"; //replace
	    	private static final String USER = "root";
    		private static final String PASSWORD = "*******"; //replace
	```

6. cd to workspace ~/srcs and use the command below
	```
	javac LibManager.java;java -cp /Library/Java/Extensions/mysql-connector-j-8.0.32.jar:.LibManager
	```
	you can replace the location depending on your situation

7. Hit enter and you shall see the initial prompts.
