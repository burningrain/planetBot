/**
 * This class is generated by jOOQ
 */
package org.jooq.db.generate.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.5" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GamesUsers extends org.jooq.impl.TableImpl<org.jooq.db.generate.tables.records.GamesUsersRecord> {

	private static final long serialVersionUID = -1282263719;

	/**
	 * The singleton instance of <code>star_marines_bot.games_users</code>
	 */
	public static final org.jooq.db.generate.tables.GamesUsers GAMES_USERS = new org.jooq.db.generate.tables.GamesUsers();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.db.generate.tables.records.GamesUsersRecord> getRecordType() {
		return org.jooq.db.generate.tables.records.GamesUsersRecord.class;
	}

	/**
	 * The column <code>star_marines_bot.games_users.game_id</code>.
	 */
	public final org.jooq.TableField<org.jooq.db.generate.tables.records.GamesUsersRecord, java.lang.Long> GAME_ID = createField("game_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>star_marines_bot.games_users.user_id</code>.
	 */
	public final org.jooq.TableField<org.jooq.db.generate.tables.records.GamesUsersRecord, java.lang.Long> USER_ID = createField("user_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>star_marines_bot.games_users.user_status_id</code>.
	 */
	public final org.jooq.TableField<org.jooq.db.generate.tables.records.GamesUsersRecord, java.lang.Short> USER_STATUS_ID = createField("user_status_id", org.jooq.impl.SQLDataType.SMALLINT.nullable(false), this, "");

	/**
	 * Create a <code>star_marines_bot.games_users</code> table reference
	 */
	public GamesUsers() {
		this("games_users", null);
	}

	/**
	 * Create an aliased <code>star_marines_bot.games_users</code> table reference
	 */
	public GamesUsers(java.lang.String alias) {
		this(alias, org.jooq.db.generate.tables.GamesUsers.GAMES_USERS);
	}

	private GamesUsers(java.lang.String alias, org.jooq.Table<org.jooq.db.generate.tables.records.GamesUsersRecord> aliased) {
		this(alias, aliased, null);
	}

	private GamesUsers(java.lang.String alias, org.jooq.Table<org.jooq.db.generate.tables.records.GamesUsersRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.db.generate.StarMarinesBot.STAR_MARINES_BOT, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<org.jooq.db.generate.tables.records.GamesUsersRecord> getPrimaryKey() {
		return org.jooq.db.generate.Keys.GAME_USER_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<org.jooq.db.generate.tables.records.GamesUsersRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.db.generate.tables.records.GamesUsersRecord>>asList(org.jooq.db.generate.Keys.GAME_USER_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<org.jooq.db.generate.tables.records.GamesUsersRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<org.jooq.db.generate.tables.records.GamesUsersRecord, ?>>asList(org.jooq.db.generate.Keys.GAMES_USERS__GAME_USER_GAME_ID_FKEY, org.jooq.db.generate.Keys.GAMES_USERS__GAME_USER_USER_ID_FKEY, org.jooq.db.generate.Keys.GAMES_USERS__USER_GAME_STATUS_FKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.db.generate.tables.GamesUsers as(java.lang.String alias) {
		return new org.jooq.db.generate.tables.GamesUsers(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.jooq.db.generate.tables.GamesUsers rename(java.lang.String name) {
		return new org.jooq.db.generate.tables.GamesUsers(name, null);
	}
}
