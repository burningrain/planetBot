package com.br;

import org.jooq.Result;
import org.jooq.impl.DSL;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.jooq.db.generate.Tables.*;

/**
 * Created by user on 01.07.2015.
 */
public class AfterMigrationTest {

    @Test
    public void testQueryingAfterMigration() throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "")) {
            Result<?> result =
                    DSL.using(c)
                            .select(
                                    AUTHOR.FIRST_NAME,
                                    AUTHOR.LAST_NAME,
                                    BOOK.ID,
                                    BOOK.TITLE
                            )
                            .from(AUTHOR)
                            .join(BOOK)
                            .on(AUTHOR.ID.eq(BOOK.AUTHOR_ID))
                            .orderBy(BOOK.ID.asc())
                            .fetch();

            assertEquals(4, result.size());
            assertEquals(Arrays.asList(1, 2, 3, 4), result.getValues(BOOK.ID));
        }
    }
}
