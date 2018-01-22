package edu.uclm.esi.tysweb.laoca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.mongodb.MongoClient;

public class Pool {
	private ConcurrentLinkedQueue<MongoClient> libres;
	private ConcurrentLinkedQueue<MongoClient> usadas;
	
	public Pool(int numeroDeConexiones) {
		this.libres=new ConcurrentLinkedQueue<>();
		this.usadas=new ConcurrentLinkedQueue<>();
		for (int i=0; i<numeroDeConexiones; i++) {
			MongoClient conexion = new MongoClient("localhost", 27017);
			try {
				this.libres.add(conexion);
				System.out.println("Conexion establecida: " + i);
			} catch (Exception e) {
				System.out.println("Fallo con la conexion: " + i);
				e.printStackTrace();
				break;
			}
		}
	}

	public MongoClient getConnection() throws Exception {
		if (this.libres.size()==0)
			throw new Exception("No hay conexiones libres");
		MongoClient conexion = this.libres.poll();
		this.usadas.offer(conexion);
		return conexion;
	}

	public void close(MongoClient conexion) {
		this.usadas.remove(conexion);
		this.libres.offer(conexion);
	}
}
