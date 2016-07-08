package com.github.br.starmarines.gamecore.db;

import javax.sql.DataSource;

import org.jooq.SQLDialect;
import org.osgi.service.component.annotations.Component;


@Component(immediate = true, service = DataSourceConfig.class)
public class DataSourceConfig {
	
	
	
	
	
	private SQLDialect dialect;
	
	
	public SQLDialect getSqlDialect(){
		return SQLDialect.POSTGRES_9_4;
	}

	
	

}
