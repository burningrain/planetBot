package com.github.br.starmarines.gamecore.db.repositories;

public interface CrudRepository<E, ID> {
	
	void create(E entity);
	E read(ID id);
	void update(E entity);	
	void deleteById(ID key);

}
