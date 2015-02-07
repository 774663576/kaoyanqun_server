package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class DBConnector {
	final static Logger log = Logger.getLogger("db");
	static String baseName = "db.config";
	static ResourceBundle bundle = ResourceBundle.getBundle(baseName);
	final static String url = bundle.getString("CONNECTION_URL");
	final static String user = bundle.getString("CONNECTION_USERNAME");
	final static String password = bundle.getString("CONNECTION_PASSWORD");
	public static String savepath = bundle.getString("SAVE_PATH");
	public static String picurl = bundle.getString("URL_PATH");
	static {
		try {
			Class.forName(bundle.getString("DRIVER_CLASS"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Connection Getconnect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	// public static ResultSet getResult(String sql){
	// Connection conn=DBConnector.Getconnect();
	// Statement stmt=null;
	// ResultSet rs = null;
	// if(conn!=null){
	// try {
	// stmt = conn.createStatement();
	// rs=stmt.executeQuery(sql);
	// log.debug("execute sql  "+sql);
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	// return rs;
	// }
	public static void deal(String sql) {
		Connection conn = DBConnector.Getconnect();
		Statement stmt = null;
		if (conn != null) {
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
				log.debug("execute sql  " + sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				closeStatement(stmt);
				closeConn(conn);
			}

		}
	}

	public static boolean getCount(String sql, Object[] params, int[] types,
			int op) {
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < types.length; i++) {
				if (types[i] == 0) {
					pstmt.setString(i + 1, String.valueOf(params[i]));
				} else if (types[i] == 1) {
					pstmt.setInt(i + 1, (Integer) params[i]);
				} else {
					pstmt.setDouble(i + 1, (Double) params[i]);
				}
			}
			if (op == 0) {
				int count = pstmt.executeUpdate();
				if (count > 0) {
					return true;
				}
			} else {
				if (pstmt.executeQuery().next()) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnector.closePreparedStatement(pstmt);
			DBConnector.closeConn(conn);
		}
		return false;

	}

	public static ResultSet getResultWitParams(String sql, Object[] params,
			int[] types) {
		Connection conn = DBConnector.Getconnect();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < types.length; i++) {
				if (types[i] == 0) {
					pstmt.setString(i + 1, String.valueOf(params[i]));
				} else if (types[i] == 1) {
					pstmt.setInt(i + 1, (Integer) params[i]);
				} else {
					pstmt.setDouble(i + 1, (Double) params[i]);
				}
			}
			rs = pstmt.executeQuery();
			log.debug("Execute sql:" + sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public static void closeConn(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void closePreparedStatement(PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void closeResult(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void closeAll(ResultSet rs, PreparedStatement pstmt,
			Statement stmt, Connection conn) {
		closeResult(rs);
		closePreparedStatement(pstmt);
		closeStatement(stmt);
		closeConn(conn);
	}
public static void main(String[] args) {
	Getconnect();
}
	

}
