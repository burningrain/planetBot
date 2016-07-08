package com.github.br.starmarines.gamecore;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;
import org.jooq.Result;
import org.jooq.impl.DSL;



import java.sql.Connection;
import java.sql.DriverManager;

import javax.sql.DataSource;

import static org.jooq.db.generate.Tables.*;

/**
 * Created by user on 01.07.2015.
 */
public class Main {

//    public static void main(String[] args) {
//        try {
//        	testFlyWay();
//            testQueryingAfterMigration();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    
//    public static void testFlyWay(){    	
//		String dbUsername = "sa";
//		String dbPassword = "";
//		String dbUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=5;";
//		DataSource dataSource = JdbcConnectionPool.create(dbUrl, dbUsername, dbPassword);
//
//		Flyway flyway = new Flyway();
//		flyway.setSchemas("FLYWAY_TEST");
//		flyway.setLocations("filesystem:src/main/resources/db/migration");
//		flyway.setDataSource(dataSource);
//		flyway.clean();
//		flyway.init();
//
//		flyway.setValidateOnMigrate(true);
//		flyway.migrate();		
//    }
//
//    public static void testQueryingAfterMigration() throws Exception {
//        try (Connection c = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "")) {
//            Result<?> result =
//                    DSL.using(c)
//                            .select(
//                                    AUTHOR.FIRST_NAME,
//                                    AUTHOR.LAST_NAME,
//                                    BOOK.ID,
//                                    BOOK.TITLE
//                            )
//                            .from(AUTHOR)
//                            .join(BOOK)
//                            .on(AUTHOR.ID.eq(BOOK.AUTHOR_ID))
//                            .orderBy(BOOK.ID.asc())
//                            .fetch();
//
//            System.out.println(result.size());
//            System.out.println(result.getValues(BOOK.TITLE));
//        }
//    }


}
