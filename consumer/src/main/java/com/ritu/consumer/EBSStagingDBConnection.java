package com.ritu.consumer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;  

public class EBSStagingDBConnection {

	public static void main(String[] args) {
		String url = Constants.DBurl;  // db instance as service
		String user = Constants.DBuser;
		String pwd = Constants.DBpwd;
		EBSStagingDBConnection cet = new EBSStagingDBConnection();
		Connection connection = cet.getDBConnection(url ,user,pwd);
		try {

			Map<String,String>  ProjectTable = cet.getProjectTableData(connection); // not working 19 july
			//cet.setProjectTableData(connection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Connection getDBConnection(String url, String user, String pwd) {
		System.out.println("-------- Oracle JDBC Connection Testing ------");
		Connection connection = null;
		try {
			Class.forName(Constants.DBdriver);
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			//return null;
		}
		System.out.println("Oracle JDBC Driver Registered!");

		try {

			//			connection = DriverManager.getConnection(
			//					"jdbc:oracle:thin:@localhost:1521:xe", "system", "password");

			connection = DriverManager.getConnection(url ,user,pwd);
			//return connection;
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			//return null;
		}
		if (connection != null) {
			System.out.println("You made it, take control your database now!");
			return connection;
		} else {
			System.out.println("Failed to make connection!");
			return null;
		}
	}

	public  Map<String,String>  getProjectTableData(Connection con)
			throws SQLException {

		Statement stmt = null;
		String query =
				"select * from AARS_P6_EBS_INTE";
		Map<String,String> tabledata = new HashMap<String,String>();
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				String BONAME = rs.getString("BONAME");
				String ID = rs.getString("ID");
				String NAME = rs.getString("NAME");
				String PROJECTSTARTDATE = rs.getString("PROJECTSTARTDATE");
				String PROJECTFINISHDATE = rs.getString("PROJECTFINISHDATE");
				String EPS = rs.getString("EPS");
				String STATUS = rs.getString("STATUS");

				tabledata.put("BONAME", BONAME);
				tabledata.put("ID", ID);
				tabledata.put("NAME", NAME);
				tabledata.put("PROJECTSTARTDATE", PROJECTSTARTDATE);
				tabledata.put("PROJECTFINISHDATE", PROJECTFINISHDATE);
				tabledata.put("EPS", EPS);
				tabledata.put("STATUS", STATUS);

			}
			System.out.println("tabledata:"+tabledata);
		} catch (SQLException e ) {
			// JDBCTutorialUtilities.printSQLException(e);
			e.printStackTrace();
		} finally {
			if (stmt != null) { stmt.close(); }
		}

		return tabledata;
	}

	public void setProjectTableData(Connection connection, Map<String, String> p6datahashmap) throws SQLException {

		Statement statement = null;

		String insertTableSQL = "INSERT INTO AARS_P6_EBS_INTE"
				+ "(BONAME, ID, NAME, PROJECTSTARTDATE,PROJECTFINISHDATE,EPS,STATUS) " + "VALUES "
				+ "("
				+ "'"+p6datahashmap.get("BONAME") + "'" +","
				+ "'"+p6datahashmap.get("ID") + "'"+","
				+ "'"+p6datahashmap.get("NAME") + "'"+","
				+ "'"+p6datahashmap.get("PROJECTSTARTDATE") + "'"+","
				+ "'"+p6datahashmap.get("PROJECTFINISHDATE") + "'"+","
				+ "'"+p6datahashmap.get("EPS") + "'"+","
				+ "'"+p6datahashmap.get("STATUS")+ "'" 
				+")";

		try {
			statement = connection.createStatement();

			System.out.println(insertTableSQL);

			// execute insert SQL stetement
			statement.executeUpdate(insertTableSQL);

			System.out.println("Record is inserted into DBUSER table!");

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (statement != null) {
				statement.close();
			}

			if (connection != null) {
				connection.close();
			}

		}

	}


}
