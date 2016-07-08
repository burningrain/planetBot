package com.github.br.starmarines.gamecore.db.repositories;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;

import com.github.br.starmarines.gamecore.db.DataSourceConfig;
import com.github.br.starmarines.gamecore.db.entities.GameEntity;
import com.github.br.starmarines.gamecore.db.entities.GameStatus;
import com.github.br.starmarines.gamecore.db.entities.User;
import com.github.br.starmarines.gamecore.db.entities.UserGameStatus;

import static org.jooq.db.generate.Tables.*;
import static org.jooq.db.generate.Sequences.*;

@Component
public class GameRepositoryImpl implements CrudRepository<GameEntity, Long> {

	private DataSource dataSource;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private DataSourceConfig dataSourceConfig;
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private void setDataSourceConfig(DataSourceConfig dataSourceConfig) {
		this.dataSourceConfig = dataSourceConfig;
	}

	private LogService logService;

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC, unbind = "unsetLogService")
	private void setLogService(LogService logService) {
		this.logService = logService;
	}

	@SuppressWarnings("unused")
	private void unsetLogService(LogService logService) {
		this.logService = null;
	}

	@Override
	public GameEntity read(Long id) {
		// FIXME переписать, добавить метод с параметрами, что именно выгружать.
		// Может, все и не нужно.
		try (Connection connection = dataSource.getConnection()) {
			DSLContext create = DSL.using(connection, dataSourceConfig.getSqlDialect());
			create.select().from(GAMES).join(PLANETS_RELATIONS)
					.on(GAMES.GAME_ID.equal(PLANETS_RELATIONS.GAME_ID))
					.join(PLANETS).on(GAMES.GAME_ID.equal(PLANETS.GAME_ID))
					.where(GAMES.GAME_ID.equal(id));
		} catch (SQLException e) {
			if (logService != null)
				logService.log(LogService.LOG_ERROR, "read game error", e);
		}

		return null;
	}

	@Override
	public void create(GameEntity entity) {
//		try (Connection connection = dataSource.getConnection()) {
//			DSLContext create = DSL.using(connection, dataSourceConfig.getSqlDialect());
//			// TODO смогу ли разрулить транзакции на уровне сервисов? И что для этого использовать?
//			create.insertInto(GAMES, 
//					GAMES.GAME_ID, GAMES.TITLE, GAMES.GAME_STATUS_ID)
//			.values(S_GAME_ID.nextval(), DSL.val(entity.getTitle()), DSL.val(GameStatus.WAITING_FOR_PLAYERS.getId()));  //TODO в postgreSQL нету однобайтного инта, только 2 байта. Так что, поменять в sql для h2
//			
//			
//		} catch (SQLException e) {
//			if (logService != null)
//				logService.log(LogService.LOG_ERROR, "create game", e);
//		}

	}

	@Override
	public void update(GameEntity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Long key) {
		// TODO Auto-generated method stub

	}

	/**
	 * Добавляем пользователей в игру, статус добавленных игроков IN_PROCESS
	 * @param gameId
	 * @param users
	 */
	public void addUsersToGame(Long gameId, User... users) {
//		try (Connection connection = dataSource.getConnection()) {
//			DSLContext create = DSL.using(connection, dataSourceConfig.getSqlDialect());
//			BatchBindStep batch = create.batch(
//					create.insertInto(GAMES_USERS,
//						GAMES_USERS.GAME_ID, GAMES_USERS.USER_ID,
//						GAMES_USERS.USER_STATUS_ID)
//					.values((Long) null, (Long) null, (Byte) null));
//
//			for (User user : users) {
//				batch.bind(gameId, user.getId(), UserGameStatus.IN_PROCESS.getStatus());
//			}
//			batch.execute();
//
//		} catch (SQLException e) {
//			if (logService != null)	logService.log(LogService.LOG_ERROR, "add users to game", e);
//		}
	}

	/**
	 * при начале и окончании игры обновляем игровой статус
	 * @param gameId
	 * @param status
	 */
	public void updateGameStatus(Long gameId, GameStatus status) {
//		try (Connection connection = dataSource.getConnection()) {
//			DSLContext create = DSL.using(connection, dataSourceConfig.getSqlDialect());
//			create.update(GAMES)
//			.set(GAMES.GAME_STATUS_ID, status.getId())
//			.where(GAMES.GAME_ID.equal(gameId))
//			.execute();					
//		} catch (SQLException e) {
//			if (logService != null) logService.log(LogService.LOG_ERROR, "update game status", e);
//		}

	}

}
