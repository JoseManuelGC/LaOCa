package edu.uclm.esi.tysweb.laoca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Pool {
	private ConcurrentLinkedQueue<Connection> libres;
	private ConcurrentLinkedQueue<Connection> usadas;
	
	Pool(int numeroDeConexiones) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.exit(-1);
		}
		String url="jdbc:mysql://alarcosj.esi.uclm.es:3306/listadelacompra";
		this.libres=new ConcurrentLinkedQueue<>();
		this.usadas=new ConcurrentLinkedQueue<>();
		for (int i=0; i<numeroDeConexiones; i++) {
			Connection bd;
			try {
				bd = DriverManager.getConnection(url, "listadelacompra", "");
				this.libres.add(bd);
				System.out.println("Conexión establecida: " + i);
			} catch (SQLException e) {
				System.out.println("Falló con la conexión: " + i);
				e.printStackTrace();
				break;
			}
		}
	}

	public Connection getBD() throws Exception {
		if (this.libres.size()==0)
			throw new Exception("No hay conexiones libres");
		Connection bd=this.libres.poll();
		this.usadas.offer(bd);
		return bd;
	}

	public void close(Connection bd) {
		this.usadas.remove(bd);
		this.libres.offer(bd);
	}
}
