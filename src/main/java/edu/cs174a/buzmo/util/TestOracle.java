import java.sql.*;


public class TestOracle {
	static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	static final String DB_URL = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
	static final String USER = "salolivares";
	static final String PASS = "480";

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		try{
	  		//STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

	  		//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

	  		//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql = "SELECT * FROM rmr.Orders";
			ResultSet rs = stmt.executeQuery(sql);

	  		//STEP 5: Extract data from result set
			while(rs.next()){

			}
	  		//STEP 6: Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
		} catch(SQLException se){
	  		//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
	  		//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
	  		//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			} catch(SQLException se2){

	  		}
		  	try {
		  		if(conn!=null)
		  			conn.close();
		  	} catch(SQLException se){
		  		se.printStackTrace();
		  	}
   		}
   	System.out.println("Goodbye!");
	}
}


