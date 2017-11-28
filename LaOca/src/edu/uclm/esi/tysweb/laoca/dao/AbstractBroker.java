package edu.uclm.esi.tysweb.laoca.dao;

import java.sql.Connection;

public abstract class AbstractBroker {
	public abstract Connection getBD() throws Exception;

	public abstract void close(Connection bd) throws Exception;
	
}
