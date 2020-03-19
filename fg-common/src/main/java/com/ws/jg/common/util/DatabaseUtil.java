package com.ws.jg.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Sudiptasish Chanda
 */
public final class DatabaseUtil {
	
	//private static final String db_url = "jdbc:odbc:GAME_DSN_ACCDB";
    //private static final String db_url = "jdbc:derby://localhost:1527/game_db;user=sysems_t_1;password=welcome1";
    private static final String db_url = "jdbc:derby://localhost:1527/game_db";
	private static final String db_user = "flash";
	private static final String db_password = "flash";

	static {
		try {
			//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		    //Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		    Class.forName("org.apache.derby.jdbc.ClientDriver");
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Return a new database connection.
	 * This API will throw SQLException, if it is unable to obtain a
	 * connection from the underlying db.
	 * 
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
	    Connection conn = DriverManager.getConnection(db_url);
	    //conn.setAutoCommit(false);
	    return conn;
	}
	
	/**
	 * Close the specific connection.
	 * @param connection
	 */
	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * API to close the statement.
	 * It may include any statement (PReparedstatement, CallableStatement, etc).
	 * 
	 * @param statement
	 */
	public static void closeStatement(Statement statement) {
	    try {
            if (statement != null && !statement.isClosed()) {
                statement.close();
			}
		}
        catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * API to close the result set.
	 * @param resultSet
	 */
	public static void closeResultSet(ResultSet resultSet) {
	    try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
			}
		}
        catch (SQLException e) {
            e.printStackTrace();
        }
	}
}
