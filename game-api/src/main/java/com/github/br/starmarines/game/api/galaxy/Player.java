package com.github.br.starmarines.game.api.galaxy;

import java.util.Objects;
import java.util.UUID;

public class Player implements Comparable<Player> {

	public static final int IS_NOT_IN_GAME = -1;

	private final UUID id;
	private final String name;

	private int innerGamePlayerId = IS_NOT_IN_GAME;

	public Player(UUID id, String name) {
		this.id = Objects.requireNonNull(id);
		this.name = Objects.requireNonNull(name);
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setInnerGamePlayerId(int innerGamePlayerId) {
		this.innerGamePlayerId = innerGamePlayerId;
	}

	public int getInnerGamePlayerId() {
		return innerGamePlayerId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Player player = (Player) o;
		return id.equals(player.id) && name.equals(player.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public int compareTo(Player o) {
		return id.compareTo(o.id);
	}

}
