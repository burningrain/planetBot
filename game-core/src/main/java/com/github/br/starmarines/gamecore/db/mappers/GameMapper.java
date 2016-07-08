package com.github.br.starmarines.gamecore.db.mappers;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;

/**
 * @see http://www.jooq.org/doc/3.6/manual/sql-execution/fetching/pojos-with-recordmapper-provider/
 * http://www.jooq.org/doc/3.6/manual/sql-execution/fetching/recordmapper/
 * @author Пользователь
 *
 */
public class GameMapper implements RecordMapperProvider {

	@Override
	public <R extends Record, E> RecordMapper<R, E> provide(RecordType<R> arg0,
			Class<? extends E> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
