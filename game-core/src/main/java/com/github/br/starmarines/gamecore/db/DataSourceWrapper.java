package com.github.br.starmarines.gamecore.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Dictionary;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;


@Component(configurationPid = "datasource", immediate = true, service = DataSource.class)
public class DataSourceWrapper implements DataSource {

	private DataSource dataSource;

	private String dbUsername = "sa";
	private String dbPassword = "";
	private String dbUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MVCC=true";

	private boolean isFillDatabase = true;

	private void init() {
		dataSource = JdbcConnectionPool.create(dbUrl, dbUsername, dbPassword);
		if (isFillDatabase) {
			migrateDataBase();
		}
	}
	
	private void migrateDataBase() {		
		final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();		
	    Thread.currentThread().setContextClassLoader(Flyway.class.getClassLoader());
	    Flyway flyway = new Flyway();
	    try {			
			flyway.setSchemas("FLYWAY_TEST");
			flyway.setDataSource(dataSource);
			flyway.clean(); 
			
			flyway.setLocations("classpath:db/structure/postgre"); 
	        flyway.migrate();
			flyway.setValidateOnMigrate(true);	   			
	    } finally {
	        Thread.currentThread().setContextClassLoader(contextClassLoader);       
	    }
	}

	@Activate
	private void activate(ComponentContext ctx) {
		init();
	}

	@Modified
	private void updateConf(ComponentContext ctx) {
		Dictionary<String, Object> dict = ctx.getProperties();
		dbUsername = (String) dict.get("db.username");
		dbPassword = (String) dict.get("db.password");
		dbUrl = (String) dict.get("db.url");
		isFillDatabase = (boolean) dict.get("db.fillDatabase");
		
		init();
	}	
	

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return dataSource.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		dataSource.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		dataSource.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return dataSource.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return dataSource.getParentLogger();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return dataSource.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return dataSource.isWrapperFor(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		return dataSource.getConnection(username, username);
	}

}
