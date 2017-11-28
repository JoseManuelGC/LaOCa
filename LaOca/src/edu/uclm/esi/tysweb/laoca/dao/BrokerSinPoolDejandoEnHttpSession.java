package edu.uclm.esi.tysweb.laoca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BrokerSinPoolDejandoEnHttpSession {
	private String url;

	private BrokerSinPoolDejandoEnHttpSession() {
		this.url="jdbc:mysql://alarcosj.esi.uclm.es:3306/listadelacompra";
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.exit(-1);
		}
	}
	
	private static class BrokerHolder {
		static BrokerSinPoolDejandoEnHttpSession singleton=new BrokerSinPoolDejandoEnHttpSession();
	}
	
	public static BrokerSinPoolDejandoEnHttpSession get() {
		return BrokerHolder.singleton;
	}

	public Connection getBD() {
			Connection bd;
			try {
				bd = DriverManager.getConnection(url, "listadelacompra", "");
				return bd;
			} catch (SQLException e) {
				System.out.println("Falló la conexión");
				e.printStackTrace();
				return null;
			}
	}

	public void close(Connection bd) throws SQLException {
		bd.close();
	}
}
